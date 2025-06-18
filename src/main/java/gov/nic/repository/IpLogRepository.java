package gov.nic.repository;

import java.sql.Timestamp;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class IpLogRepository {	
	
    private final JdbcTemplate jdbcTemplate;
    
    public void saveIpLog(String ip, String path,String requestParams, String status) {
        String sql = "INSERT INTO IP_LOG (IP_ADDRESS, REQUEST_URI, REQUEST_PARAMS, STATUS,LOG_TIME) VALUES (?, ?, ?,?,?)";
        jdbcTemplate.update(sql, ip, path, requestParams,status,new Timestamp(System.currentTimeMillis()));
    }

}
