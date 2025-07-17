package gov.nic.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dealer GSTIN details")
public class GstinDetails {
	@Schema(example = "22AAAAA0000A1Z5")
	private String gstin;
	@Schema(example = "ABC Pvt Ltd")
	private String tradeName;
	@Schema(example = "ABC Traders")
	private String legalName;
	private String chargeName;
	private String circleName;
	@Schema(example = "9876543210")
	private String mobileNo;

}
