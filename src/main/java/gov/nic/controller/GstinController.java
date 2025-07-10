package gov.nic.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nic.exception.EncryptionException;
import gov.nic.exception.ResourceNotFoundException;
import gov.nic.model.ApiResponse;
import gov.nic.model.GstinDetails;
import gov.nic.service.GstService;
import gov.nic.util.EncryptionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
//@RequestMapping("/api/v1/gstin")
@RequestMapping("/api/gst")
@RequiredArgsConstructor
public class GstinController {

	private final GstService gstService;

	@GetMapping("/getDealerDetails")
	public ResponseEntity<?> getGstinDetails(@RequestParam String gstin,@RequestHeader("X-API-KEY") String apiKey,
            HttpServletRequest request) {
		log.info("Received  GSTIN request: {}", gstin);

		if (gstin == null || gstin.trim().isEmpty()) {
			throw new IllegalArgumentException("GSTIN must not be empty");
		}

		if (!gstin.matches("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$")) {
			throw new IllegalArgumentException("Invalid GSTIN ");
		}

		GstinDetails details = gstService.getGstDet(gstin).join();
		if (details == null) {
			throw new ResourceNotFoundException("No GSTIN details found for: " + gstin);
		}
		try {
			String json = new ObjectMapper().writeValueAsString(details);
			String encrypted = EncryptionUtil.encrypt(json);
			return ResponseEntity.ok(new ApiResponse<>(200, "Success", encrypted));
		} catch (Exception ex) {
			log.error("Encryption failed", ex);
			throw new EncryptionException("Error encrypting response", ex);
		}
	}

}
