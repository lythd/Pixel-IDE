package hash;

import java.util.LinkedList;

public class HashMap {

	private HashElement[] elements;
	private boolean autoSize = true;
	private int limit = 10, inc = 1;
	
	public HashMap() {
		elements = new HashElement[1];
	}
	
	public HashMap(int size) {
		elements = new HashElement[size];
	}
	
	public HashMap withAutoSize(boolean autoSize) {
		this.autoSize = autoSize;
		return this;
	}
	
	public HashMap withLimit(int limit) {
		this.limit = limit;
		return this;
	}
	
	public HashMap withInc(int inc) {
		this.inc = inc;
		return this;
	}
	
	public Object get(String key) {
		for(HashObject obj : elements[hashFunc(key)].getObs()) if(key == obj.getKey()) return obj.getContent();
		return null;
	}
	
	public LinkedList<HashObject> getObjects() {
		LinkedList<HashObject> obs = new LinkedList<HashObject>();
		for(HashElement e : elements) if(e != null && e.getObs() != null) for(HashObject o : e.getObs()) obs.add(o);
		return obs;
	}
	
	public HashElement[] getElements() {
		return elements;
	}
	
	public boolean exists(String key) {
		if(elements[hashFunc(key)] != null && elements[hashFunc(key)].getObs() != null) for(HashObject obj : elements[hashFunc(key)].getObs()) if(key == obj.getKey()) return true;
		return false;
	}
	
	public void modify(String key, Object content) {
		if(this.exists(key)) set(key, content);
		else add(key, content);
	}
	
	public void set(String key, Object content) {
		for(HashObject obj : elements[hashFunc(key)].getObs()) if(key == obj.getKey()) obj.setContent(content);
		update();
	}
	
	public void add(String key, Object content) {
		if(elements[hashFunc(key)] == null || elements[hashFunc(key)].getObs() == null) elements[hashFunc(key)] = new HashElement();
		elements[hashFunc(key)].getObs().add(new HashObject(key,content));
		update();
	}
	
	public void modify(HashObject obj) {
		if(this.exists(obj.getKey())) set(obj.getKey(), obj.getContent());
		else add(obj.getKey(), obj.getContent());
	}
	
	public void set(HashObject obj) {
		for(HashObject o : elements[hashFunc(obj.getKey())].getObs()) if(obj.getKey() == o.getKey()) o.setContent(obj.getContent());
		update();
	}
	
	public void add(HashObject obj) {
		if(elements[hashFunc(obj.getKey())] == null || elements[hashFunc(obj.getKey())].getObs() == null) elements[hashFunc(obj.getKey())] = new HashElement();
		elements[hashFunc(obj.getKey())].getObs().add(new HashObject(obj.getKey(),obj.getContent()));
		update();
	}
	
	public HashMap changeSize(int newSize) {
		HashMap newT = new HashMap(newSize).withAutoSize(autoSize).withLimit(limit).withInc(inc);
		for(HashObject o : getObjects()) newT.add(o);
		return newT;
	}
	
	public void set(HashMap newT) {
		this.elements = newT.elements;
		this.autoSize = newT.autoSize;
	}
	
	private void update() {
		if(shouldChange()) changeSize(elements.length+inc);
	}
	
	private boolean shouldChange() {
		if(autoSize) {
			for(HashElement e : elements) if(e != null && e.getObs() != null && e.getObs().size() > limit) return true;
		}
		return false;
	}
	
	private int hashFunc(String key) {
		int total = 0;
		for(char c : key.toCharArray()) total += c;
		return (total % elements.length);
	}
	
}
