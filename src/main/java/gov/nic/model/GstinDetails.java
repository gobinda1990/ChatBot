package gov.nic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GstinDetails {
	
	private String gstin;
    private String tradeName;
    private String legalName;
    private String chargeName;
    private String circleName;
    private String mobileNo;

}
