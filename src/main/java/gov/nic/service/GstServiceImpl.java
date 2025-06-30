package gov.nic.service;

import org.springframework.stereotype.Service;
import gov.nic.model.GstinDetails;
import gov.nic.repository.GstRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class GstServiceImpl implements GstService {

	private final GstRepository gstRepository;

	@Override
	public GstinDetails getGstDet(String gstin) {

		log.info("Enter getGstDet() method with GSTIN::");

		GstinDetails details = gstRepository.getGstDet(gstin);

		return details;
	}

}
