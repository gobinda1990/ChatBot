package gov.nic.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import gov.nic.model.HrmsDetails;
import gov.nic.repository.HrmsRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HrmsServiceImpl implements HrmsService {

	private static final Logger logger = LoggerFactory.getLogger(HrmsServiceImpl.class);

	private final HrmsRepository hrmsRepository;

	@Override
	public List<HrmsDetails> getHrmsDet(String hrms) {
		logger.info("Enter getHrmsDet() method with HRMS Code: {}", hrms);		
		List<HrmsDetails> details = hrmsRepository.getHrmsDet(hrms);		
		return details;
	}

}
