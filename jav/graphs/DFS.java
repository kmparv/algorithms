package graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class DFS {
	public static final String INPUT_FILE = "<update input file path>";

	private static final int DFS_WHITE = -1;
	private static final int DFS_BLACK = 1;
	private static final int DFS_GREY = 2;

	private static List<ArrayList<Integer[]>> AdjList;
	private static ArrayList<Integer> dfs_num, dfs_low, dfs_parent;
	private static ArrayList<Boolean> articulation_vertex, visited;
	private static Stack<Integer> st;
	private static ArrayList<Integer> topologicalSort;
	private static int numComp, dfsNumberCounter, dfsRoot, rootChildren;

	private static void initDFS(int V) {
		dfs_num = new ArrayList<Integer>();
		dfs_num.addAll(Collections.nCopies(V, DFS_WHITE));
		numComp = 0;
	}

	private static void initGraphCheck(int V) {
		initDFS(V);
		dfs_parent = new ArrayList<Integer>();
		dfs_parent.addAll(Collections.nCopies(V, 0));
	}

	private static void dfs(int u) {
		System.out.print(u);
		dfs_num.set(u, DFS_BLACK);
		for (int i = 0; i < AdjList.get(u).size(); i++) {
			Integer[] v = AdjList.get(u).get(i);
			if (dfs_num.get(v[0]) == DFS_WHITE) {
				dfs(v[0]);
			}
		}
	}

	private static void floodFill(int u, int color) {
		dfs_num.set(u, color);
		for (int i = 0; i < AdjList.get(u).size(); i++) {
			Integer[] v = AdjList.get(u).get(i);
			if (dfs_num.get(v[0]) == DFS_WHITE) {
				floodFill(v[0], color);
			}
		}
	}

	private static void graphCheck(int u) {
		dfs_num.set(u, DFS_GREY);
		for (int i = 0; i < AdjList.get(u).size(); i++) {
			Integer[] v = AdjList.get(u).get(i);
			if (dfs_num.get(v[0]) == DFS_WHITE) {
				dfs_parent.set(v[0], u);
				graphCheck(v[0]);
			} else if (dfs_num.get(v[0]) == DFS_GREY) {
				if (v[0] == dfs_parent.get(u)) {
					System.out.println(" Bidirectional Edge ( " + u + ", " + v[0] + " ) - ( " + v[0] + ", " + u + " )");
				} else {
					System.out.println(" Back Edge ( " + u + ", " + v[0] + " )");
				}
			} else if (dfs_num.get(v[0]) == DFS_BLACK) {
				System.out.println(" Forward/Cross Edge ( " + u + ", " + v[0] + " )");
			}
		}
		dfs_num.set(u, DFS_BLACK);
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws FileNotFoundException {
		int V, total_neighbors, id, weight;

		File f = new File(INPUT_FILE);
		Scanner sc = new Scanner(f);

		V = sc.nextInt();
		AdjList = new ArrayList<ArrayList<Integer[]>>();
		AdjList.clear();
		for (int i = 0; i < V; i++) {
			ArrayList<Integer[]> neighbors = new ArrayList<>();
			AdjList.add(neighbors);
		}
		// System.out.println(AdjList);
		for (int i = 0; i < V; i++) {
			total_neighbors = sc.nextInt();
			for (int j = 0; j < total_neighbors; j++) {
				id = sc.nextInt();
				weight = sc.nextInt();
				AdjList.get(i).add(new Integer[] { id, weight });
			}
		}
		// System.out.println(AdjList);
		initDFS(V);
		System.out.println("Standard DFS Demo (the input graph must be UNDIRECTED)");
		for (int i = 0; i < V; i++) {
			if (dfs_num.get(i) == DFS_WHITE) {
				System.out.print("Component Visit : " + (++numComp));
				dfs(i);
				System.out.println();
			}
		}
		System.out.print("There are %d connected components\n" + numComp);

		initDFS(V);
		System.out.println("Flood Fill Demo (the input graph must be UNDIRECTED)");
		for (int i = 0; i < V; i++) {
			if (dfs_num.get(i) == DFS_WHITE) {
				floodFill(i, ++numComp);
			}
		}
		for (int i = 0; i < V; i++) {
			System.out.print("Vertex " + i + " has color " + dfs_num.get(i));
			System.out.println();
		}

		initGraphCheck(V);
		System.out.println("Graph Edges Property Check");
		for (int i = 0; i < V; i++) {
			if (dfs_num.get(i) == DFS_WHITE) {
				System.out.println("Component : " + (++numComp));
				graphCheck(i);
			}
		}
	}

}
