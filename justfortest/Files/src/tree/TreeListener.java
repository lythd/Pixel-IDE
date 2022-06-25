package tree;

import java.util.EventListener;

import tree.TreeEvent;

public interface TreeListener extends EventListener {
	public void treeEventOccured(TreeEvent event);
}
