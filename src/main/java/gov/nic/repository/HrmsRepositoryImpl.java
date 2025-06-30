package gov.nic.repository;

import java.util.Collections;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import gov.nic.exception.DatabaseException;
import gov.nic.model.HrmsDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@AllArgsConstructor
public class HrmsRepositoryImpl implements HrmsRepository {
	
	private final JdbcTemplate jdbcTemplate;

	@Override
	public List<HrmsDetails> getHrmsDet(String hrms) {
		
		log.info("Enter getHrmsDet() method with HRMS Code: {}", hrms);

	    try {
	        return jdbcTemplate.query(HRMS_QRY, new HrmsRowMapper(), hrms);
	    } catch (EmptyResultDataAccessException ex) {
	        log.warn("No HRMS details found for code: {}", hrms);
	        return Collections.emptyList();
	    } catch (DataAccessException ex) {
	        log.error("Database error while fetching HRMS details for code: {}", hrms, ex);
	        throw new DatabaseException("Failed to fetch HRMS details", ex);
	    } catch (Exception ex) {
	        log.error("Unexpected error in getHrmsDet for HRMS code: {}", hrms, ex);
	        throw new RuntimeException("Unexpected error occurred", ex);
	    }
	}

}
