package edu.ou.cse.swlp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import edu.ou.cse.swlp.beans.Media;
import edu.ou.cse.swlp.services.RelatedResourcesService;
/**
 * @author Rashmi Pethe
 * This class acts as a Controller for Related Resources.
 */

@Path("/relatedresources")
public class RelatedResourcesController {
	
	
	/**
	 * This method is invoked on the root of the application. If no key is provided,
	 * it will return with Unauthorized status code.
	 * @return String of JSON object
	 */
	@GET
	@Path("/")
	@Produces("application/json")
	public String getRelatedResources(){
		throw new WebApplicationException(Response.Status.UNAUTHORIZED);
	}
	
	/**
	 * This method gets the related resources for the tags.
	 * @param tags
	 * @param limit
	 * @param key
	 * @return List of Media Objects
	 */
	@GET
	@Path("/get")
	@Produces("application/json")
	public List<Media> getRelatedResources(@HeaderParam("tags") String tags, @HeaderParam("limit") int limit, @HeaderParam("key") String key){
		RelatedResourcesService service = new RelatedResourcesService();
		List<Media> mediaList = new ArrayList<Media>();
		if(service.isValidRequest(key)){
			mediaList = service.getRelatedResources(tags, limit);
		}else {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		return mediaList;
	}
}