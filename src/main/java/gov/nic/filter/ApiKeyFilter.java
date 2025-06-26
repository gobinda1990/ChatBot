package gov.nic.filter;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nic.model.ApiResponse;
import gov.nic.service.IpLogService;
import gov.nic.service.RateLimiterService;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Filter that validates the <code>X-API-KEY</code> header, applies IP‑based
 * rate limiting and logs request metadata.
 * <p>
 * 
 * <strong>Key improvements</strong>
 * <ul>
 * <li>Lombok’s <code>@RequiredArgsConstructor</code> &amp; <code>@Slf4j</code>
 * for boilerplate reduction</li>
 * <li>Constructor injection for Spring beans (testable &amp; immutable)</li>
 * <li>Centralised JSON error writing</li>
 * <li>Constants for header names &amp; max quota</li>
 * <li>Skips CORS pre‑flight and non‑API traffic via
 * <code>shouldNotFilter</code></li>
 * </ul>
 */

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {


	private static final String HEADER_API_KEY = "X-API-KEY";
	private static final String API_PREFIX = "/ChatBot/api/";
	private static final int MAX_REQUESTS = 100;
	

	@Value("${api.security.key}")
	private String expectedApiKey;

	private final RateLimiterService rateLimiterService;
	private final IpLogService ipLogService;
	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestUri = request.getRequestURI();
		String requestParams = buildRequestParams(request);
		String clientIp = resolveClientIp(request);

		log.info("Request URI: {} | Params: {}", requestUri, requestParams);

		if (!isValidApiKey(request.getHeader(HEADER_API_KEY))) {
			log.warn("Unauthorized  access attempt to {} with key: {}", request.getRequestURI(), "Unauthorized - Invalid API Key");
			writeError(response, HttpStatus.UNAUTHORIZED, "Unauthorized - Invalid API Key");
			ipLogService.logIp(clientIp, requestUri, requestParams, "FAIL");
			return;
		}

		ConsumptionProbe probe = rateLimiterService.checkIp(clientIp);
		if (!probe.isConsumed()) {
			setRetryAfterHeader(response, probe);
			writeError(response, HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
			ipLogService.logIp(clientIp, requestUri, requestParams, "RATE_LIMIT");
			return;
		}

		setRateLimitHeaders(response, probe);
		ipLogService.logIp(clientIp, requestUri, requestParams, "SUCCESS");
		filterChain.doFilter(request, response);
	}

	private boolean isValidApiKey(String apiKey) {
		return expectedApiKey != null && expectedApiKey.equals(apiKey);
	}

	private void writeError(HttpServletResponse response, HttpStatus status, String message) throws IOException {
		response.setStatus(status.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ApiResponse<Void> apiResponse = new ApiResponse<>(status.value(), message, null);
		response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
	}

	private void setRetryAfterHeader(HttpServletResponse response, ConsumptionProbe probe) {
		long retryAfterSeconds = probe.getNanosToWaitForRefill() / 1_000_000_000L;
		response.setHeader(HttpHeaders.RETRY_AFTER, String.valueOf(retryAfterSeconds));
	}

	private void setRateLimitHeaders(HttpServletResponse response, ConsumptionProbe probe) {
		response.setHeader("X-RateLimit-Limit", String.valueOf(MAX_REQUESTS));
		response.setHeader("X-RateLimit-Remaining", String.valueOf(probe.getRemainingTokens()));
		response.setHeader("X-RateLimit-Reset",
				String.valueOf(Instant.now().plusNanos(probe.getNanosToWaitForRefill()).getEpochSecond()));
	}

	private String buildRequestParams(HttpServletRequest request) {
		return request.getParameterMap().entrySet().stream().map(e -> e.getKey() + "=" + Arrays.toString(e.getValue()))
				.collect(Collectors.joining("&"));
	}

	private String resolveClientIp(HttpServletRequest request) {
		String forwarded = request.getHeader("X-Forwarded-For");
		return (forwarded == null || forwarded.isEmpty()) ? request.getRemoteAddr() : forwarded.split(",")[0].trim();
	}

	/**
	 * Exclude nonAPI paths and preflight requests.
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String uri = request.getRequestURI();
		boolean skip = !uri.startsWith(API_PREFIX) || HttpMethod.OPTIONS.matches(request.getMethod());
		log.debug("ApiKeyFilter.shouldNotFilter URI [{}] => {}", uri, skip);
		return skip;
	}
}
