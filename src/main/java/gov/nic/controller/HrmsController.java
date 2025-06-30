package gov.nic.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nic.exception.ResourceNotFoundException;
import gov.nic.model.ApiResponse;
import gov.nic.model.HrmsDetails;
import gov.nic.service.HrmsService;
import gov.nic.util.EncryptionUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
//@RequestMapping("/api/v1/hrms")
@RequestMapping("/api")
@RequiredArgsConstructor
public class HrmsController {
	
	
//	private static final Logger logger=LoggerFactory.getLogger(HrmsController.class);	
	private final HrmsService hrmsService;
	
//	@GetMapping("/details")
	@GetMapping("/userInfo")
	public ResponseEntity<?> getHrmsDetails(@RequestParam String hrmsCode,@RequestHeader("X-API-KEY") String apiKey,
            HttpServletRequest request) {
	   
		log.info("Received HRMS request: {}", hrmsCode);
	    // Validate HRMS format
	    if (hrmsCode == null || hrmsCode.trim().isEmpty()) {
	        throw new IllegalArgumentException("HRMS code must not be null or blank");
	    }
	    if (!hrmsCode.matches("^\\d{10}$")) {
	        throw new IllegalArgumentException("Invalid HRMS.");
	    }
	    List<HrmsDetails> details = hrmsService.getHrmsDet(hrmsCode);
	    if (details == null || details.isEmpty()) {
	        throw new ResourceNotFoundException("No HRMS details found for: " + hrmsCode);
	    }
	    try {
	        String json = new ObjectMapper().writeValueAsString(details);
	        String encryptedResponse = EncryptionUtil.encrypt(json);
	        return ResponseEntity.ok(new ApiResponse<>(200, "Success", encryptedResponse));
	    } catch (Exception ex) {
	        log.error("Error processing HRMS response", ex.getMessage());
	        throw new RuntimeException("Internal error", ex);
	    }
	}

}
