package net.brianscully.ds;



public class BinarySearchTree<E extends Comparable<E>> { // implements List<E> {

	private Node<E> root;
	
	private static class Node<E extends Comparable<E>> { 
		private final E value;
		private Option<Node<E>> left = Option.none();
		private Option<Node<E>> right = Option.none();
		private Option<Node<E>> parent = Option.none();

		private Node(E value) {
			this.value = value;
		}
		
		private Node(E value, Node<E> parent, Node<E> left, Node<E> right) {
			this.value = value;
			this.parent = Option.fromNullable(parent);
			this.left = Option.fromNullable(left);
			this.right = Option.fromNullable(right);		
		}
		
		public void insert(Node<E> newNode) {
			Option<Node<E>> maybeRoot = Option.some(this);
			
			while(maybeRoot.isSome()) {
				Node<E> root = maybeRoot.get();
				int compared = root.value.compareTo(newNode.value);
				if (compared < 0 && root.right.isEmpty()) {
					root.right = Option.some(newNode);
					newNode.parent = maybeRoot;
					break;
				} else if (compared < 0) {
					maybeRoot = root.right;
				} else if (compared > 0 && root.left.isEmpty()) {
					root.left = Option.some(newNode);
					newNode.parent = maybeRoot;
					break;
				} else if (compared > 0) {
					maybeRoot = root.left;
				} else {
					throw new IllegalArgumentException("Node value "+newNode.value+" already exists in tree");
				}
			}
		}
		
		public Option<Node<E>> search(E value) {
			Option<Node<E>> maybeRoot = Option.some(this);
			
			while(maybeRoot.isSome()) {
				Node<E> root = maybeRoot.get();
				int compared = root.value.compareTo(value);
				if (compared == 0) {
					return maybeRoot;
				} else if (compared < 0) {
					maybeRoot = root.right;
				} else if (compared > 0) {
					maybeRoot = root.left;
				}
			}
			return Option.none();
		}
		
		private void replace(Node<E> node, Node<E> with) {
			if (left.isSome() && left.get() == node)
				left = Option.some(with);
			else if (right.isSome() && right.get() == node)
				right = Option.some(with);
		}
		
		public Node<E> remove(Node<E> rootNode, E value) {
			
			Option<Node<E>> maybeFound = search(value);
			if (maybeFound.isEmpty()) return rootNode;
			
			Node<E> found = maybeFound.get();
			if (found.left.isEmpty() || found.right.isEmpty()) {
				Node<E> child;
				if (found.left.isEmpty())
					child = found.right.get();
				else
					child = found.left.get();
				
				if (found.parent.isEmpty()) {
					return child;
				} else {
					int compared = found.value.compareTo(found.parent.get().value);
					if (compared > 0)
						found.parent.get().right = Option.some(child);
					else
						found.parent.get().left = Option.some(child);
				}
			} else {
				
			    Option<Node<E>> maybeNodeToMove = smallest(found.right);
			    
			    Node<E> nodeToMove = maybeNodeToMove.get();
			    
			    found.parent.get().replace(found, nodeToMove);
			    
			    remove(nodeToMove, nodeToMove.value);
			    if (parent == null)
			        found.right = Option.some(nodeToMove);
			    else
			        parent.get().left = Option.some(nodeToMove);
			}
			return rootNode;
		}
		
		private Option<Node<E>> smallest(Option<Node<E>> maybeSubtree) {
		    if (maybeSubtree.isNone())
		    {
		        return Option.none();
		    }
		    
		    Node<E> subtree = maybeSubtree.get();
		 
		    if (subtree.left.isNone())
		    {
		        return Option.some(subtree);
		    }
		    
		    Node<E> subtreeLeft = subtree.left.get();
		 
		    if (subtreeLeft.left.isNone())
		    {
		        return subtree.left;
		    }
		 
		    return smallest(subtree.left);
		}

		public int size() {
			return nodeSize(left) + nodeSize(right) + 1;
		}

		private int nodeSize(Option<Node<E>> node) {
			return node.isEmpty() ? 0 : node.get().size();
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(left.toString()).append(" << ").append(value).append(" >> ").append(right.toString());
			return builder.toString();
		}
	}
	
	public void add(E element) {
		Node<E> newNode = new Node<E>(element);
		if (root == null) {
			root = newNode;
			return;
		}
		
		root.insert(newNode);		
	}
	
	public void remove(E element) {
		if (root == null) return;
		this.root = root.remove(root, element);
	}
	
	public boolean contains(E element) {
		return root.search(element).isSome();
	}
	
	public int size() {
		if (root == null) return 0;
		return root.size();
	}
	
	@Override
	public String toString() {
		return root != null ? root.toString() : "null" ;
	}
	
}
