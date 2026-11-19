import java.util.Arrays;

    static class Edge {
        int source, destination, weight;

        Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    static class Graph {
        int vertices;
        int edgesCount;
        Edge[] edges;

        Graph(int vertices, int edgesCount) {
            this.vertices = vertices;
            this.edgesCount = edgesCount;
            this.edges = new Edge[edgesCount];
        }

        public void addEdge(int index, int u, int v, int w) {
            edges[index] = new Edge(u, v, w);
        }

        public void bellmanFord(int source) {
            int[] dist = new int[vertices];
            int[] parent = new int[vertices];

            Arrays.fill(dist, Integer.MAX_VALUE);
            Arrays.fill(parent, -1);
            dist[source] = 0;

            for (int i = 1; i < vertices; ++i) {
                for (int j = 0; j < edgesCount; ++j) {
                    int u = edges[j].source;
                    int v = edges[j].destination;
                    int weight = edges[j].weight;

                    if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                        dist[v] = dist[u] + weight;
                        parent[v] = u;
                    }
                }
            }

            int cycleStartVertex = -1;
            for (int j = 0; j < edgesCount; ++j) {
                int u = edges[j].source;
                int v = edges[j].destination;
                int weight = edges[j].weight;

                if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                    System.out.println("Обнаружен зацикленный отрицательный граф");
                    cycleStartVertex = v;
                    break;
                }
            }

            if (cycleStartVertex == -1) {
                System.out.println("Отрицательных циклов не обнаружено.");
                printDistances(dist);
            } else {
                printNegativeCycle(cycleStartVertex, parent);
            }
        }

        private void printNegativeCycle(int startVertex, int[] parent) {
            int current = startVertex;
            for (int i = 0; i < vertices; i++) {
                current = parent[current];
            }

            System.out.print("Пример цикла: ");
            int temp = current;
            do {
                System.out.print(temp + " -> ");
                temp = parent[temp];
            } while (temp != current && temp != -1);
            System.out.println(current + " (замыкание)");
        }

        private void printDistances(int[] dist) {
            System.out.println("Кратчайшие расстояния от источника:");
            for (int i = 0; i < vertices; i++) {
                System.out.println("Вершина " + i + ": " + (dist[i] == Integer.MAX_VALUE ? "∞" : dist[i]));
            }
        }
    }

    public static void main(String[] args) {
        int V = 5;
        int E = 7;

        Graph graph = new Graph(V, E);

        graph.addEdge(0, 0, 1, 1);
        graph.addEdge(1, 1, 2, -2);
        graph.addEdge(2, 2, 3, -2);

        graph.addEdge(3, 3, 1, 1);
        graph.addEdge(4, 3, 4, 5);
        graph.addEdge(5, 0, 4, 10);
        graph.addEdge(6, 4, 0, 2);

        System.out.println("Запуск анализа графа...");
        graph.bellmanFord(0);
    }
