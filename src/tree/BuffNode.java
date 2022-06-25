package tree;

import java.io.File;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

public class BuffNode {
	
//	Variables
	
	private File file;
	String type;
	private String data;
	private DefaultMutableTreeNode node;
	private BuffNode parent;
	private ArrayList <BuffNode> children;
	
//	Constructors
	
	public BuffNode() {
		this.file = null;
		this.type = "FOLDER";
		this.data = null;
		this.node = null;
		children = new ArrayList <BuffNode>();
		this.parent = this;
	}
	
	public BuffNode(DefaultMutableTreeNode node) {
		this.file = null;
		this.type = "FOLDER";
		this.data = null;
		this.node = node;
		children = new ArrayList <BuffNode>();
		this.parent = this;
	}
	
	public BuffNode(String type) {
		this.file = null;
		this.type = type;
		this.data = null;
		this.node = null;
		children = new ArrayList <BuffNode>();
		this.parent = this;
	}
	
	public BuffNode(String type, DefaultMutableTreeNode node) {
		this.file = null;
		this.type = type;
		this.data = null;
		this.node = node;
		children = new ArrayList <BuffNode>();
		this.parent = this;
	}
	
	public BuffNode(File file, String type, DefaultMutableTreeNode node) {
		this.file = file;
		this.type = type;
		this.data = null;
		this.node = node;
		children = new ArrayList <BuffNode>();
		this.parent = this;
	}
	
	public BuffNode(File file, String type, String data, DefaultMutableTreeNode node) {
		this.file = file;
		this.type = type;
		this.data = data;
		this.node = node;
		children = new ArrayList <BuffNode>();
		this.parent = this;
	}
	
	public BuffNode(File file, String type, String data, DefaultMutableTreeNode node, BuffNode parent) {
		this.file = file;
		this.type = type;
		this.data = data;
		this.node = node;
		children = new ArrayList <BuffNode>();
		setParent(parent);
	}
	
//	Get Functions
	
	public File file() {
		return file;
	}
	
	public String name() {
		if(file != null) {
			if(file.getName().indexOf('.') != -1) return file.getName().substring(0,file.getName().indexOf('.'));
			else return file.getName();
		} else return "";
	}
	
	public String extension() {
		if(file != null && file.getName().indexOf('.') != -1) return file.getName().substring(file.getName().indexOf('.'));
		return "";
	}
	
	public String type() {
		return type;
	}
	
	public String data() {
		return data;
	}
	
	public String exportedData() {
		return data;
	}
	
	public DefaultMutableTreeNode node() {
		return node;
	}
	
	public String nodeName() {
		if(node != null && node.getUserObject() != null) return (String) node.getUserObject();
		else return "";
	}
	
	public BuffNode parent() {
		return parent;
	}
	
	public ArrayList <BuffNode> children() {
		return children;
	}
	
	public int childrenCount() {
		if(children != null) return children.size();
		else return 0;
	}
	
//	Set Functions
	
	public void setFile(File file) {
		this.file = file;
	}
	
	public void setFile(File file, String type, String data) {
		this.file = file;
		this.type = type;
		this.data = data;
	}
	
	public void setFile(File file, String type) {
		this.file = file;
		this.type = type;
	}
	
	public void setFile(String type, String data) {
		this.type = type;
		this.data = data;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public void exportData() {
		exportedData();
	}
	
	public void setNode(DefaultMutableTreeNode node) {
		this.node = node;
	}
	
	public void setParent(BuffNode parent) {
		if(this.parent != null) this.parent.removeChild(this);
		this.parent = parent;
		parent.children.add(this);
	}
	
	public void setChild(BuffNode child) {
		child.parent = this;
		for(BuffNode c : children) c.removeParent();
		children = new ArrayList<BuffNode>();
		children.add(child);
	}
	
	public void setParent(BuffNode parent, boolean patch) {
		if(this.parent != null) {
			this.parent.removeChild(this);
			if(patch) parent.patchChildren();
		}
		this.parent = parent;
		parent.children.add(this);
	}
	
//	Add Functions

	public void addFile(File file) {
		this.file = file;
	}
	
	public void addFile(File file, String type, String data) {
		this.file = file;
		this.type = type;
		this.data = data;
	}
	
	public void addFile(File file, String type) {
		this.file = file;
		this.type = type;
	}
	
	public void addFile(String type, String data) {
		this.type = type;
		this.data = data;
	}
	
	public void addType(String type) {
		this.type = type;
	}
	
	public void addData(String data) {
		this.data = data;
	}
	
	public void addNode(DefaultMutableTreeNode node) {
		this.node = node;
	}
	
	public void addParent(BuffNode parent) {
		if(this.parent != null) {
			this.parent.removeChild(this);
		}
		parent.add(this);
	}
	
	public void addParent(BuffNode parent, boolean patch) {
		if(this.parent != null) {
			this.parent.removeChild(this);
			if(patch) this.parent.patchChildren();
		}
		parent.add(this);
	}
	
	public void addChild(BuffNode child) {
		child.parent = this;
		children.add(child);
	}
	
	public void add(BuffNode child) {
		child.parent = this;
		children.add(child);
	}
	
//	Remove Functions
	
	public void removeFile() {
		file.delete();
		file = null;
		type = null;
		data = null;
	}
	
	public void removeFile(boolean mode) {
		file.delete();
		file = null;
		if(mode) {
			type = null;
			data = null;
		}
	}
	
	public void removeNode() {
		node = null;
	}
	
	public void removeParent() {
		if(parent != null) parent.removeChild(this);
		parent = null;
	}
	
	public void removeParent(boolean patch) {
		if(parent != null) {
			parent.removeChild(this);
			if(patch) parent.patchChildren();
		}
		parent = null;
	}
	
	public void removeChildren() {
		for (BuffNode c : children) c.removeParent();
		children = null;
	}
	
//	With Functions (add but returns this)

	public BuffNode withPatch() {
		patchChildren();
		return this;
	}

	public BuffNode withNullify() {
		nullify();
		return this;
	}
	
	public BuffNode withFile(File file) {
		addFile(file);
		return this;
	}
	
	public BuffNode withFile(File file, String type, String data) {
		addFile(file, type, data);
		return this;
	}
	
	public BuffNode withType(String type) {
		addType(type);
		return this;
	}
	
	public BuffNode withData(String data) {
		addData(data);
		return this;
	}
	
	public BuffNode withNode(DefaultMutableTreeNode node) {
		addNode(node);
		return this;
	}
	
	public BuffNode withParent(BuffNode parent) {
		addParent(parent, true);
		return this;
	}
	
	public BuffNode withChild(BuffNode child) {
		addChild(child);
		return this;
	}
	
	public BuffNode with(BuffNode child) {
		add(child);
		return this;
	}
	
//	Without Functions (remove but returns this)
	
	public BuffNode withoutFile() {
		removeFile();
		return this;
	}
	
	public BuffNode withoutNode() {
		removeNode();
		return this;
	}
	
	public BuffNode withoutParent() {
		removeParent();
		return this;
	}
	
	public BuffNode withoutParent(boolean patch) {
		removeParent(patch);
		return this;
	}
	
	public BuffNode withoutChildren() {
		removeChildren();
		return this;
	}	
	
	public BuffNode withoutChild(BuffNode child) {
		removeChild(child);
		return this;
	}	
//	Child Get Functions
	
	public int index(BuffNode child) {
		for(int i = 0; i < childrenCount(); i++) if(children.get(i) == child) return i;
		return -1;
	}
	
	public BuffNode child(int index) {
		return children.get(index);
	}
	
	public BuffNode child(String name) {
		for(BuffNode c : children) if (c.name() == name) return c;
		return null;
	}
	
	public BuffNode childNode(String name) {
		for(BuffNode c : children) if (c.nodeName() == name) return c;
		return null;
	}
	
	public BuffNode child(String name, boolean mode) {
		if(mode) for(BuffNode c : children) {
			if (c.name() == name) return c;
			else if(c.childrenCount() > 0) return c.child(name, true);
		}
		else for(BuffNode c : children) if (c.name() == name) return c;
		return null;
	}
	
	public BuffNode childNode(String name, boolean mode) {
		BuffNode[] cResults = new BuffNode[children.size()];
		int cChecked = 0;
		if(mode) for(BuffNode c : children) {
			if (c.nodeName() == name) return c;
			else if(c.childrenCount() > 0) {
				cResults[cChecked] = c.childNode(name, true);
				cChecked++;
			}
		}
		if(cChecked > 0) {
			for(BuffNode r : cResults) if(r != null) return r;
		}
		else for(BuffNode c : children) if (c.nodeName() == name) return c;
		return null;
	}
	
	public BuffNode child(String name, int gen) {
		if(gen > 0) for(BuffNode c : children) {
			if (c.name() == name) return c;
			else if(c.childrenCount() > 0 && c.child(name, gen - 1) != null) return c.child(name, gen - 1);
		}
		else for(BuffNode c : children) if (c.name() == name) return c;
		return null;
	}
	
	public BuffNode childNode(String name, int gen) {
		if(gen > 0) for(BuffNode c : children) {
			if (c.nodeName() == name) return c;
			else if(c.childrenCount() > 0 && c.childNode(name, gen - 1) != null) return c.childNode(name, gen - 1);
		}
		else for(BuffNode c : children) if (c.nodeName() == name) return c;
		return null;
	}
	
//	Child Remove Functions
	
	public void removeChild(int index) {
		children.set(index, null);
	}
	
	public void removeChild(String name) {
		for(BuffNode c : children) if (c.name() == name) c = null;
	}
	
	public void removeChild(BuffNode child) {
		for(BuffNode c : children) if (c == child) c = null;
	}
	
	public void removeChildNode(String name) {
		for(BuffNode c : children) if (c.nodeName() == name) c = null;
	}
	
	public void removeChild(String name, boolean mode) {
		if(mode) for(BuffNode c : children) {
			if (c.name() == name) c = null;
			else if(c.childrenCount() > 0) c.removeChild(c.child(name), true);
		}
		else for(BuffNode c : children) if (c.name() == name) c = null;
	}
	
	public void removeChild(BuffNode child, boolean mode) {
		if(mode) for(BuffNode c : children) {
			if (c == child) c = null;
			else if(c.childrenCount() > 0) c.removeChild(child, true);
		}
		else for(BuffNode c : children) if (c == child) c = null;
	}
	
	public void removeChildNode(String name, boolean mode) {
		if(mode) for(BuffNode c : children) {
			if (c.nodeName() == name) c = null;
			else if(c.childrenCount() > 0) c.removeChild(c.childNode(name), true);
		}
		else for(BuffNode c : children) if (c.nodeName() == name) c = null;
	}
	
	public void removeChild(String name, int gen) {
		if(gen > 0) for(BuffNode c : children) {
			if (c.name() == name) c = null;
			else if(c.childrenCount() > 0 && c.child(name, gen - 1) != null) c.removeChild(c.child(name), gen - 1);
		}
		else for(BuffNode c : children) if (c.name() == name) c = null;
	}
	
	public void removeChild(BuffNode child, int gen) {
		if(gen > 0) for(BuffNode c : children) {
			if (c == child) c = null;
			else if(c.childrenCount() > 0) c.removeChild(child, gen - 1);
		}
		else for(BuffNode c : children) if (c == child) c = null;
	}
	
	public void removeChildNode(String name, int gen) {
		if(gen > 0) for(BuffNode c : children) {
			if (c.nodeName() == name) c = null;
			else if(c.childrenCount() > 0 && c.childNode(name, gen - 1) != null) c.removeChild(c.childNode(name), gen - 1);
		}
		else for(BuffNode c : children) if (c.nodeName() == name) c = null;
	}
	
//	Modification functions
	
	public void patchChildren() {
		for(int i = 0; i < childrenCount(); i++) if(children.get(i) == null) {
			for(int j = i; j + 1 < childrenCount(); j++) children.set(j,children.get(j+1));
			children.set(children.size(), null);
		}
	}
	
	public void nullify() {
		this.children = null;
		this.data = null;
		this.file = null;
		this.node = null;
		this.parent = null;
		this.type = null;
	}	
}
