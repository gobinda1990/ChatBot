package gov.nic.service;

import java.util.concurrent.CompletableFuture;

import gov.nic.model.GstinDetails;

public interface GstService {
	
	public CompletableFuture<GstinDetails> getGstDet(String gstin);

}
