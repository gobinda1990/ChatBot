package gov.nic.repository;

import gov.nic.model.GstinDetails;

public interface GstRepository {
	
	String GST_QRY="SELECT A.GSTIN, A.TRADE_NAME, A.LEGAL_NAME, C.CHARGE_NM, D.CIRCLE_NM, "
			+ "  TO_CHAR(A.MOBILE_NO) AS MOBILE_NO FROM ACS_MAST.GST_DEALER_MASTER_WBCOMTAX A "
			+ "  INNER JOIN GST_MASTER_JURISDICTION B ON A.ST_JURI = B.JURISDICTION_CODE "
			+ "  INNER JOIN CHARGE_CD C ON B.CHARGE_CD = C.CHARGE_CD "
			+ "  INNER JOIN CIRCLE_CD D ON C.CIRCLE_CD = D.CIRCLE_CD "
			+ "  WHERE A.GSTIN = ?";
	
	public GstinDetails getGstDet(String gstin);

}
