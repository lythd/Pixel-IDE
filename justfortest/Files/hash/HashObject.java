package hash;

public class HashObject {

	private String key;
	private Object content;
	
	public HashObject(String key, Object content) {
		this.key = key;
		this.content = content;
	}
	
	public String getKey() {
		return key;
	}
	
	public Object getContent() {
		return content;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public void setContent(Object content) {
		this.content = content;
	}
}
