#define _CRT_SECURE_NO_DEPRECATE
#include <vector>
#include <algorithm>
#include <iostream>
#include <ios>
#include <stdio.h>
#include <stdlib.h>


#define DFS_WHITE -1
#define DFS_BLACK 1

#define DFS_GREY 2

#ifdef LOCAL
#define debug(...) cerr << "[" << #__VA_ARGS__ << "]:", debug_out(__VA_ARGS__)
#else
#define debug(...) 42
#endif

using namespace std;
void debug_out() { cerr << endl; }

template <typename Head, typename... Tail>
void debug_out(Head H, Tail... T) {
	cerr << " " << to_string(H);
	debug_out(T...);
}

class DFS {
public:
	vector<int> dfs_num;
	int numCC;
	vector<vector<pair<int, int>>> AdjList;

	void dfs(int& u) {
		cout << u;
		dfs_num[u] = DFS_BLACK;
		for (int i = 0; i < (int)AdjList[u].size(); i++) {
			pair<int, int> v = AdjList[u][i];
			if (dfs_num[v.first] == DFS_WHITE) {
				dfs(v.first);
			}
		}
	}

	void floodfill(int& u, int& color) {
		cout << color << endl;
		dfs_num[u] = color;
		for (int i = 0; i < (int)AdjList[u].size(); i++) {
			pair<int, int> v = AdjList[u][i];
			if (dfs_num[v.first] == DFS_WHITE) {
				floodfill(v.first, color);
			}
		}
	}

	vector<int> topSort;
	void dfs2(int u) {
		dfs_num[u] = DFS_BLACK;
		for (int i = 0; i < (int)AdjList[u].size(); i++) {
			pair<int, int> v = AdjList[u][i];
			if (dfs_num[v.first] == DFS_WHITE) {
				dfs2(v.first);
			}
		}
		topSort.push_back(u);
	}

	vector<int> dfs_parent;
	void graphCheck(int u) {
		dfs_num[u] = DFS_GREY;
		for (int i = 0; i < (int)AdjList[u].size(); i++) {
			pair<int, int> v = AdjList[u][i];
			if (dfs_num[v.first] == DFS_WHITE) {
				dfs_parent[v.first] = u;
				graphCheck(v.first);
			}
			else if (dfs_num[v.first] == DFS_GREY) {
				if (v.first == dfs_parent[u]) {
					cout << "Birectional ( " << u << " " << v.first << " ) - ( " << v.first << " " << u << " )\n";
				}
				else {
					cout << "Backedge ( " << u <<" " << v.first << " ) (Cycle)" << "\n";
				}
			}
			else if (dfs_num[v.first] == DFS_BLACK) {
				cout << "Forward / Cross Edge ( " << u << " "<< v.first << " )" << "\n";
			}
		}
		dfs_num[u] = DFS_BLACK;
	}


	vector<int> dfs_low;
	vector<int> articulation_vertex;
	int dfsNumCounter, dfsRoot, rootChildern;
	void articulationPointAndBridges(int u) {

	}
};

int main() {
	DFS cls;
	int V, total_neighbors, id, weight;
	FILE* f = freopen("DFS.txt", "r", stdin);
	//scanf("%d", &V);
	cin >> V;
	cls.AdjList.assign(V, vector<pair<int, int>>());
	for (int i = 0; i < V; i++) {
		cin >> total_neighbors;
		for (int j = 0; j < total_neighbors; j++) {
			cin >> id >> weight;
			cls.AdjList[i].push_back(pair<int, int>(id, weight));
		}
	}
	cls.numCC = 0;
	cls.dfs_num.assign(V, DFS_WHITE);
	for (int i = 0; i < V; i++) {
		if (cls.dfs_num[i] == DFS_WHITE) {
			cout << "Component :" << ++cls.numCC;
			//debug(cls.dfs(i));
			cls.dfs(i);
			cout << "\n";
		}
	}
	cout << "There are " << cls.numCC << " connceted components\n";
	cout << V << endl;

	cout << "Flood Fill Demo(the input graph must be UNDIRECTED)" << "\n";
	cls.numCC = 0;
	cls.dfs_num.assign(V, DFS_WHITE);
	for (int i = 0; i < V; i++) {
		if (cls.dfs_num[i] == DFS_WHITE) {
			cls.floodfill(i, ++cls.numCC);
		}
	}
	for (int i = 0; i < V; i++)
		cout << "Vertex " << i << " has color " << cls.dfs_num[i] << "\n";


	cout << "Topological Sort (the input graph must be DAG)\n";
	cls.topSort.clear();
	cls.dfs_num.assign(V, DFS_WHITE);
	for (int i = 0; i < V; i++) {
		if (cls.dfs_num[i] == DFS_WHITE) {
			cls.dfs2(i);
		}
	}
	reverse(cls.topSort.begin(), cls.topSort.end());
	for (int i = 0; i < (int)cls.topSort.size(); i++) {
		cout << cls.topSort[i];
	}cout << "\n";

	cout << "Graph Edges Property Check" << endl;
	cls.numCC = 0;
	cls.dfs_num.assign(V, DFS_WHITE);
	cls.dfs_parent.assign(V, DFS_WHITE);
	for (int i = 0; i < V; i++) {
		if (cls.dfs_num[i] == DFS_WHITE) {
			cout << "Component : " << i << "\n";
			cls.graphCheck(i);
		}
	}
	return 0;
}