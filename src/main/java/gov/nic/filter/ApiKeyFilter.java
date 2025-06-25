package gov.nic.filter;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(ApiKeyFilter.class);

	@Value("${api.security.key}")
	private String expectedApiKey;

	@Autowired
	private IpLogService ipLogService;
	
	@Autowired
	private RateLimiterService rateLimiterService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String apiKey = request.getHeader("X-API-KEY");
		String clientIp = getClientIp(request);
		String requestUri = request.getRequestURI();
		String requestParams = getRequestParams(request);
		
		logger.info("Request URI: {}", requestUri);
		logger.info("Request Params: {}", requestParams);
		if (!expectedApiKey.equals(apiKey)) {
			logger.warn("Unauthorized access to {} from IP {} with key: {}", requestUri, clientIp, apiKey);
			ipLogService.logIp(clientIp, requestUri, requestParams, "FAIL");
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            ApiResponse<Void> apiResponse = new ApiResponse<>(401, "Unauthorized - Invalid API Key", null);
            ObjectMapper mapper = new ObjectMapper();
            String responseBody = mapper.writeValueAsString(apiResponse);
            response.getWriter().write(responseBody);
            return;
		}
		ConsumptionProbe probe = rateLimiterService.checkIp(clientIp);
		if (!probe.isConsumed()) {
	        long retryAfterSeconds = probe.getNanosToWaitForRefill() / 1_000_000_000;
	        response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));
	       // response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "Too many requests");
	        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            ApiResponse<Void> apiResponse = new ApiResponse<>(429, "Too many requests", null);
            ObjectMapper mapper = new ObjectMapper();
            String responseBody = mapper.writeValueAsString(apiResponse);
            response.getWriter().write(responseBody);
	        return;
	    }
		response.setHeader("X-RateLimit-Limit", "100");
		response.setHeader("X-RateLimit-Remaining", String.valueOf(probe.getRemainingTokens()));
		response.setHeader("X-RateLimit-Reset", String.valueOf(
	        Instant.now().plusNanos(probe.getNanosToWaitForRefill()).getEpochSecond()
	    ));
		ipLogService.logIp(clientIp, requestUri, requestParams, "SUCCESS");
		filterChain.doFilter(request, response);
	}

	private String getClientIp(HttpServletRequest request) {
		String xfHeader = request.getHeader("X-Forwarded-For");
		return (xfHeader == null || xfHeader.isEmpty()) ? request.getRemoteAddr() : xfHeader.split(",")[0].trim();
	}

	private String getRequestParams(HttpServletRequest request) {
		Map<String, String[]> paramMap = request.getParameterMap();
		return paramMap.entrySet().stream().map(e -> e.getKey() + "=" + Arrays.toString(e.getValue()))
				.collect(Collectors.joining("&"));
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String uri = request.getRequestURI();
		boolean skip = !uri.startsWith("/ChatBot/api/");
		logger.debug("ApiKeyFilter shouldNotFilter URI [{}] => {}", uri, skip);
		return skip;
	}

}
