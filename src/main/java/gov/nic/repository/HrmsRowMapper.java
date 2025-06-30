package gov.nic.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import gov.nic.model.HrmsDetails;

public class HrmsRowMapper implements RowMapper<HrmsDetails> {

	@Override
	public HrmsDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		HrmsDetails hrdt = new HrmsDetails();
		hrdt.setHrmsCd(rs.getString("HRMS_CODE"));
		hrdt.setChrgCd(rs.getString("CHARGE_CD"));
		hrdt.setChrgNm(rs.getString("CHARGE_NM"));
		hrdt.setDg(rs.getString("DESIGNATION"));
		hrdt.setMobileNo(rs.getString("MOBILE"));
		hrdt.setRoles(rs.getString("USR_CATG_NM"));
		return hrdt;
	}

}
