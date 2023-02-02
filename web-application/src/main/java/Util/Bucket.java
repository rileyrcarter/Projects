package Util;

public class Bucket {
	
	private int bucketId;
	private String username;
	private String bucketName;
	private int likes;
	
	public Bucket(int bucketId, String bucketName, int likes, String username) {
		this.bucketId = bucketId;
		this.bucketName = bucketName;
		this.likes = likes;
		this.username = username;
	}
	
	public Bucket(int bucketId, String bucketName, int likes) {
		this.bucketId = bucketId;
		this.bucketName = bucketName;
		this.likes = likes;
	}
	
	public Bucket() {
		this(-1, "", 0, "");
	}

	public int getBucketId() {
		return bucketId;
	}

	public String getBucketName() {
		return bucketName;
	}
	
	public int getLikes() {
		return likes;
	}
	
	public String getUsername() {
		return username;
	}
	
	
}
