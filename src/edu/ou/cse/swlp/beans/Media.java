package edu.ou.cse.swlp.beans;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Rashmi Pethe
 * This class is a simple POJO of media type.
 */
@XmlRootElement
public class Media {

	private String title;
	private String description;
	private String mediaType;
	private String resourceURL;
	private String ID;
	private String image;
	private String ISBN;
	private String downloadLink;
	private String tags;
	private Double similarityValue;
	private String errorMsg;
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getISBN() {
		return ISBN;
	}
	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}
	public String getDownloadLink() {
		return downloadLink;
	}
	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMediaType() {
		return mediaType;
	}
	public void setMediaType(String mediType) {
		this.mediaType = mediType;
	}
	public String getResourceURL() {
		return resourceURL;
	}
	public void setResourceURL(String resourceURL) {
		this.resourceURL = resourceURL;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public Double getSimilarityValue() {
		return similarityValue;
	}
	public void setSimilarityValue(Double similarityValue) {
		this.similarityValue = similarityValue;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}