package gov.nic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.nic.repository.IpLogRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IpLogService {
	
private static final Logger logger=LoggerFactory.getLogger(IpLogService.class);
	
	private final IpLogRepository ipLogRepository;
	
	@Transactional(rollbackFor = Exception.class)
	public void logIp(String ip, String path,String requestParams, String status) {
        logger.info("Enter into logIp() method:-- ");
        ipLogRepository.saveIpLog(ip, path,requestParams, status);
        logger.info("Exit from logIp() method:--");
    }

}
