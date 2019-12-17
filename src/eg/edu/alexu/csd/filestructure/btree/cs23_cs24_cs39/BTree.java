package eg.edu.alexu.csd.filestructure.btree.cs23_cs24_cs39;

import java.util.ArrayList;

import javax.management.RuntimeErrorException;

import eg.edu.alexu.csd.filestructure.btree.IBTree;
import eg.edu.alexu.csd.filestructure.btree.IBTreeNode;
import javafx.util.Pair;

public class BTree<K extends Comparable<K>, V> implements IBTree<K, V> {
	private int minimumDegree;
	private IBTreeNode<K, V> root;

	public BTree(int minimumDegree) {
		if (minimumDegree < 2) {
			throw new RuntimeErrorException(null);
		}
		this.minimumDegree = minimumDegree;
		this.root = new BTreeNode<K, V>();
		root.setNumOfKeys(0);
		root.setLeaf(true);
	}

	@Override
	public int getMinimumDegree() {

		int minimumDegree = this.minimumDegree;
		return minimumDegree;
	}

	@Override
	public IBTreeNode<K, V> getRoot() {
		if (this.root.getNumOfKeys() == 0) {
			return null;
		}
		IBTreeNode<K, V> root = this.root;
		return root;
	}

	@Override
	public void insert(K key, V value) {

		if (key == null || value == null) {
			throw new RuntimeErrorException(null);
		}

		if (root.getNumOfKeys() == (2 * minimumDegree - 1)) {
			IBTreeNode<K, V> s = new BTreeNode<K, V>();
			s.setLeaf(false);
			s.setNumOfKeys(0);
			setChildAtIndex(root, 0, s);
			root = s;
			splitChild(s, 0);
			insertNonFull(s, key, value);

		} else {
			insertNonFull(root, key, value);
		}

	}

	@Override
	public V search(K key) {
		if (key == null) {
			throw new RuntimeErrorException(null);
		}
		return searchInNode(root, key);
	}

	@Override
	public boolean delete(K key) {
		if (key == null) {
			throw new RuntimeErrorException(null);
		}

		if (this.getRoot() != null) {
			return delete_logic(this.root, key);
		}
		return false;
	}

	private void splitChild(IBTreeNode<K, V> toBeSplit, int index) {
		IBTreeNode<K, V> split2 = new BTreeNode<K, V>();
		IBTreeNode<K, V> split1 = (BTreeNode<K, V>) getChildAtIndex(index, toBeSplit);
		split2.setLeaf(split1.isLeaf());
		split2.setNumOfKeys(minimumDegree - 1);

		for (int i = 0; i < minimumDegree - 1; i++) {
			setKeyAtIndex(getKeyAtIndex(i + minimumDegree, split1), i, split2);
			setValueAtIndex(getValueAtIndex(i + minimumDegree, split1), i, split2);

		}

		if (!split1.isLeaf()) {
			for (int i = 0; i < minimumDegree; i++) {
				setChildAtIndex(getChildAtIndex(i + minimumDegree, split1), i, split2);

			}
			for (int i = 0; i < minimumDegree; i++) {
				split1.getChildren().remove(minimumDegree);
			}

		}

		for (int i = 0; i < minimumDegree - 1; i++) {
			split1.getKeys().remove(minimumDegree);
			split1.getValues().remove(minimumDegree);

		}

		setChildAtIndex(split2, index + 1, toBeSplit);

		setKeyAtIndex(getKeyAtIndex(minimumDegree - 1, split1), index, toBeSplit);
		setValueAtIndex(getValueAtIndex(minimumDegree - 1, split1), index, toBeSplit);

		split1.getKeys().remove(minimumDegree - 1);
		split1.getValues().remove(minimumDegree - 1);

		toBeSplit.setNumOfKeys(toBeSplit.getNumOfKeys() + 1);
		split1.setNumOfKeys(minimumDegree - 1);
	}

	private void insertNonFull(IBTreeNode<K, V> toBeInserted, K key, V value) {
		int index = toBeInserted.getNumOfKeys() - 1;

		if (toBeInserted.isLeaf()) {
			while (index >= 0 && key.compareTo(getKeyAtIndex(index, toBeInserted)) <= 0) {
				if (key.compareTo(getKeyAtIndex(index, toBeInserted)) == 0) {
					return;
				}
				index--;
			}
			setKeyAtIndex(key, index + 1, toBeInserted);
			setValueAtIndex(value, index + 1, toBeInserted);
			toBeInserted.setNumOfKeys(toBeInserted.getNumOfKeys() + 1);

		} else {
			while (index >= 0 && key.compareTo(getKeyAtIndex(index, toBeInserted)) <= 0) {
				if (key.compareTo(getKeyAtIndex(index, toBeInserted)) == 0) {
					return;
				}
				index--;
			}
			index++;
			if (getChildAtIndex(index, toBeInserted).getNumOfKeys() == (2 * minimumDegree - 1)) {
				splitChild(toBeInserted, index);
				if (key.compareTo(getKeyAtIndex(index, toBeInserted)) == 0) {
					return;
				}
				if (key.compareTo(getKeyAtIndex(index, toBeInserted)) > 0) {
					index++;
				}

			}
			insertNonFull((BTreeNode<K, V>) getChildAtIndex(index, toBeInserted), key, value);
		}
	}

	private V searchInNode(IBTreeNode<K, V> node, K key) {
		int index = 0;

		while (index < node.getNumOfKeys() && key.compareTo(getKeyAtIndex(index, node)) > 0) {
			index++;
		}

		if (index < node.getNumOfKeys() && key.compareTo((K) getKeyAtIndex(index, node)) == 0) {
			return getValueAtIndex(index, node);
		} else if (node.isLeaf())
			return null;
		else {
			return searchInNode((BTreeNode<K, V>) getChildAtIndex(index, node), key);
		}
	}

	private boolean delete_logic(IBTreeNode<K, V> node, K key) {
		////System.out.println("___________________________________________");

		if (node == null) {
			return false;
		}

		for (int counter = 0; counter < node.getNumOfKeys(); counter++) {
			////System.out.println("key" + counter);
			if (key.equals(this.getKeyAtIndex(counter, node))) {

				if (node.isLeaf()) {
					//System.out.println("case1");
					return delete_leaf(node, counter);
				} else {

					if (node.getChildren().get(counter).getNumOfKeys() > this.getMinimumDegree() - 1) {
						Pair<IBTreeNode<K, V>, Integer> pre = predecessor(node, counter);
						if (pre != null) {
							//System.out.println("case2.a");
							return after_pre(node, counter, pre);
						}
					}
					if (node.getChildren().size() > counter + 1
							&& node.getChildren().get(counter + 1).getNumOfKeys() > this.getMinimumDegree() - 1) {
						Pair<IBTreeNode<K, V>, Integer> suc = successor(node, counter);
						if (suc != null) {
							//System.out.println("case2.b");
							return after_pre(node, counter, suc);
						}
					}
					if (node.getChildren().size() > counter + 1
							&& node.getChildren().get(counter + 1).getNumOfKeys() == this.getMinimumDegree() - 1
							&& node.getChildren().get(counter).getNumOfKeys() == this.getMinimumDegree() - 1) {
						//System.out.println("case2.c");
						combine(node, counter);
						if (node == this.getRoot() && node.getNumOfKeys() == 1) {

							this.root = node.getChildren().get(0);
						}
						node.getKeys().remove(counter);
						node.getValues().remove(counter);
						node.getChildren().remove(counter + 1);
						node.setNumOfKeys(node.getNumOfKeys() - 1);
						/**
						 * for (int i = 0; i < node.getChildren().get(counter).getNumOfKeys(); i++) { if
						 * (key.compareTo(node.getChildren().get(counter).getKeys().get(i)) == 0) {
						 * node.getChildren().get(counter).getKeys().remove(i);
						 * node.getChildren().get(counter).getValues().remove(i);
						 * node.getChildren().get(counter)
						 * .setNumOfKeys(node.getChildren().get(counter).getNumOfKeys() - 1); } }
						 */
						return delete_logic(node.getChildren().get(counter), key);
					}
					

				}

			} else if (key.compareTo(this.getKeyAtIndex(counter, node)) < 0) {
				if (node.isLeaf()) {
					//System.out.println("here1");
					return false;
				}
				if (node.getChildren().get(counter).getNumOfKeys() == this.getMinimumDegree() - 1) {
					if (node.getChildren().get(counter + 1).getNumOfKeys() > this.getMinimumDegree() - 1) {
						//System.out.println("case3.a1");
						swap(node, counter, false);
					} else if (counter != 0
							&& node.getChildren().get(counter - 1).getNumOfKeys() > this.getMinimumDegree() - 1) {
						//System.out.println("case3.a2");
						swap(node, counter - 1, true);
					} else {
						//System.out.println("case3.a3");
						combine(node, counter);
						if (node == this.getRoot() && node.getNumOfKeys() == 1) {
							this.root = node.getChildren().get(0);
						}
						node.getKeys().remove(counter);
						node.getValues().remove(counter);
						node.getChildren().remove(counter + 1);
						node.setNumOfKeys(node.getNumOfKeys() - 1);
					}
				}
				return delete_logic(node.getChildren().get(counter), key);

			} else if (counter == node.getNumOfKeys() - 1 && key.compareTo(this.getKeyAtIndex(counter, node)) > 0) {
				if (node.isLeaf()) {
					//System.out.println("here2");
					return false;
				}
				if (node.getChildren().get(node.getChildren().size() - 1).getNumOfKeys() == this.getMinimumDegree()
						- 1) {
					if (node.getChildren().get(counter).getNumOfKeys() > this.getMinimumDegree() - 1) {
						swap(node, counter, true);
						//System.out.println("case3.b1");
					} else {
						//System.out.println("case3.b2");
						combine(node, counter);
						if (node == this.getRoot() && node.getNumOfKeys() == 1) {
							this.root = node.getChildren().get(0);
						}
						node.getKeys().remove(counter);
						node.getValues().remove(counter);
						node.getChildren().remove(counter + 1);
						node.setNumOfKeys(node.getNumOfKeys() - 1);
					}
				}
				return delete_logic(node.getChildren().get(node.getChildren().size() - 1), key);
			}
		}
		if (true) {
			//System.out.println("here3");
		}

		return true;

	}

	private boolean delete_leaf(IBTreeNode<K, V> node, int index) {
		node.getKeys().remove(index);
		node.getValues().remove(index);

		node.setNumOfKeys(node.getNumOfKeys() - 1);
		return true;
	}

	private void combine(IBTreeNode<K, V> parent, int index) {
		//System.out.println("comb");
		parent.getChildren().get(index).getKeys().add(parent.getKeys().get(index));
		parent.getChildren().get(index).getValues().add(parent.getValues().get(index));
		parent.getChildren().get(index).setNumOfKeys(parent.getChildren().get(index).getNumOfKeys() + 1);
		while (parent.getChildren().get(index + 1).getNumOfKeys() > 0) {
			parent.getChildren().get(index).getKeys().add(parent.getChildren().get(index + 1).getKeys().get(0));
			parent.getChildren().get(index).getValues().add(parent.getChildren().get(index + 1).getValues().get(0));
			if (!parent.getChildren().get(index).isLeaf()) {
				parent.getChildren().get(index).getChildren()
						.add(parent.getChildren().get(index + 1).getChildren().get(0));
			}
			parent.getChildren().get(index + 1).getKeys().remove(0);
			parent.getChildren().get(index + 1).getValues().remove(0);
			if (!parent.getChildren().get(index).isLeaf()) {
				parent.getChildren().get(index + 1).getChildren().remove(0);
			}
			parent.getChildren().get(index + 1).setNumOfKeys(parent.getChildren().get(index + 1).getNumOfKeys() - 1);
			parent.getChildren().get(index).setNumOfKeys(parent.getChildren().get(index).getNumOfKeys() + 1);
		}
		if (!parent.getChildren().get(index).isLeaf()) {
			parent.getChildren().get(index).getChildren().add(parent.getChildren().get(index + 1).getChildren().get(0));
		}
		if (!parent.getChildren().get(index).isLeaf()) {
			parent.getChildren().get(index + 1).getChildren().remove(0);
		}
		// parent.getKeys().remove(index);
		// parent.getValues().remove(index);
		// parent.getChildren().remove(index + 1);
		// return true;
	}

	private void swap(IBTreeNode<K, V> parent, int index, boolean flag) {
		// right ==> true / left ==> false
		if (flag) {
			//System.out.println("swap true");
			parent.getChildren().get(index + 1).getKeys().add(0, parent.getKeys().get(index));
			parent.getChildren().get(index + 1).getValues().add(0, parent.getValues().get(index));
			parent.getChildren().get(index + 1).setNumOfKeys(parent.getChildren().get(index + 1).getNumOfKeys() + 1);
			if (!parent.getChildren().get(index + 1).isLeaf()) {
				parent.getChildren().get(index + 1).getChildren().add(0, (parent.getChildren().get(index).getChildren()
						.get(parent.getChildren().get(index).getChildren().size() - 1)));
			}
			parent.getKeys().remove(index);
			parent.getValues().remove(index);
			parent.getKeys().add(index, parent.getChildren().get(index).getKeys()
					.get(parent.getChildren().get(index).getKeys().size() - 1));
			parent.getValues().add(index, parent.getChildren().get(index).getValues()
					.get(parent.getChildren().get(index).getValues().size() - 1));
			parent.getChildren().get(index).setNumOfKeys(parent.getChildren().get(index).getNumOfKeys() - 1);
			parent.getChildren().get(index).getKeys().remove(parent.getChildren().get(index).getKeys().size() - 1);
			parent.getChildren().get(index).getValues().remove(parent.getChildren().get(index).getValues().size() - 1);
			if (!parent.getChildren().get(index + 1).isLeaf()) {
				parent.getChildren().get(index).getChildren()
						.remove(parent.getChildren().get(index).getChildren().size() - 1);
			}

		} else {
			//System.out.println("swap false");
			parent.getChildren().get(index).getKeys().add(parent.getKeys().get(index));
			parent.getChildren().get(index).getValues().add(parent.getValues().get(index));
			parent.getChildren().get(index).setNumOfKeys(parent.getChildren().get(index).getNumOfKeys() + 1);
			if (!parent.getChildren().get(index).isLeaf()) {
				parent.getChildren().get(index).getChildren()
						.add((parent.getChildren().get(index + 1).getChildren().get(0)));
			}
			parent.getKeys().remove(index);
			parent.getValues().remove(index);
			parent.getKeys().add(index, parent.getChildren().get(index + 1).getKeys().get(0));
			parent.getValues().add(index, parent.getChildren().get(index + 1).getValues().get(0));
			parent.getChildren().get(index + 1).setNumOfKeys(parent.getChildren().get(index + 1).getNumOfKeys() - 1);
			parent.getChildren().get(index + 1).getKeys().remove(0);
			parent.getChildren().get(index + 1).getValues().remove(0);
			if (!parent.getChildren().get(index).isLeaf()) {
				parent.getChildren().get(index + 1).getChildren().remove(0);
			}

		}
	}

	private Pair<IBTreeNode<K, V>, Integer> predecessor(IBTreeNode<K, V> nodeToBeDeleted, int index) {

		//System.out.println("pre");
		int predecessorIndex = 0;
		Pair<IBTreeNode<K, V>, Integer> pair = null;
		IBTreeNode<K, V> child = getChildAtIndex(index, nodeToBeDeleted);

		predecessorIndex = child.getNumOfKeys() - 1;

		if (!child.isLeaf()) {
			pair = predecessor(child, predecessorIndex + 1);
		}

		if (child.isLeaf()) {
			pair = new Pair<IBTreeNode<K, V>, Integer>(child, predecessorIndex);

		}
		return pair;
	}

	boolean after_pre(IBTreeNode<K, V> node, int counter, Pair<IBTreeNode<K, V>, Integer> pr) {
		//System.out.println("after pre");
		K temp = pr.getKey().getKeys().get(pr.getValue());
		V value = pr.getKey().getValues().get(pr.getValue());

		delete_logic(node, pr.getKey().getKeys().get(pr.getValue()));

		node.getKeys().set(counter, temp);
		node.getValues().set(counter, value);

		return true;
	}

	private Pair<IBTreeNode<K, V>, Integer> successor(IBTreeNode<K, V> nodeToBeDeleted, int index) {
		//System.out.println("suc");
		int successorIndexdex = -1;
		IBTreeNode<K, V> child = getChildAtIndex(index + 1, nodeToBeDeleted);
		Pair<IBTreeNode<K, V>, Integer> pair = null;

		if (!child.isLeaf()) {
			pair = successor(child, successorIndexdex);
		}

		if (child.isLeaf()) {
			pair = new Pair<IBTreeNode<K, V>, Integer>(child, successorIndexdex + 1);
		}
		return pair;
	}

	private void setChildAtIndex(IBTreeNode<K, V> child, int index, IBTreeNode<K, V> node) {
		ArrayList<IBTreeNode<K, V>> list = (ArrayList<IBTreeNode<K, V>>) node.getChildren();
		list.add(index, child);

	}

	private IBTreeNode<K, V> getChildAtIndex(int index, IBTreeNode<K, V> node) {
		ArrayList<IBTreeNode<K, V>> list = (ArrayList<IBTreeNode<K, V>>) node.getChildren();
		return list.get(index);
	}

	private void setKeyAtIndex(K key, int index, IBTreeNode<K, V> node) {
		ArrayList<K> keys = (ArrayList<K>) node.getKeys();
		keys.add(index, key);
	}

	private K getKeyAtIndex(int index, IBTreeNode<K, V> node) {
		ArrayList<K> keys = (ArrayList<K>) node.getKeys();
		return keys.get(index);
	}

	private void setValueAtIndex(V value, int index, IBTreeNode<K, V> node) {
		ArrayList<V> values = (ArrayList<V>) node.getValues();
		values.add(index, value);
	}

	private V getValueAtIndex(int index, IBTreeNode<K, V> node) {
		ArrayList<V> values = (ArrayList<V>) node.getValues();

		return values.get(index);
	}

}