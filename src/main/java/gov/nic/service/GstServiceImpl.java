package gov.nic.service;

import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Service;
import gov.nic.exception.ServiceUnavailableException;
import gov.nic.model.GstinDetails;
import gov.nic.repository.GstRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class GstServiceImpl implements GstService {

	private final GstRepository gstRepository;

	@CircuitBreaker(name = "gstCB", fallbackMethod = "fallback")
	@Retry(name = "gstRetry", fallbackMethod = "fallback")
	@TimeLimiter(name = "gstCB")
	public CompletableFuture<GstinDetails> getGstDet(String gstin) {
		log.info("Invoking getGstDet for GSTIN: {}", gstin);
		return CompletableFuture.supplyAsync(() -> gstRepository.getGstDet(gstin));
	}

	public CompletableFuture<GstinDetails> fallback(String gstin, Throwable t) {
		log.error("Fallback triggered for GSTIN: {}, due to: {}", gstin, t.getMessage());
		throw new ServiceUnavailableException("Service temporarily unavailable. Try again later.", t);
	}

}
