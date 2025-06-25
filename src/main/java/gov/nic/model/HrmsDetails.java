package gov.nic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrmsDetails {
	
	private String hrmsCd;
	private String chrgCd;
	private String chrgNm;
	private String dg;
	private String mobNo;
	private String roles;

}
