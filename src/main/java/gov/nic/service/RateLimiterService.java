package gov.nic.service;

import java.time.Duration;
import org.springframework.stereotype.Service;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;


@Service
public class RateLimiterService {
	
	 private static final int CAPACITY = 300;
	    private static final Duration PERIOD = Duration.ofMinutes(1);

	    private final LoadingCache<String, Bucket> buckets = Caffeine.newBuilder()
	            .expireAfterAccess(Duration.ofHours(1))
	            .maximumSize(20_000)
	            .build(this::createBucket);

	    private Bucket createBucket(String apiKey) {
	        Bandwidth limit = Bandwidth.builder()
	                .capacity(CAPACITY)
	                .refillIntervally(CAPACITY, PERIOD)
	                .build();

	        return Bucket.builder()
	                .addLimit(limit)
	                .build();
	    }


	    public ConsumptionProbe tryConsume(String apiKey) {
	        return buckets.get(apiKey).tryConsumeAndReturnRemaining(1);
	    }
}
