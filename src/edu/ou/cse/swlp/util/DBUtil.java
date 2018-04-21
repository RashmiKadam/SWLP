/**
 * 
 */
package edu.ou.cse.swlp.util;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import edu.ou.cse.swlp.constants.DBConstants;

/**
 * @author Rashmi Pethe
 * This class is utility class for DAO.
 */
public class DBUtil {
	private static MongoClient client;
	
	
	/**
	 * Method to create the Mongo client connection.
	 * @return the DBinstance.
	 */
	public static DB getConnection(){
		DB dbObject = null;
		try {
			MongoClientURI mongoClientURI = new MongoClientURI(DBConstants.MONGODB_URI);
			client = new MongoClient(mongoClientURI);
			
			if(client != null){
				if("true".equalsIgnoreCase(DBConstants.LOCAL_DB)){
					dbObject = client.getDB("medialibrary");
				} else {
					dbObject = client.getDB("resources-service-db");
				}
			}	
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return dbObject;
		
	}
	
	/**
	 * Close the Mongo client connection.
	 */
	public static void closeConnection(){
		client.close();
	}
}