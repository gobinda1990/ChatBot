package gov.nic.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import gov.nic.model.GstinDetails;

public class GstRowMapper implements RowMapper<GstinDetails> {

	@Override
	public GstinDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		GstinDetails gstDet = new GstinDetails();
		gstDet.setGstin(rs.getString("GSTIN"));
		gstDet.setTradeName(rs.getString("TRADE_NAME"));
		gstDet.setLegalName(rs.getString("LEGAL_NAME"));
		gstDet.setChargeName(rs.getString("CHARGE_NM"));
		gstDet.setCircleName(rs.getString("CIRCLE_NM"));
		gstDet.setMobileNo(rs.getString("MOBILE_NO"));
		return gstDet;
	}

}
