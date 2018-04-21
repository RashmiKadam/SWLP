/**
 * 
 */
package edu.ou.cse.swlp.externalservices;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
 * This class acts as a service client for accessing ITeBooks API end-point.  
 * ITeBooks is an external API which provides the list of books for search keyword. 
 */
public class IteBooksServiceClient {
	private String apiURL = "http://it-ebooks-api.info/v1/";
	
	/**
	 * This method will invoke search method for books based on the input tags.
	 * @param keyword - input tags
	 * @return an ArrayList of Media objects
	 */
	public ArrayList<Media> searchBooks(String keyword){
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj;
		String response;
		Media media;
		ArrayList<Media> mediaList = new ArrayList<Media>();
		response = ServiceUtil.invokeService(apiURL, ApplicationConstants.SEARCH_URI, keyword);
		System.out.println(response);
		try {
			jsonObj = (JSONObject) jsonParser.parse(response);
			JSONArray jsonArray = (JSONArray) jsonObj.get(ApplicationConstants.BOOKS);
			if(jsonArray != null && !jsonArray.isEmpty()){
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject bookObj = (JSONObject) jsonArray.get(i);
					media = new Media();
					media.setID(bookObj.get(ApplicationConstants.BOOK_ID).toString());
					media.setTitle(bookObj.get(ApplicationConstants.BOOK_TITLE).toString());
					media.setDescription(bookObj.get(ApplicationConstants.BOOK_DESC).toString());
					media.setImage(bookObj.get(ApplicationConstants.BOOK_IMAGE).toString());
					media.setISBN(bookObj.get(ApplicationConstants.BOOK_ISBN).toString());
					media.setMediaType(ApplicationConstants.BOOK_MEDIA_TYPE);
					media.setResourceURL(bookObj.get(ApplicationConstants.BOOK_IMAGE).toString());
					media.setTags(keyword);
					mediaList.add(media);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(mediaList != null){
			insertBooksToDatabase(mediaList);
		}
		return mediaList;
	}
	
	/**
	 * This method inserts the books into database corresponding to tags.
	 * @param bookList
	 */
	private void insertBooksToDatabase(ArrayList<Media> bookList){
		Gson gson = new Gson();
		List jsonList = new ArrayList();
		
		RelatedResourcesDAO relatedResourcesDaoImpl = new RelatedResourcesDAOImpl();
		
		for (Media media : bookList) {
			String jsonObj = gson.toJson(media);
			jsonList.add(jsonObj);
		}
		relatedResourcesDaoImpl.insertResources(jsonList);
	}
	
	/**
	 * To be implemented for Future Scope.
	 */
	/*public void getBooksDetails(){
		
	}*/
	
	/**
	 * This method gets the list of books and invoke semantic similarity on the book list. 
	 * @param keyword
	 * @param dbPediaConcept
	 * @return an ArrayList of Media objects
	 */
	public ArrayList<Media> getRelatedBooks(String keyword, String dbPediaConcept) {
		ArrayList<Media> mediaLst = new ArrayList<>();

		mediaLst = searchBooks(keyword);
		
		//If no DBpedia definition available for the tag, skip the Semantic Similarity
		if(dbPediaConcept.equalsIgnoreCase("Skip")) {
			return mediaLst;
		} else {
			mediaLst = updateBookSimilarityRank(mediaLst, dbPediaConcept);
			return mediaLst;
		}
	}
	
	/**
	 * This method gets the Semantic Similarity rank and updates to Media Object.
	 * @param mediaLst
	 * @param dbPediaConcept
	 * @return an ArrayList of Media Object with Similarity ranks
	 */
	private ArrayList<Media> updateBookSimilarityRank(ArrayList<Media> mediaLst, String dbPediaConcept){
		String text1, text2;
		String params;
		SemanticSimilarityServiceClient semClient = new SemanticSimilarityServiceClient();
		try {
			for (Media media : mediaLst) {
				String description = media.getDescription();
				text1 = ServiceUtil.encodeParams(dbPediaConcept);
				text2 = ServiceUtil.encodeParams(description);
				params = "text1=".concat(text1).concat(ApplicationConstants.AND).concat("text2=").concat(text2).concat(ApplicationConstants.AND).concat("token=");
				
				String similarityRankStr = semClient.invokeSemSimilarityAPI(dbPediaConcept, params);
				if("ServerError".equalsIgnoreCase(similarityRankStr)){
					media.setErrorMsg("ServerError");
					return mediaLst;
				} else if(similarityRankStr != null){
					Double similarityRank = Double.valueOf(similarityRankStr);
					media.setSimilarityValue(similarityRank);;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return mediaLst;
	}
}