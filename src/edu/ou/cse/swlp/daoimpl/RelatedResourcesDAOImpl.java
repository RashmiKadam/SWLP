package edu.ou.cse.swlp.daoimpl;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import edu.ou.cse.swlp.beans.Media;
import edu.ou.cse.swlp.constants.DBConstants;
import edu.ou.cse.swlp.dao.RelatedResourcesDAO;
import edu.ou.cse.swlp.util.DBUtil;

/**
 * This class makes database access to handle the related resources from MongoDB.
 * @author Rashmi Pethe
 *
 */
public class RelatedResourcesDAOImpl implements RelatedResourcesDAO{

	/* (non-Javadoc)
	 * @see edu.ou.cse.swlp.dao.RelatedResourcesDAO#fetchResources()
	 * This method fetches the resources corresponding to tags from database.
	 */
	@Override
	public ArrayList<Media> fetchResources(String tags) {
		ArrayList<Media> mediaList = new ArrayList<Media>();
		Media media = null;
		String mediaType = null;
		
		DBCollection collection = DBUtil.getConnection().getCollection(DBConstants.COLLECTION_NAME);
		BasicDBObject  query = new BasicDBObject ();
		query.put(DBConstants.TAGS, tags);
		List<String> typeList = new ArrayList<String>();
		typeList.add(DBConstants.VIDEO_TYPE);
		typeList.add(DBConstants.TEXT_TYPE);
		query.put(DBConstants.MEDIA_TYPE, new BasicDBObject("$in", typeList));
		
		DBCursor cursor = collection.find(query).limit(Integer.parseInt(DBConstants.RESULTS_LIMIT));
		while(cursor.hasNext()){
			DBObject obj = cursor.next();
			media = new Media();
			media.setID(obj.get(DBConstants.MEDIA_ID).toString());
			media.setTags(obj.get(DBConstants.TAGS).toString());
			media.setTitle(obj.get(DBConstants.TITLE).toString());
			media.setDescription(obj.get(DBConstants.DESCRIPTION).toString());
			mediaType = obj.get(DBConstants.MEDIA_TYPE).toString();
			media.setMediaType(mediaType);
			if(DBConstants.VIDEO_TYPE.equalsIgnoreCase(mediaType)){
				media.setImage(obj.get(DBConstants.RESOURCE_URL).toString());
				media.setResourceURL(obj.get(DBConstants.RESOURCE_URL).toString());
			} else if(DBConstants.TEXT_TYPE.equalsIgnoreCase(mediaType)){
				media.setImage(obj.get(DBConstants.IMAGE).toString());
				media.setResourceURL(obj.get(DBConstants.IMAGE).toString());
			}
			mediaList.add(media);
		}
		System.out.println("No of media objects fetched--"+mediaList.size());
		DBUtil.closeConnection(); 
		return mediaList;
	}
	
	/* (non-Javadoc)
	 * @see edu.ou.cse.swlp.dao.RelatedResourcesDAO#insertResources(java.util.List)
	 * This method inserts the Media objects into databaseas JSON documents.
	 */
	public void insertResources(List jsonList){
		DBCollection collection = DBUtil.getConnection().getCollection(DBConstants.COLLECTION_NAME);
		DBObject dbObject = null;
		
		for (Object object : jsonList) {
			String jsonStr = (String) object;
			dbObject = (DBObject) JSON.parse(jsonStr);
			//If already json document not exists, then insert new one.
			collection.update(dbObject, dbObject, true, false);
			//System.out.println("Inserted");
		}
		DBUtil.closeConnection();
	}
}