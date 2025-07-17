package gov.nic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nic.exception.EncryptionException;
import gov.nic.exception.ResourceNotFoundException;
import gov.nic.model.GstinDetails;
import gov.nic.service.GstService;
import gov.nic.model.ApiResponse;
import gov.nic.util.EncryptionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/gst")
@RequiredArgsConstructor
@Tag(name = "GSTIN Lookup", description = "API to fetch encrypted GSTIN dealer details")
public class GstController {

	private final GstService gstService;

	@Operation(summary = "Fetch encrypted GSTIN details", description = "Returns AES-encrypted GSTIN details like legal name, trade name, and mobile number")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved and encrypted GSTIN details"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid GSTIN format or missing input"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "GSTIN details not found"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal error during encryption") })

	@GetMapping("/getDealerDetails")
	public ResponseEntity<?> getGstinDetails(
			@Parameter(description = "GSTIN number to fetch details for", example = "22AAAAA0000A1Z5", required = true) @RequestParam String gstin,

			@Parameter(description = "API key for authentication", required = true) @RequestHeader("X-API-KEY") String apiKey,

			HttpServletRequest request) {
		log.info("Received GSTIN request: {}", gstin);

		if (gstin == null || gstin.trim().isEmpty()) {
			throw new IllegalArgumentException("GSTIN must not be empty");
		}

		if (!gstin.matches("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$")) {
			throw new IllegalArgumentException("Invalid GSTIN");
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
