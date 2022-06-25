package hash;

import java.util.LinkedList;

public class HashElement {

	private LinkedList<HashObject> objects;
	
	public HashElement() {
		objects = new LinkedList<HashObject>();
	}
	
	public LinkedList<HashObject> getObs() {
		return objects;
	}
}
