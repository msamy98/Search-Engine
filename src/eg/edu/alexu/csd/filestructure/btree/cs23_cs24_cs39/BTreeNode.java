package eg.edu.alexu.csd.filestructure.btree.cs23_cs24_cs39;

import java.util.ArrayList;
import java.util.List;

import eg.edu.alexu.csd.filestructure.btree.IBTreeNode;

public class BTreeNode<K extends Comparable<K>, V> implements IBTreeNode<K, V> {

	private int numOfKeys;
	private boolean isLeaf;
	private List<K> keys = new ArrayList<K>();
	private List<V> values = new ArrayList<V>();
	private List<IBTreeNode<K, V>> children = new ArrayList<IBTreeNode<K, V>>();
	
	@Override
	public int getNumOfKeys() {
		int numOfKeys = this.numOfKeys;
		return numOfKeys;
	}

	@Override
	public void setNumOfKeys(int numOfKeys) {
		
		this.numOfKeys = numOfKeys;
	}

	@Override
	public boolean isLeaf() {
		boolean isLeaf = this.isLeaf;
		return isLeaf;
	}

	@Override
	public void setLeaf(boolean isLeaf) {
		
		this.isLeaf = isLeaf;

	}

	@Override
	public List<K> getKeys() {
		List<K> keys = this.keys;
		return keys;
	}

	@Override
	public void setKeys(List<K> keys) {
		this.keys = keys;

	}

	@Override
	public List<V> getValues() {
		List<V> values = this.values;
		return values;
	}

	@Override
	public void setValues(List<V> values) {
		this.values = values;
	}

	@Override
	public List<IBTreeNode<K, V>> getChildren() {
		List<IBTreeNode<K, V>> children = this.children;
		return children;
	}

	@Override
	public void setChildren(List<IBTreeNode<K, V>> children) {
		this.children = children;

	}
	
	
	
	}