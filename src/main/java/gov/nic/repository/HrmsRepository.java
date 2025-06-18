package gov.nic.repository;

import java.util.List;
import gov.nic.model.HrmsDetails;

public interface HrmsRepository {
	
	String HRMS_QRY=" SELECT A.HRMS_CODE, A.CHARGE_CD, B.DESIGNATION, C.MOBILE, D.USR_CATG_NM "
			+ " FROM USR_CD A "
			+ " LEFT JOIN DESIGNATION_CD B ON A.DESIG = B.DESIG_CD "
			+ " LEFT JOIN IMPACT_USER_PROFILE C ON A.USR_CD = C.USR_CD "
			+ " LEFT JOIN USR_CATEGORY D ON A.USR_CATG_CD = D.USR_CATG_CD "
			+ " WHERE A.HRMS_CODE = ? ";

	public List<HrmsDetails> getHrmsDet(String hrms);

}
