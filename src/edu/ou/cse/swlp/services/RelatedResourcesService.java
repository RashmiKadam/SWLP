/**
 * 
 */
package edu.ou.cse.swlp.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.ou.cse.swlp.beans.Media;
import edu.ou.cse.swlp.constants.ApplicationConstants;
import edu.ou.cse.swlp.dao.RelatedResourcesDAO;
import edu.ou.cse.swlp.daoimpl.RelatedResourcesDAOImpl;
import edu.ou.cse.swlp.externalservices.DBPediaLookupServiceClient;
import edu.ou.cse.swlp.externalservices.IteBooksServiceClient;
import edu.ou.cse.swlp.externalservices.YouTubeDataServiceClient;
import edu.ou.cse.swlp.util.ServiceUtil;

/**
 * @author Rashmi Pethe
 * This class acts as service layer, which integrates all the external services as well as database service.
 */

public class RelatedResourcesService {

	/**
	 * This method gets all the related resources for the input tags
	 * @param tags 
	 * @param limit
	 * @return List of Media Objects
	 */
	public List<Media> getRelatedResources(String tags, int limit){
		List<Media> mediaLst = new ArrayList<Media>();
		
		RelatedResourcesDAO rrDao = new RelatedResourcesDAOImpl();
		mediaLst = rrDao.fetchResources(tags);
		
		String concept = ApplicationConstants.EMPTY_STRING;
		DBPediaLookupServiceClient dbPediaClient = new DBPediaLookupServiceClient();
		concept = dbPediaClient.lookUpDBPediaConcept(tags);
		
		if(mediaLst.isEmpty()){
			IteBooksServiceClient bookClient = new IteBooksServiceClient();
			mediaLst.addAll(bookClient.getRelatedBooks(tags, concept));
			
			YouTubeDataServiceClient videoClient = new YouTubeDataServiceClient();
			mediaLst.addAll(videoClient.getRelatedVideos(tags, concept));
		}
		if(mediaLst == null || mediaLst.isEmpty()){
			System.out.println("No related resources");
		}else{
			if(concept != null && "Skip".equalsIgnoreCase(concept)){
				Collections.shuffle(mediaLst);
			}else {
				ServiceUtil.rankBasedSorting(mediaLst);
			}
			if(limit != 0 && mediaLst.size() >= limit){
				mediaLst =  mediaLst.subList(0, limit);
			} else {
				return mediaLst;
			}
		}
		
		return mediaLst;
	}
	
	/**
	 * This method checks if the request is valid based on input key.
	 * If the input key is valid and matches with secret key, then request is valid.
	 * @param key
	 * @return boolean - true if valid else false
	 */
	public boolean isValidRequest(String key){
		boolean valid = false;
		if(ApplicationConstants.APPLICATION_REQUEST_KEY.equals(key)){
			valid = true;
		}
		return valid;
	}
}