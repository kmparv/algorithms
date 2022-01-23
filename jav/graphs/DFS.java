/**
 * @author 	: parveenkumar (kmparv)
 * @created	: 01.23.2022 
 * */

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
    private static void initArticulationPointAndBridge(int V) {
		initGraphCheck(V);
		dfs_low = new ArrayList<Integer>();
		dfs_low.addAll(Collections.nCopies(V, 0));
		articulation_vertex = new ArrayList<Boolean>();
		articulation_vertex.addAll(Collections.nCopies(V, false));
		dfsNumberCounter = 0;
	}

	private static void initTarjanSCC(int V) {
		initGraphCheck(V);
		dfs_low = new ArrayList<Integer>();
		dfs_low.addAll(Collections.nCopies(V, 0));
		dfsNumberCounter = 0;
		numComp = 0;
		st = new Stack<Integer>();
		visited = new ArrayList<Boolean>();
		visited.addAll(Collections.nCopies(V, false));
	}

	private static void initTopologicalSort(int V) {
		initDFS(V);
		topologicalSort = new ArrayList<Integer>();
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
    private static void articulationPointAndBridge(int u) {
		dfs_low.set(u, dfsNumberCounter);
		dfs_num.set(u, dfsNumberCounter++);
		for (int i = 0; i < AdjList.get(u).size(); i++) {
			Integer[] v = AdjList.get(u).get(i);
			if (dfs_num.get(v[0]) == DFS_WHITE) {
				dfs_parent.set(v[0], u);
				if (dfsRoot == u) {
					rootChildren++;
				}
				articulationPointAndBridge(v[0]);
				if (dfs_low.get(v[0]) >= dfs_num.get(u)) {
					articulation_vertex.set(u, true);
				}
				if (dfs_low.get(v[0]) > dfs_num.get(u)) {
					System.out.println(" Edge ( " + u + ", " + v[0] + " ) is a bridge");
				}
				dfs_low.set(u, Math.min(dfs_low.get(u), dfs_low.get(v[0])));
			} else if (v[0] != dfs_parent.get(u)) {
				dfs_low.set(u, Math.min(dfs_low.get(u), dfs_num.get(v[0])));
			}
		}
	}

	private static void tarjanSCC(int u) {
		dfs_num.set(u, dfsNumberCounter);
		dfs_low.set(u, dfsNumberCounter++);
		st.push(u);
		visited.set(u, true);

		for (int i = 0; i < AdjList.get(u).size(); i++) {
			Integer[] v = AdjList.get(u).get(i);
			if (dfs_num.get(v[0]) == DFS_WHITE) {
				tarjanSCC(v[0]);
				if (visited.get(v[0])) {
					dfs_low.set(u, Math.min(dfs_low.get(u), dfs_low.get(v[0])));
				}
			}
		}
		if (dfs_low.get(u) == dfs_num.get(u)) {
			System.out.print("SCC : " + (++numComp));
			while (true) {
				int v = st.pop();
				visited.set(v, false);
				System.out.print(" " + v);
				if (u == v)
					break;
			}
			System.out.println();
		}
	}

	private static void topoSort(int u) {
		dfs_num.set(u, DFS_BLACK);
		for (int i = 0; i < AdjList.get(u).size(); i++) {
			Integer[] v = AdjList.get(u).get(i);
			if(dfs_num.get(v[0]) == DFS_WHITE){
				topoSort(v[0]);
			}
		}
		topologicalSort.add(u);
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
        initArticulationPointAndBridge(V);
		System.out.println("Articulation Points & Bridges (the input graph must be UNDIRECTED)");
		System.out.println("Bridges: ");
		for (int i = 0; i < V; i++) {
			if (dfs_num.get(i) == DFS_WHITE) {
				dfsRoot = i;
				rootChildren = 0;
				articulationPointAndBridge(i);
				articulation_vertex.set(dfsRoot, (rootChildren > 1)); // special case
			}
		}
		System.out.println("Articulation Points : ");
		for (int i = 0; i < V; i++) {
			if (articulation_vertex.get(i))
				System.out.println("Vertex : " + i);
		}

		initTarjanSCC(V);
		System.out.println("Strongly Connected Components (the input graph must be DIRECTED)");
		for (int i = 0; i < V; i++) {
			if (dfs_num.get(i) == DFS_WHITE) {
				tarjanSCC(i);
			}
		}
		
		initTopologicalSort(V);
		System.out.println("Toplogical Sort (the input graph must be DAG) ");
		for(int i = 0; i < V; i++) {
			if(dfs_num.get(i) == DFS_WHITE) {
				topoSort(i);
			}
		}
		for(int i = topologicalSort.size()-1; i >= 0; i--) {
			System.out.print(" " + topologicalSort.get(i));
		}
		System.out.println();
	}

}
