package gov.nic.repository;

import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import gov.nic.exception.DatabaseException;
import gov.nic.model.HrmsDetails;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class HrmsRepositoryImpl implements HrmsRepository {
	
	private static final Logger logger=LoggerFactory.getLogger(HrmsRepositoryImpl.class);
	
	private final JdbcTemplate jdbcTemplate;

	@Override
	public List<HrmsDetails> getHrmsDet(String hrms) {
		
		logger.info("Enter getHrmsDet() method with HRMS Code: {}", hrms);

	    try {
	        return jdbcTemplate.query(HRMS_QRY, new HrmsRowMapper(), hrms);
	    } catch (EmptyResultDataAccessException ex) {
	        logger.warn("No HRMS details found for code: {}", hrms);
	        return Collections.emptyList();
	    } catch (DataAccessException ex) {
	        logger.error("Database error while fetching HRMS details for code: {}", hrms, ex);
	        throw new DatabaseException("Failed to fetch HRMS details", ex);
	    } catch (Exception ex) {
	        logger.error("Unexpected error in getHrmsDet for HRMS code: {}", hrms, ex);
	        throw new RuntimeException("Unexpected error occurred", ex);
	    }
	}

}
