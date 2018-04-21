/**
 * 
 */
package edu.ou.cse.swlp.constants;

/**
 * @author Rashmi Pethe
 * This class contains all the constants specific to Database Layer.
 */
public class DBConstants {
	
	//limit on number of results fetched
	public static String RESULTS_LIMIT = "10";
	
	//Constant variables for document attributes
	public static String MEDIA_ID = "ID";
	public static String TAGS = "tags";
	public static String IMAGE = "image";
	public static String TITLE = "title";
	public static String DESCRIPTION = "description";
	public static String RESOURCE_URL = "resourceURL";
	public static String MEDIA_TYPE = "mediaType";
	public static String VIDEO_TYPE = "Video";
	public static String TEXT_TYPE = "Text";
	
	//Constant variable for database connection
	public static String COLLECTION_NAME = "media";
	public static String LOCAL_DB = "false";
	//public static String HOST_NAME = "localhost";
	//public static Integer PORT = 27017;
	//URI for MongoDB connection
	public static String MONGODB_URI ="mongodb://swlpresource:csi5510@ds121015.mlab.com:21015/resources-service-db";
}
