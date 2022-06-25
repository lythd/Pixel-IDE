package tree;

import java.util.EventObject;

public class TreeEvent extends EventObject{

private static final long serialVersionUID = 1L;
	
	private String type;
	private Object data;
	
	public TreeEvent(Object source, Object data, String type) {
		super(source);
		this.data = data;
		this.type = type;
	}

	public Object getData() {
		return data;
	}

	public String getType() {
		return type;
	}	
}
