package gov.nic.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import gov.nic.model.HrmsDetails;

public interface HrmsService {
	
	public CompletableFuture<List<HrmsDetails>> getHrmsDet(String hrms);

}
