package gov.nic.service;

import java.util.List;
import org.springframework.stereotype.Service;
import gov.nic.model.HrmsDetails;
import gov.nic.repository.HrmsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HrmsServiceImpl implements HrmsService {

	private final HrmsRepository hrmsRepository;

	@Override
	public List<HrmsDetails> getHrmsDet(String hrms) {
		log.info("Enter getHrmsDet() method with HRMS Code: {}", hrms);
		List<HrmsDetails> details = hrmsRepository.getHrmsDet(hrms);
		return details;
	}

}
