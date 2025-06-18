package gov.nic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import gov.nic.model.GstinDetails;
import gov.nic.repository.GstRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GstServiceImpl implements GstService {
	
	private static final Logger logger=LoggerFactory.getLogger(GstServiceImpl.class);
	
	private final GstRepository gstRepository;

	@Override
    public GstinDetails getGstDet(String gstin) {
		
        logger.info("Enter getGstDet() method with GSTIN:: {}", gstin);       

        GstinDetails details = gstRepository.getGstDet(gstin);
       
        return details;
    }

}
