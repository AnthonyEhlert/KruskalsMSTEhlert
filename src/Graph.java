import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/*****************************************************************
 * Name				: KruskalsMSTEhlert
 * Author			: Tony Ehlert
 * Created			: Mar 7, 2023
 * Course			: CIS152 Data Structures
 * Version			: 1.0
 * OS				: Windows 11
 * Copyright		: This is my own original work based on
 *         	  	  	  specifications issued by our instructor
 * Description		: This program creates a graph and stores it as an adjacencyList and then
 * 					  calls a function that implements Kruskal's algorithm to find the MST of the
 * 					  graph and print it to the console
 *					 Input: Vertices labeled/named with a single letter and edges connecting 
 *							connecting the vertices with the weight of the edge
 *					 Output: The calculated MST using Kruskal's algorithm
 * Academic Honesty	: I attest that this is my original work.
 * I have not used unauthorized source code, either modified or 
 * unmodified. I have not given other fellow student(s) access to
 * my program.         
 *****************************************************************/
public class Graph {

	// ArrayList contains LinkedLists of the head node at the front of LinkedList
	ArrayList<LinkedList<Node>> nodeLists;

	/**
	 * Creates an empty Array
	 */
	Graph() {
		nodeLists = new ArrayList<>(); // empty ArrayList creation
	}

	/**
	 * This method creates a Node with a weight of 0, then creates an empty
	 * LinkedList with the newly created node being placed in the first position of
	 * the list
	 * 
	 * @param node - Node with a weight of zero
	 */
	public void addNode(Node node) {
		LinkedList<Node> currentList = new LinkedList<>();
		node.setParent(node);
		currentList.add(node);
		nodeLists.add(currentList);
		//node.setParent(node);
	}

	/**
	 * This method creates a node that gets added to the corresponding LinkedList
	 * for both the starting vertex and ending vertex
	 * 
	 * @param src    - Starting vertex
	 * @param dst    - ending vertex
	 * @param weight = weight of traversal path
	 */
	public void addEdge(char src, char dst, int weight) {
		Iterator<LinkedList<Node>> nodeListsIter = nodeLists.iterator();

		// get LinkedList from arrayList and store as current list
		// alist.get(src) grabs the linkedList contained in the ArrayList index of src
		// value
		LinkedList<Node> currentList = nodeListsIter.next();

		Node currentNode = currentList.getFirst();

		while (Character.toLowerCase(currentNode.data) != Character.toLowerCase(src)) {
			currentList = nodeListsIter.next();
			currentNode = currentList.getFirst();
		}

		// dstNode = address of node to link to = alist.get(dst).get(0)
		// .get(0) grabs the first node contained in LinkedList (aka head node)
		Node dstNode = new Node(dst, weight);
		
		// set parentNode of dstNode to currentNode
		dstNode.setParent(currentNode);

		// adds node to tail of current LinkedList with src value/index == head node
		currentList.add(dstNode);

		// reset currentList iterator
		nodeListsIter = nodeLists.iterator();

		// grab first node of current list
		currentNode = currentList.get(0);

		// while loop to find matching node.data for dst node
		while (Character.toLowerCase(currentNode.data) != Character.toLowerCase(dst)) {
			currentList = nodeListsIter.next();
			currentNode = currentList.get(0);
		}

		// adds node to tail of current LinkedList
		Node srcNode = new Node(src, weight);
		
		// set parentNode of srcNode top currentNode
		srcNode.setParent(currentNode);

		currentList.add(srcNode);

	}

	/**
	 * This method checks if an edge is present between two vertices
	 * 
	 * @param src - starting vertex
	 * @param dst - ending vertex
	 * @return
	 */
	public boolean checkEdge(char src, char dst) {
		
		if (nodeLists.size() > 0) {
			ListIterator<LinkedList<Node>> nodeListsIter = nodeLists.listIterator();

			// get LinkedList from arrayList and store as current list
			// alist.get(src) grabs the linkedList contained in the ArrayList index of src
			// value
			LinkedList<Node> currentList = nodeListsIter.next();

			Node currentNode = currentList.getFirst();

			while ((Character.toLowerCase(currentNode.data) != Character.toLowerCase(src)) && nodeListsIter.hasNext()) {
				currentList = nodeListsIter.next();
				currentNode = currentList.getFirst();
			}
	
			// iterate over current linkedList and check for a node that matches the
			// destination node data
			for (Node node : currentList) {
				if (node.data == dst) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This method prints the adjacencyList representing the graph
	 */
	public void print() {
		for (LinkedList<Node> currentList : nodeLists) {
			for (Node node : currentList) {
				System.out.print(node.data + "(" + node.weight + "), Parent: " + node.parent.data + " -> ");
			}
			System.out.println();
		}
	}

	/**
	 * This method compares the traversal weights of each node in the adjacentNodes
	 * list passed in and returns the index location of the node with smallest
	 * weight
	 * 
	 * @param adjNodes - current list of adjacent nodes
	 * @return - index of minimum traversal node weight from adjNodes list
	 */
	private int findMin(LinkedList<Node> adjNodes) {

		LinkedList<Node> currentNodeList = adjNodes;

		// set index of minimum to 0/ first element in list
		int minNodeIndex = 0;

		// create ListIterator
		ListIterator<Graph.Node> nodeListIter = currentNodeList.listIterator();

		// assign first node in list to minimumNode variable
		Node minNode = nodeListIter.next();

		// while loop to verify there is a node still in list to compare
		while (nodeListIter.hasNext()) {
			Node currentNode = nodeListIter.next();

			// compare minNode.weight to currentNode.weight and assign currentNode to
			// minNode if less and get current index value
			if (currentNode.weight < minNode.weight) {
				minNode = currentNode;
				minNodeIndex = nodeListIter.nextIndex() - 1;
			}
		}

		return minNodeIndex;
	}

	/**
	 * This method looks at the current a current node's adjacent nodes and one by
	 * one verifies that the adjacent nodes have not been visited or are not
	 * currently contained in the current adjacentNodes list. If no match in either
	 * list is found, then the current node is added to the adjacentNodes list.
	 * After all of the current node's adjacent nodes have been examined, the
	 * updated adjacentNodes list is returned.
	 * 
	 * @param nodeListToAdd - list of nodes to be added to adjNodes list (if
	 *                      possible)
	 * @param visited       - current list of visited nodes
	 * @param adjNodeList   - current list of adjacent nodes
	 * @return - updated list with all current adjacent nodes that have not been
	 *         visited
	 */
	private LinkedList<Node> addNodes(LinkedList<Node> nodeListToAdd, LinkedList<Node> visited,
			LinkedList<Node> adjNodeList) {

		// list of current unvisited adjacent nodes
		LinkedList<Node> adjNodes = adjNodeList;

		// list of current nodes to be added if possible
		LinkedList<Node> nodesToAdd = nodeListToAdd;

		// list of visited nodes
		LinkedList<Node> visitedNodes = visited;

		if (nodesToAdd.size() > 1) {
			for (int i = 1; i < nodesToAdd.size(); i++) {
				// grab next node from nodesToAdd list and assign to curNode variable
				Node curNode = nodesToAdd.get(i);

				// create boolean found variable to track if a match was found
				boolean found = false;

				// iterate through current visited list and check the data of nodes for match
				// and break if found.  (used toUpperCase for additional input testing)
				for (int j = 0; j < visitedNodes.size(); j++) {
					if (Character.toUpperCase(curNode.data) == Character.toUpperCase(visitedNodes.get(j).data)) {
						found = true;
						break;
					}
				}

				// iterate through current adjNodes list and check the nodes for match and break
				// if found
				for (int j = 0; j < adjNodes.size(); j++) {
					if (curNode.data == adjNodes.get(j).data && curNode.weight == adjNodes.get(j).weight) {
						found = true;
						break;
					}
				}

				if (!found) {
					// add node to adjNodes list if found is still false
					adjNodes.add(curNode);
				}

			}
		}

		return adjNodeList;
	}

	/**
	 * This method implements Prim's MST algorithm to calculate the
	 * minumum spanning tree of a graph
	 * 
	 * @param node - starting node for Prim's MST
	 * @return - integer value representing the calculated MST
	 */
	public int primMST(char node) {

		// create listIterator for arrayList
		ListIterator<LinkedList<Node>> nodeListsIter = nodeLists.listIterator();

		// creation of variable to store/track calculated MST
		int calcMST = 0;

		// create an empty list to store visited nodes
		LinkedList<Node> visited = new LinkedList<>();

		// create an empty list for nodes adjacent nodes
		LinkedList<Node> adjacentNodes = new LinkedList<>();

		// get first node list from ArrayList
		LinkedList<Node> currentNodeList = nodeListsIter.next();

		// assign curNode to first node of first list
		Node curNode = currentNodeList.getFirst();

		// find initial node LinkedList
		while (Character.toLowerCase(curNode.data) != Character.toLowerCase(node)) {
			currentNodeList = nodeListsIter.next();
			curNode = currentNodeList.getFirst();
		}

		// add first node to visited list
		visited.add(curNode);

		while (visited.size() < nodeLists.size()) {

			// PRINT OUT OF VISITED NODES FOR DEBUGGING
//			System.out.println("");
//			System.out.print("Visited Node List: ");
//			for (int i = 0; i < visited.size(); i++) {
//				System.out.print(visited.get(i).data + " " + visited.get(i).weight + ", ");
//			}

			// add nodes in curNode LinkedList to adjacentNodesList
			adjacentNodes = addNodes(currentNodeList, visited, adjacentNodes);

			// PRINT OUT OF ADJACENT NODES FOR DEBUGGING
//			System.out.println("");
//			System.out.print("AdjNode List: ");
//			for (int i = 0; i < adjacentNodes.size(); i++) {
//				System.out.print(adjacentNodes.get(i).data + " " + adjacentNodes.get(i).weight + ", ");
//			}

			// PRINT OUT OF MINIMUM NODE FOR DEBUGGING
//			System.out.println("");
//			System.out.println("MinimumNode: " + adjacentNodes.get(findMin(adjacentNodes)).data + " "
//					+ adjacentNodes.get(findMin(adjacentNodes)).weight);

			// add weight to MST value
			calcMST += adjacentNodes.get(findMin(adjacentNodes)).weight;

			// add to visited list
			visited.add(adjacentNodes.get(findMin(adjacentNodes)));

			// update currentNodeList to last visited node
			nodeListsIter = nodeLists.listIterator();
			Node nextNode = visited.getLast();
			currentNodeList = nodeListsIter.next();
			curNode = currentNodeList.getFirst();

			while (Character.toLowerCase(curNode.data) != Character.toLowerCase(nextNode.data)) {
				currentNodeList = nodeListsIter.next();
				curNode = currentNodeList.getFirst();
			}

			// remove minimum path node from adjacent list
			adjacentNodes.remove(findMin(adjacentNodes));

		}

		return calcMST;
	}

	// START OF KRUSKAL'S MST METHODS/ALGORITHM
	
	
	private LinkedList<Node> getEdgeList() {
		
		// create an empty list to hold added srcNodes (node with weight of 0)
		LinkedList<Node> addedNodeLists = new LinkedList<Node>();

		// create an empty list to hold edges
		LinkedList<Node> edgeList = new LinkedList<Node>();

		// create listIterator for arrayList
		ListIterator<LinkedList<Node>> nodeListsIter = nodeLists.listIterator();

		// System.out.println("nodeLists size: " + nodeLists.size());

		// add edges to edgeList
		while (nodeListsIter.hasNext()) {
			// assign first nodeList to currentList variable
			LinkedList<Node> currentList = nodeListsIter.next();

			// assign first node of current list to srcNode variable
			Node srcNode = currentList.getFirst();

			// create nodeList iterator
			ListIterator<Graph.Node> currentListIter = currentList.listIterator();

			// create boolean variables to track matching data in lists
			boolean foundEdgeList = false;
			boolean foundAddedNodeLists = false;

			if (currentList.size() > 1) {

				// set dstNode to second element in list
				Node dstNode = currentListIter.next();

				// check if srcNode.data is contained in edgeList
				if (!edgeList.isEmpty()) {
					for (int i = 0; i < edgeList.size(); i++) {
						if (Character.toUpperCase(srcNode.data) == Character.toUpperCase(edgeList.get(i).data)) {
							foundEdgeList = true;
							break;
						}
					}
				}

				// while loop to iterate through current list
				while (currentListIter.hasNext()) {
					dstNode = currentListIter.next();
					foundAddedNodeLists = false;
					for (int i = 0; i < addedNodeLists.size(); i++) {
						if (Character.toUpperCase(dstNode.data) == Character.toUpperCase(addedNodeLists.get(i).data)) {
							foundAddedNodeLists = true;
							break;
						}
					}

					// if both boolean found variables are false add to edge list
					if (!foundEdgeList || !foundAddedNodeLists) {
						edgeList.add(dstNode);
					}
				}
			}

			// add srcNode (weight = 0) to addedNodeLists list before starting next list
			// loop
			addedNodeLists.add(srcNode);
		}
//		// print out of edgeList & addNodeLists for debugging
//		System.out.print("edgeList: ");
//		for (int i = 0; i < edgeList.size(); i++) {
//			System.out.print(edgeList.get(i).data + "(" + edgeList.get(i).weight + "), ");
//		}
//		System.out.println();
//		System.out.print("addedNodeLists: ");
//		for (int i = 0; i < addedNodeLists.size(); i++) {
//			System.out.print(addedNodeLists.get(i).data + "(" + addedNodeLists.get(i).weight + "), ");
//		}
		
		return edgeList;
	}
	
	public int kruskalMST() {

		// create calcMST variable to return
		int calcMST = 0;
		
		// creation of edgeCounter variable to keep track of number of edges added
		int edgeCounter = 0;
		
		// get list of edges using private class method getEdgeList()
		LinkedList<Node> edgeList = getEdgeList();
		
		// create new array list of linkedList nodes for unionCycle checks
		ArrayList<LinkedList<Node>> unionNodeLists = new ArrayList<>();
		
		// add source nodes to list (nodes with weight 0)
		for (int i = 0; i < nodeLists.size(); i++) {
			LinkedList<Node> currentList = new LinkedList<>();
			currentList.add(nodeLists.get(i).getFirst());
			unionNodeLists.add(currentList);
		}
		
		//System.out.println("nodeLists size: " + nodeLists.size());
		while ((edgeCounter < nodeLists.size()-1) && (edgeList.size() > 0)) {
			
			System.out.println();
			System.out.print("edgeList: ");
			for (int i = 0; i < edgeList.size(); i++) {
				System.out.print(edgeList.get(i).data + "(" + edgeList.get(i).weight + ")" + edgeList.get(i).parent.data
						+ edgeList.get(i).parent.weight + ", ");
			}
			
			int min = findMin(edgeList);
			Node minEdge = edgeList.get(min);
			
			// create variable for dstNode parent
			Node dstParent = minEdge.parent;
			
			// creation of boolean variable for cycle being found
			boolean cycleFound = false;
			
			// find unionNodeList for minEdgeParent
			int unionNodeListIndex = 0;
			for (int i = 0; i < unionNodeLists.size(); i++) {
				if (unionNodeLists.get(i).getFirst().data == minEdge.parent.data) {
					unionNodeListIndex = i;
					break;
				}
			}
			
			// check if dstNode.data in any unionNodeLists  node list
			boolean dstNodeFound = false;
			int dstNodeIndex = 0;
			
			for (int i = 0; i < unionNodeLists.size(); i ++) {
				for (int j = 1; j < unionNodeLists.get(i).size(); j++) {
					if (minEdge.data == unionNodeLists.get(i).get(j).data) {
						dstNodeFound = true;
						dstNodeIndex = i;
						// check to ensure minEdge.parent.data does not equal data of set parent node
						if (unionNodeLists.get(i).getFirst().data == minEdge.parent.data) {
							cycleFound = true;
						}
						// check if dstNode.parent.data exists in list
						for(int k = 1; k < unionNodeLists.get(i).size(); k++) {
							if (minEdge.parent.data == unionNodeLists.get(i).get(k).data) {
								cycleFound = true;
								break;
							}
						}
					}
					if (cycleFound) {
						break;
					}
				}
				if (cycleFound) {
					break;
				}
			}
			
			// check is dstParent.data is in any unionNodeLists node list
			boolean dstParentFound = false;
			int dstParentIndex = 0;
			
			for (int i = 0; i < unionNodeLists.size(); i ++) {
				for (int j = 1; j < unionNodeLists.get(i).size(); j++) {
					if (dstParent.data == unionNodeLists.get(i).get(j).data) {
						dstParentFound = true;
						dstParentIndex = i;
						break;
					}
				}
				if (dstParentFound) {
					break;
				}
			}
			
			if (cycleFound) {
				System.out.println(minEdge.data + "(" + minEdge.weight + ")" + minEdge.parent.data + " EDGE SKIPPED");
			}else if (!dstNodeFound && dstParentFound) {
				unionNodeLists.get(dstParentIndex).add(minEdge);
				calcMST += minEdge.weight;
				edgeCounter++;
				System.out.println();
				System.out.println(minEdge.data + "(" + minEdge.weight + ")" + minEdge.parent.data + " Edge added:");
			} else if (dstNodeFound && dstParentFound) {
				unionNodeLists.get(dstNodeIndex).addAll(unionNodeLists.get(dstParentIndex));
				calcMST += minEdge.weight;
				edgeCounter++;
				System.out.println();
				System.out.println(minEdge.data + "(" + minEdge.weight + ")" + minEdge.parent.data + " Edge added:");
			} else if (!dstNodeFound && !dstParentFound) {
				unionNodeLists.get(unionNodeListIndex).add(minEdge);
				calcMST += minEdge.weight;
				edgeCounter++;
				System.out.println();
				System.out.println(minEdge.data + "(" + minEdge.weight + ")" + minEdge.parent.data + " Edge added:");
			} else if (dstNodeFound && !dstParentFound) {
				unionNodeLists.get(dstNodeIndex).add(minEdge);
				calcMST += minEdge.weight;
				edgeCounter++;
				System.out.println();
				System.out.println(minEdge.data + "(" + minEdge.weight + ")" + minEdge.parent.data + " Edge added:");
			}
			
			// remove minEdge from edgeList
			edgeList.remove(edgeList.get(findMin(edgeList)));
		}
		return calcMST;
	}
	
	/**
	 * Node class used to store the vertex name/data and the weight of the traversal
	 * path( if not first node in list)
	 */
	class Node {

		char data;
		int weight;
		Node parent;

		/**
		 * Creates a Node with a default weight of 0 and parent set to null. Only called when adding vertices
		 * to graph
		 * 
		 * @param data - Node data letter/name
		 */
		Node(char data) {
			this.data = data;
			this.weight = 0;
			this.parent = null;
		}

		/**
		 * Creates an adjacency node with the weight of the edge and a null parent
		 * that gets added to a LinkedList via the addEdge method
		 * 
		 * @param data
		 * @param weight - Node data letter/name
		 */
		Node(char data, int weight) {
			this.data = data;
			this.weight = weight;
			this.parent = null;
		}
		
		
		/**
		 * this method is used to set the parentNode to the node that gets passed in
		 * 
		 * @param node - node object to be set as parent
		 */
		public void setParent(Node node) {
			this.parent = node;
		}
		
	}

}
