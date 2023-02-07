/*
 *
 MovieGraph.java
 COSC 102, Colgate University

 Your code goes here.
 See instructions for explanation of methods.

 */

import java.io.*;
import java.util.*;

public class MovieGraph
{


	/*
	 	Adjacency list Graph implementation (used for more efficient memory management)
		(Array of Linked Lists)
	 */
	private class ListGraph {
		Edge[] NodeArray;																													// Main node array for storing adjacent nodes
		ArrayList<String> Nodes = new ArrayList<String>();												// Data for individual Nodes + indices
		HashMap<String,Integer> indices = new HashMap<String,Integer>();					// Stores individual indices for actor/movie

		/* Edges to adjacent nodes */
		private class Edge {
			int vertex;							// index identifier for each adjacent node
			Edge next;							// pointer to other Edges
			public Edge(int vertex, Edge next) {
				this.vertex = vertex;
				this.next = next;
			}
		}

		/* Make a graph with n nodes in total */
		public void make(int n) {
			NodeArray = new Edge[n];
			for (int i = 0; i < NodeArray.length; i++) NodeArray[i] = new Edge(i, null);
		}

		/* Add an edge to the graph (vertex1 -- vertex2) */
		public void addEdge(int vertex1, int vertex2) {
			Edge current = NodeArray[vertex1];
			while (current.next != null) {
				current = current.next;
			}
			current.next = new Edge(vertex2, null);	// make link from vertex1--vertex2

			current = NodeArray[vertex2];						// bidirectional edges - vertex2--vertex1 edge also created
			while (current.next != null) {
				current = current.next;
			}
			current.next = new Edge(vertex1, null);	// make link from vertex2--vertex1 (bidirectional edges)
		}

		/* Return an ArrayList of adjacent nodes */
		public ArrayList<Integer> adjacentNodesList(int vertex) {
			ArrayList<Integer> adjacentNodesList = new ArrayList<Integer>();
			Edge current = NodeArray[vertex];
			current = current.next;
			if (current == null) return null;		// if there are no adjacent nodes, return null
			while (current != null) {
				adjacentNodesList.add(current.vertex);
				current = current.next;
			}
			return adjacentNodesList;						// return list of adjacent nodes (indices)
		}

	}


	ListGraph GRAPH;


  /*
		Constructor
		Gets passed all of the provided read in data
		in the form of an ArrayList of String arrays.
		Each string array represents one line of the source data
		split on the forward slashes '/'.
   */
  public MovieGraph(ArrayList<String[]> data)
	{
		GRAPH = new ListGraph();		// initialise new Graph (Adjacency List) object

		// Indice Generator
		int index = 0;
		for (String[] row : data) {										// Generate indices for each movie/actor in parsed data
			for (int i = 0; i < row.length; i++) {
				if (GRAPH.indices.get(row[i]) == null) {	// Generate new index for element if it isn't already present in indices (takes care of overlaps)
					GRAPH.indices.put(row[i], index);
					index++;
					GRAPH.Nodes.add(row[i]);								// Nodes data
				}
			}
		}

		GRAPH.make(index);	// Make a graph with [index] number of nodes

		// Make new graph, and fill it with information about the relationships between nodes
		for (String[] row : data) {
			int movieIndex = GRAPH.indices.get(row[0]);
			for (int i = 1; i < row.length; i++) {
				int actorIndex = GRAPH.indices.get(row[i]);
				GRAPH.addEdge(actorIndex, movieIndex);		// adds edges to adjacent node (bidirectional edges)
			}
		}
  }

  /*
		Returns an ArrayList of Strings which is the shortest path of movies/actors between
		target1 and target2.
		If no path can be found, can return either null or an empty ArrayList
   */
  public ArrayList<String> findShortestLink(String target1, String target2) {
		ArrayList<String> shortestLink = new ArrayList<String>();		// Shortest link from target1 to target2

		HashSet<String> adjacentNodes = new HashSet<String>();											// keeps track of adjacent nodes
		HashSet<String> alreadyVisited = new HashSet<String>();											// Already visited nodes (HashSet used for constant time lookup of already visited nodes)
		HashMap<String,String> BFSTrajectory = new HashMap<String,String>();				// Breadth-first search traversed path (HashMap<Node, PreviousNode>)
		LinkedList<String> BFSQueue = new LinkedList<String>();											// Queue for breadth-first search

		boolean pathFound = false;
		int currentNodeIndex = GRAPH.Nodes.indexOf(target1);					// index of the current node

		// If either target1 or target2 not in list of nodes, then don't bother searching
		if (!GRAPH.Nodes.contains(target1) || !GRAPH.Nodes.contains(target2)) return null;

		/* Initialise Breadth-first search */
		BFSTrajectory.put(target1, null);					// add to our BFS Search trajectory (tracking our path)
		BFSQueue.add(target1);

		/* Search through the graph until a link is found */
		while (!pathFound) {
			currentNodeIndex = GRAPH.Nodes.indexOf(BFSQueue.poll());							// Remove node from queue, and then later look for its adjacent nodes
			if (currentNodeIndex == -1) break;																		// if no adjacent nodes are found : break the BFS
			String currentNode = GRAPH.Nodes.get(currentNodeIndex);								// Make node at current index the current node
			alreadyVisited.add(currentNode);																			// add currentNode to nodes already visited

			/* look in the graph for adjacent nodes */
			for (int i : GRAPH.adjacentNodesList(currentNodeIndex)) {
				adjacentNodes.add(GRAPH.Nodes.get(i));															// keep track of all nodes adjacent to current node
				if (!alreadyVisited.contains(GRAPH.Nodes.get(i))) {									// If node has been already visited, don't add to queue
					BFSQueue.add(GRAPH.Nodes.get(i));																	// add to queue for breadth-first search
					BFSTrajectory.put(GRAPH.Nodes.get(i), GRAPH.Nodes.get(currentNodeIndex));	// Add to our BFS Search trajectory hashmap
					alreadyVisited.add(GRAPH.Nodes.get(i));
				}
			}

			if (adjacentNodes.contains(target2))			// If target2 is in one of the adjacent nodes, end search
				pathFound = true;
			adjacentNodes.clear();
		}

		if (currentNodeIndex == -1) return null; 		// no link found - return null

		/* Find shortest link */
		String nodeKey = target2;
		shortestLink.add(nodeKey);									// fill ArrayList with shortest trajectory
		while (!nodeKey.equals(target1)) {
			shortestLink.add(0, BFSTrajectory.get(nodeKey));
			nodeKey = BFSTrajectory.get(nodeKey);
		}

		/* return shortest link from target1 to target2 */
		return shortestLink;

  }

}
