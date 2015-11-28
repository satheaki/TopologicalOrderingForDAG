import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

/**
 * Program to give the topological ordering of a given graph
 * 
 * @author Akshay
 *
 */
public class TopologicalOrdering {
	Graph.Vertex u, v;

	/* Linked List containing the topological order */
	LinkedList<Graph.Vertex> mTopList;

	Stack<Graph.Vertex> mDFSVertexStack;

	/**
	 * Method to calculate topological order using Queue
	 * 
	 * @param g
	 *            :A input graph
	 * @return Returns a Linked List with topological order of DAG
	 */
	public LinkedList<Graph.Vertex> toplogicalOrder1(Graph g) {
		mTopList = new LinkedList<>();
		int processedNodeCount = 0;
		g.initialize();

		/* Calculating the indegree of each node */
		for (int i = 1; i < g.V.length; i++) {
			for (Graph.Edge edge : g.V[i].Adj) {
				v = edge.otherEnd(g.V[i]);
				if (v != null)
					v.mIndegree++;

			}
		}

		/* Queue containing all unprocessed nodes of degree 0 */
		Queue<Graph.Vertex> unprocessedNodeQueue = new LinkedList<Graph.Vertex>();

		for (int i = 1; i < g.V.length; i++) {
			if (g.V[i].mIndegree == 0) {
				unprocessedNodeQueue.add(g.V[i]);
			}
		}

		while (!unprocessedNodeQueue.isEmpty()) {
			u = unprocessedNodeQueue.remove();
			mTopList.add(u);
			processedNodeCount++;

			/* Decreasing the indegree of node to 0 */
			for (Graph.Edge edge : u.Adj) {
				v = edge.otherEnd(u);
				if (v != null) {
					v.mIndegree--;
					if (v.mIndegree == 0) {
						unprocessedNodeQueue.add(v);
					}
				}
			}

		}

		int checker = checkDAG(processedNodeCount, g);
		if (checker == 0)
			return null;

		return mTopList;
	}

	/**
	 * 
	 * @param g
	 * @return
	 */
	Stack<Graph.Vertex> toplogicalOrder2(Graph g) {
		g.initialize();
		mDFSVertexStack = new Stack<Graph.Vertex>();

			for (int i = 1; i < g.V.length; i++) {
				if (!g.V[i].seen) {
					dfsVisit(g.V[i], mDFSVertexStack);
				}
			}

		return mDFSVertexStack;

	}

	/**
	 * 
	 * @param vertex
	 * @param mDFSVertexStack
	 */
	private void dfsVisit(Graph.Vertex vertex,
			Stack<Graph.Vertex> mDFSVertexStack) {
		vertex.seen = true;
		vertex.mActive = true;

		for (Graph.Edge edge : vertex.Adj) {
			v = edge.otherEnd(vertex);
			if (v != null) {
				if (!v.seen) {
					dfsVisit(v, mDFSVertexStack);
				} else if (v.mActive) {
					System.out.println("Graph is not a DAG");
				}
			}
		}
		mDFSVertexStack.add(vertex);
		vertex.mActive = false;
	}

	/**
	 * Function to check if input graph is a DAG or not
	 * 
	 * @param processedNodeCount
	 *            :counter of total nodes processed
	 * @param g
	 *            :Graph object
	 * @return Return 0 if graph is not a DAG else 1
	 */
	private int checkDAG(int processedNodeCount, Graph g) {
		if (processedNodeCount + 1 != g.V.length) {
			System.out.println("Graph is not a DAG");
			return 0;
		}
		return 1;
	}

	/**
	 * Function to convert a LinkedList to a String format
	 * 
	 * @param resList
	 *            :input Linked List
	 * @return Returns the string represenation of the corresponding list
	 */

	public static String listToString(LinkedList<Graph.Vertex> resList) {
		String result = " ";
		for (int j = 0; j < resList.size(); j++) {
			result += resList.get(j);
		}
		return new StringBuffer(result).toString();

	}

	/**
	 * Main Method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		TopologicalOrdering ordering = new TopologicalOrdering();
		Scanner in;
		String topOrder = "";
		try {
			in = new Scanner(new File(args[0]));
			Graph g = Graph.readGraph(in);

			LinkedList<Graph.Vertex> topOrderList = ordering
					.toplogicalOrder1(g);
			if (topOrderList != null) {
				topOrder = listToString(topOrderList);
				System.out.println("Topological order by Algorithm1:");
				System.out.print(topOrder);
			}

			Stack<Graph.Vertex> topOrderStack = ordering.toplogicalOrder2(g);
			System.out.println("\nTopological order by Algorithm2:");
			while (!topOrderStack.isEmpty()) {
				System.out.print(topOrderStack.pop());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
