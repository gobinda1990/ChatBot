package gov.nic.repository;


import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import gov.nic.exception.DatabaseException;
import gov.nic.model.GstinDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GstRepositoryImpl implements GstRepository {

	
	private final JdbcTemplate jdbcTemplate;

	@Override
	public GstinDetails getGstDet(String gstin) {

		log.info("Enter getGstDet() method with GSTIN: {}", gstin);

		try {
			return jdbcTemplate.queryForObject(GST_QRY, new GstRowMapper(), gstin);

		} catch (EmptyResultDataAccessException ex) {
			log.warn("No GSTIN details found for: {}", gstin);
			return null;
		} catch (DataAccessException ex) {
			log.error("Database error occurred while fetching GSTIN details for: {}", gstin, ex);
			throw new DatabaseException("Failed to fetch GSTIN details", ex); // optional custom exception
		} catch (Exception ex) {
			log.error("Unexpected error in getGstDet for GSTIN: {}", gstin, ex);
			throw new RuntimeException("Unexpected error occurred", ex);
		}
	}

}
