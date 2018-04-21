/**
 * 
 */
package edu.ou.cse.swlp.externalservices;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.ou.cse.swlp.util.ServiceUtil;

/**
 * @author Rashmi Pethe
 * This class acts as a service client for accessing Dandelion Semantic Similarity API end-point.  
 * Dandelion Semantic Similarity is an external API which compares the media description with 
 * DBPedia description to return Semantic Similarity ranks. 
 */
public class SemanticSimilarityServiceClient {
	private String apiURL = "https://api.dandelion.eu/datatxt/";
	private String serviceName = "sim/v1/?";
	private String apiToken = "e4f853c0480b497d95b1290887f4ac14";//"0b905b07c43f445badd9e0b5f542d1aa";

	/**
	 * This method invokes the Dandelion Semantic Similarity API
	 * to compare the media description with DBPedia description.
	 * @param dbPediConcept
	 * @param params
	 * @return Similarity Rank value as String 
	 */
	public String invokeSemSimilarityAPI(String dbPediConcept, String params){
		JSONParser jsonParser = new JSONParser();
		JSONObject responseObj;
		String similarityValue = null;
		String response = null;
		try {
			params = params.concat(apiToken);
			response = ServiceUtil.invokeService(apiURL, serviceName, params);
			if("ServerError".equalsIgnoreCase(response)){
				return "ServerError";
			} else if(response != null){
				responseObj =  (JSONObject) jsonParser.parse(response);
				similarityValue = (String) responseObj.get("similarity");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return similarityValue;
	}
}