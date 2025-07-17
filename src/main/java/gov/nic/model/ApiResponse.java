package gov.nic.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Standard API response wrapper")
public class ApiResponse<T> {
	@Schema(description = "HTTP status code", example = "200")
	private int status;
	@Schema(description = "Response message", example = "Success")
	private String message;
	@Schema(description = "Encrypted payload")
	private T data;

}
