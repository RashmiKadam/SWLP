package edu.ou.cse.swlp.dao;

import java.util.ArrayList;
import java.util.List;

import edu.ou.cse.swlp.beans.Media;

/**
 * @author Rashmi Pethe
 * This is an interface for related resources DAO layer.
 */
public interface RelatedResourcesDAO {
	
	/**
	 * This method fetches the related resources for given tags from database.
	 * @return ArrayList of Media Objects
	 */
	public ArrayList<Media> fetchResources(String tags);
	
	/**
	 * This method inserts the related resources into database.
	 * @param jsonList - List of Media objects
	 */
	public void insertResources(List jsonList);

}
