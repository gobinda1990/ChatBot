package gov.nic.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import gov.nic.exception.DatabaseException;
import gov.nic.model.GstinDetails;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GstRepositoryImpl implements GstRepository {

	private static final Logger logger = LoggerFactory.getLogger(GstRepositoryImpl.class);

	
	private final JdbcTemplate jdbcTemplate;

	@Override
	public GstinDetails getGstDet(String gstin) {

		logger.info("Enter getGstDet() method with GSTIN: {}", gstin);

		try {
			return jdbcTemplate.queryForObject(GST_QRY, new GstRowMapper(), gstin);

		} catch (EmptyResultDataAccessException ex) {
			logger.warn("No GSTIN details found for: {}", gstin);
			return null;
		} catch (DataAccessException ex) {
			logger.error("Database error occurred while fetching GSTIN details for: {}", gstin, ex);
			throw new DatabaseException("Failed to fetch GSTIN details", ex); // optional custom exception
		} catch (Exception ex) {
			logger.error("Unexpected error in getGstDet for GSTIN: {}", gstin, ex);
			throw new RuntimeException("Unexpected error occurred", ex);
		}
	}

}
