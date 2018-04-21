package edu.ou.cse.swlp.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import edu.ou.cse.swlp.beans.Media;
import edu.ou.cse.swlp.constants.ApplicationConstants;

/**
 * @author Rashmi Pethe
 * This class is util class for connecting to external API's.
 */
public class ServiceUtil {
	private static URL url;
	private static HttpURLConnection con;
	
	/**
	 * This method invokes the requested API service.
	 * @param apiURL - URL for the requested external API
	 * @param serviceName - service method requested
	 * @param params - list of params to SPI
	 * @return String - response as String
	 */
	public static String invokeService(String apiURL, String serviceName, String params){
		String response = ApplicationConstants.EMPTY_STRING;
		try {
			url = new URL(apiURL.concat(serviceName).concat(params));
			con = (HttpURLConnection) url.openConnection();
			// Set request method as GET
			con.setRequestMethod(ApplicationConstants.REQUEST_METHOD_GET); 
			// Set accepted response as JSON
			con.setRequestProperty("Accept", "application/json");
			
			con.connect();
			if (con.getResponseCode() != 200) {
				System.out.println("Failed : HTTP error code : "
						+ con.getResponseCode());
				System.out.println("Connection failed...Server down");
				return "ServerError";
			}

			Scanner scanner = new Scanner(url.openStream());
			while (scanner.hasNext()) {
				response += scanner.nextLine();
			}
			scanner.close();
			con.disconnect();

		  } catch (MalformedURLException e) {
			e.printStackTrace();
		  } catch (IOException e) {
			e.printStackTrace();
		  }
		return response;
	}
	
	/**
	 * This method prepares the request for YouTube Data API service.
	 * @return String - video request as String
	 */
	public static String prepareVideoRequest(){
		String part = ApplicationConstants.EMPTY_STRING;
		String maxResults = ApplicationConstants.EMPTY_STRING;
		String searchQuery = ApplicationConstants.EMPTY_STRING;
		String type = ApplicationConstants.EMPTY_STRING;
		String apiKey = ApplicationConstants.EMPTY_STRING;
		
		apiKey = apiKey.concat(ApplicationConstants.API_KEY_PARAM).concat(ApplicationConstants.EQUALITY_OP).concat(ApplicationConstants.API_KEY);
		part = part.concat(ApplicationConstants.PART_PARAM).concat(ApplicationConstants.EQUALITY_OP).concat(ApplicationConstants.PART);
		type = type.concat(ApplicationConstants.TYPE_PARAM).concat(ApplicationConstants.EQUALITY_OP).concat(ApplicationConstants.TYPE);
		maxResults = maxResults.concat(ApplicationConstants.MAXRESULTS_PARAM).concat(ApplicationConstants.EQUALITY_OP).concat(ApplicationConstants.MAXRESULTS);
		searchQuery = searchQuery.concat(ApplicationConstants.SEARCH_QUERY_PARAM).concat(ApplicationConstants.EQUALITY_OP);
		
		String videoReq = ApplicationConstants.QUESTION_MARK;
		videoReq = videoReq.concat(apiKey).concat(ApplicationConstants.AND).concat(part).concat(ApplicationConstants.AND).concat(type).concat(ApplicationConstants.AND)
				.concat(maxResults).concat(ApplicationConstants.AND).concat(searchQuery);
		return videoReq;
	}
	
	/**
	 * This method encodes the params to standard UTF-8 format.
	 * @param param
	 * @throws UnsupportedEncodingException
	 * @return encoded param as String
	 */
	public static String encodeParams(String param) throws UnsupportedEncodingException{
		String encodedParam = ApplicationConstants.EMPTY_STRING;
		if(param != null){
			encodedParam = URLEncoder.encode(param, ApplicationConstants.ENCODE_FORMAT);
		}
		return encodedParam;
	}
	
	/**
	 * This method sorts the Media objects based on Similarity ranks. 
	 * @param videoLst - List of Media 
	 */
	public static void rankBasedSorting(List<Media> mediaLst){
		Collections.sort(mediaLst, new Comparator<Object>(){
			public int compare(Object o1, Object o2){
				Double simValue1 = ((Media) o1).getSimilarityValue();
				Double simValue2 = ((Media) o2).getSimilarityValue();
				if( simValue1 != null && simValue2 != null){
					return (simValue2).compareTo(simValue1);
				} else {
					return -1;
				}
			}
		});
	}
}