import java.util.*;
public class Main {
    static int find(int[] parent, int i) {
        if (parent[i] == i)
            return i;
        return find(parent, parent[i]);
    }

    static void union(int[] parent, int x, int y) {
        int xset = find(parent, x);
        int yset = find(parent, y);
        parent[xset] = yset;
    }

    public static void kruskalMST(int vertices, List<Edge> edges) {
        System.out.println("\nАлгоритм Краскала");
        Collections.sort(edges);

        int[] parent = new int[vertices + 1];
        for (int i = 1; i <= vertices; i++) {
            parent[i] = i;
        }

        List<Edge> mst = new ArrayList<>();
        int mstWeight = 0;

        for (Edge edge : edges) {
            int x = find(parent, edge.src);
            int y = find(parent, edge.dest);

            if (x != y) {
                mst.add(edge);
                mstWeight += edge.weight;
                union(parent, x, y);
            }
        }

        System.out.println("Ребра MST: " + mst);
        System.out.println("Общий вес: " + mstWeight);
    }

    public static void primMST(int vertices, List<List<Edge>> adjList, int startVertex) {
        System.out.println("\nАлгоритм Прима");
        boolean[] inMST = new boolean[vertices + 1];
        PriorityQueue<PrimNode> pq = new PriorityQueue<>();

        int mstWeight = 0;
        List<String> mstEdges = new ArrayList<>();

        pq.add(new PrimNode(startVertex, 0));

        while (!pq.isEmpty()) {
            PrimNode node = pq.poll();
            int u = node.vertex;

            if (inMST[u]) continue;

            inMST[u] = true;
            mstWeight += node.weight;

            if (node.weight > 0) {
                mstEdges.add("Вершина " + u + " (вес " + node.weight + ")");
            }

            for (Edge edge : adjList.get(u)) {
                if (!inMST[edge.dest]) {
                    pq.add(new PrimNode(edge.dest, edge.weight));
                }
            }
        }

        System.out.println("Добавленные вершины:");
        for(String s : mstEdges) System.out.println(s);
        System.out.println("Общий вес: " + mstWeight);
    }

    public static void main(String[] args) {
        int vertices = 9;

        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(1, 2, 5));
        edges.add(new Edge(1, 3, 3));
        edges.add(new Edge(3, 4, 2));
        edges.add(new Edge(3, 5, 2));
        edges.add(new Edge(3, 7, 3));
        edges.add(new Edge(5, 6, 3));
        edges.add(new Edge(5, 7, 6));
        edges.add(new Edge(7, 8, 2));
        edges.add(new Edge(8, 9, 4));

        List<List<Edge>> adjList = new ArrayList<>();
        for (int i = 0; i <= vertices; i++) {
            adjList.add(new ArrayList<>());
        }

        for (Edge e : edges) {
            adjList.get(e.src).add(new Edge(e.src, e.dest, e.weight));
            adjList.get(e.dest).add(new Edge(e.dest, e.src, e.weight)); // Обратное ребро
        }

        kruskalMST(vertices, edges);
        primMST(vertices, adjList, 1); // Начальная вершина 1
    }
}