/**
 * 
 */
package edu.ou.cse.swlp.externalservices;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

import edu.ou.cse.swlp.beans.Media;
import edu.ou.cse.swlp.constants.ApplicationConstants;
import edu.ou.cse.swlp.dao.RelatedResourcesDAO;
import edu.ou.cse.swlp.daoimpl.RelatedResourcesDAOImpl;
import edu.ou.cse.swlp.util.ServiceUtil;

/**
 * @author Rashmi Pethe
 * This class acts as a service client for accessing YouTube Data API end-point.  
 * YouTube Data is an external API which provides the list of videos for search keyword. 
 */
public class YouTubeDataServiceClient {
	
	/**
	 * This method invokes the YouTube Data service for the input tags as keyword search.
	 * @param keyword - input tags
	 * @return an ArrayList of Media Objects  
	 * @throws UnsupportedEncodingException 
	 */
	public ArrayList<Media> getVideos(String keyword) throws UnsupportedEncodingException{
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj;
		
		Media media;
		ArrayList<Media> mediaList = new ArrayList<Media>();
		String response = ApplicationConstants.EMPTY_STRING;
		
		String params = ServiceUtil.prepareVideoRequest().concat(ServiceUtil.encodeParams(keyword));
		response = ServiceUtil.invokeService(ApplicationConstants.API_URL, ApplicationConstants.SEARCH_URI, params);
		
		System.out.println(response);
		try {
			String videoId = null;
			jsonObj = (JSONObject) jsonParser.parse(response);
			JSONArray jsonArray = (JSONArray) jsonObj.get(ApplicationConstants.ITEMS);
			if(jsonArray != null && !jsonArray.isEmpty()){
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject videoObj = (JSONObject) jsonArray.get(i);
					media = new Media();
					JSONObject idObj = (JSONObject) videoObj.get(ApplicationConstants.ID);
					
					videoId = idObj.get(ApplicationConstants.VIDEO_ID).toString();
					media.setID(videoId);
					
					JSONObject snippetObj = (JSONObject) videoObj.get(ApplicationConstants.SNIPPET);
					media.setTitle(snippetObj.get(ApplicationConstants.TITLE).toString());
					
					media.setDescription(snippetObj.get(ApplicationConstants.DESC).toString());
					media.setMediaType(ApplicationConstants.VIDEO_MEDIA_TYPE);
					media.setTags(keyword);
					media.setResourceURL(ApplicationConstants.YOUTUBE_URL.concat(videoId));
					mediaList.add(media);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(mediaList != null){
			insertVideosToDatabase(mediaList);
		}
		return mediaList;
	}
	
	/**
	 * This method inserts the video results corresponding to tags into database.
	 * @param videoList - list of videos
	 */
	private void insertVideosToDatabase(ArrayList<Media> videoList){
		Gson gson = new Gson();
		List jsonList = new ArrayList();
		
		RelatedResourcesDAO relatedResourcesDaoImpl = new RelatedResourcesDAOImpl();
		
		for (Media media : videoList) {
			String jsonObj = gson.toJson(media);
			jsonList.add(jsonObj);
		}
		relatedResourcesDaoImpl.insertResources(jsonList);
	}
	
	/**
	 * This method gets the list of videos and invoke semantic similarity on the video list. 
	 * @param keyword - input tags
	 * @param dbPediaConcept - DBpedia definition for tags
	 * @return videoLst as Collection 
	 */
	public Collection getRelatedVideos(String keyword, String dbPediaConcept){
		ArrayList<Media> videoLst = new ArrayList<>();
		try {
			videoLst = getVideos(keyword);
			
			if(dbPediaConcept.equalsIgnoreCase("Skip")) {
				return videoLst;
			} else {
				videoLst = updateVideoSimilarityRank(videoLst, dbPediaConcept);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return videoLst;
	}
	
	/**
	 * This method gets the Semantic Similarity rank and updates to Media Object.
	 * @param videoLst - List of videos
	 * @param dbPediaConcept - DBpedia definition
	 * @return an ArrayList of Media Objects
	 */
	private ArrayList<Media> updateVideoSimilarityRank(ArrayList<Media> videoLst, String dbPediaConcept){
		String text1;
		String params;
			
		SemanticSimilarityServiceClient semClient = new SemanticSimilarityServiceClient();
		try {
			for (Media media : videoLst) {
				text1 = ServiceUtil.encodeParams(dbPediaConcept);
				params = "text1=".concat(text1).concat("&").concat("url2=").concat(media.getResourceURL()).concat("&").concat("token=");
				
				String similarityRankStr = semClient.invokeSemSimilarityAPI(dbPediaConcept, params);
				if("ServerError".equalsIgnoreCase(similarityRankStr)){
					media.setErrorMsg("ServerError");
					return videoLst;
				} else if(similarityRankStr != null){
					Double similarityRank = Double.valueOf(similarityRankStr);
					media.setSimilarityValue(similarityRank);;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return videoLst;
	}
}