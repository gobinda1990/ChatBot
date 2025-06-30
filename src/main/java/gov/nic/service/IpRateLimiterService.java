package gov.nic.service;

import java.time.Duration;
import org.springframework.stereotype.Service;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IpRateLimiterService {

	private static final int CAPACITY = 10; // 10 requests
	private static final Duration PERIOD = Duration.ofSeconds(1); // per second

	private final LoadingCache<String, RateLimiter> ipLimiterCache;

	public IpRateLimiterService() {
		RateLimiterConfig config = RateLimiterConfig.custom().limitForPeriod(CAPACITY).limitRefreshPeriod(PERIOD)
				.timeoutDuration(Duration.ZERO) // fail‑fast, no blocking
				.build();

		ipLimiterCache = Caffeine.newBuilder().expireAfterAccess(Duration.ofMinutes(10)).maximumSize(10_000)
				.build(ip -> RateLimiter.of("ip-" + ip, config));
	}

	/** @return true if the request is permitted */
	public boolean tryConsume(String ip) {
		return ipLimiterCache.get(ip).acquirePermission();
	}

}
