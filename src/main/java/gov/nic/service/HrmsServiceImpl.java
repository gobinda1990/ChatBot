package gov.nic.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import gov.nic.exception.ServiceUnavailableException;
import gov.nic.model.HrmsDetails;
import gov.nic.repository.HrmsRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HrmsServiceImpl implements HrmsService {

	private final HrmsRepository hrmsRepository;

	@CircuitBreaker(name = "hrmsCB", fallbackMethod = "fallback")
	@Retry(name = "hrmsRetry", fallbackMethod = "fallback")
	@TimeLimiter(name = "hrmsCB")
	public CompletableFuture<List<HrmsDetails>> getHrmsDet(String code) {
		log.info("Invoking getHrmsDet for code: {}", code);
		return CompletableFuture.supplyAsync(() -> hrmsRepository.getHrmsDet(code));
	}

	public CompletableFuture<List<HrmsDetails>> fallback(String code, Throwable t) {
		log.error("Fallback triggered for HRMS code: {}, due to: {}", code, t.getMessage());
		throw new ServiceUnavailableException("Service temporarily unavailable. Try again later.", t);
	}

}
