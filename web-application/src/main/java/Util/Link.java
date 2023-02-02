package Util;

public class Link {
	private int linkId;
	private String link;
	private String linkName;
	private String description;
	
	public Link(int linkId, String link, String linkName, String description) {
		this.linkId = linkId;
		this.link = link;
		this.linkName = linkName;
		this.description = description;
	}

	public int getLinkId() {
		return linkId;
	}

	public String getLink() {
		return link;
	}

	public String getDescription() {
		return description;
	}
	
	public String getLinkName() {
		return linkName;
	}
	
	public String getClickLink() {
		if (!Constants.urlPattern.matcher(link).matches()) {
			return "http://" + link;
		}
		return link;
	}
	
	
}
