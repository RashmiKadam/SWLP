/**
 * 
 */
package edu.ou.cse.swlp.externalservices;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import edu.ou.cse.swlp.util.ServiceUtil;

/**
 * @author Rashmi Pethe
 * This class acts as a service client for accessing DBpedia Lookup API end-point.  
 * DBPedia Lookup is an external API which provides the definition for search keyword from DBPedia. 
 */
public class DBPediaLookupServiceClient {
	private String apiURL = "http://lookup.dbpedia.org/api/search";
	private String serviceName = "/KeywordSearch";
	private String paramName = "?QueryString=";
	
	/**
	 * This method looks up for the description for given params in DBPedia.
	 * It performs the keyword search.
	 * @param params
	 * @return 
	 */
	public String lookUpDBPediaConcept(String params){
		JSONObject jsonObj;
		String desc = "Skip";
		try {
			paramName = paramName.concat(ServiceUtil.encodeParams(params));
			String response = ServiceUtil.invokeService(apiURL, serviceName, paramName);
			System.out.println(response);
			
			jsonObj = XML.toJSONObject(response);
			JSONObject obj = (JSONObject) jsonObj.get("ArrayOfResult");
			
			if(obj.has("Result")){
				Object tmpObj = obj.get("Result");
				if(tmpObj instanceof JSONObject){
					JSONObject concept = (JSONObject) tmpObj;
					desc = concept.get("Description").toString();
					return desc;
				}else{
					JSONArray jsonArray = (JSONArray) obj.get("Result");
					if(jsonArray != null ){
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject concept = (JSONObject) jsonArray.get(i);
							desc = concept.get("Description").toString();
							return desc;
						}
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return desc;
	}
}