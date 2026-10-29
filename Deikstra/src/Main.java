import java.util.*;

    //Number 1
    static class Edge {
        int to, w;
        Edge(int to, int w) { this.to = to; this.w = w; }
    }

    public static long totalDelay(int r, List<List<Edge>> adj, List<Integer> entryPoints) {
        final int MAX_DIST = 100 * r;
        final long INF = Long.MAX_VALUE;

        long[] dist = new long[r];
        Arrays.fill(dist, INF);

        List<List<Integer>> buckets = new ArrayList<>(MAX_DIST + 1);
        for (int i = 0; i <= MAX_DIST; i++) buckets.add(new ArrayList<>());

        for (int s : entryPoints) {
            dist[s] = 0;
            buckets.get(0).add(s);
        }

        for (int d = 0; d <= MAX_DIST; d++) {
            List<Integer> bucket = buckets.get(d);
            for (int idx = 0; idx < bucket.size(); idx++) {
                int v = bucket.get(idx);
                if (dist[v] != d) continue;
                for (Edge e : adj.get(v)) {
                    long nd = (long) d + e.w;
                    if (nd < dist[e.to] && nd <= MAX_DIST) {
                        dist[e.to] = nd;
                        buckets.get((int) nd).add(e.to);
                    }
                }
            }
        }

        long total = 0;
        for (int v = 0; v < r; v++) total += dist[v];
        return total;
    }

    //Number 2
    public static int minFlights(int c, int[][] f, int home, int[] tours) {
        List<List<Integer>> adj = new ArrayList<>(c);
        for (int i = 0; i < c; i++) adj.add(new ArrayList<>());
        for (int[] e : f) adj.get(e[0]).add(e[1]);

        int[] special = new int[] { home, tours[0], tours[1], tours[2] };

        int[][] dist = new int[4][];
        for (int i = 0; i < 4; i++) {
            dist[i] = bfs(adj, c, special[i]);
        }

        int[][] perms = {
                {1, 2, 3}, {1, 3, 2},
                {2, 1, 3}, {2, 3, 1},
                {3, 1, 2}, {3, 2, 1}
        };

        final int INF = Integer.MAX_VALUE / 2;
        int best = INF;

        for (int[] p : perms) {
            int total = 0;
            boolean ok = true;
            int[] path = { 0, p[0], p[1], p[2], 0 };
            for (int i = 0; i + 1 < path.length; i++) {
                int fromIdx = path[i];
                int toNode = special[path[i + 1]];
                int d = dist[fromIdx][toNode];
                if (d < 0) { ok = false; break; }
                total += d;
            }
            if (ok) best = Math.min(best, total);
        }

        return best == INF ? -1 : best;
    }

    private static int[] bfs(List<List<Integer>> adj, int c, int s) {
        int[] d = new int[c];
        Arrays.fill(d, -1);
        d[s] = 0;
        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.add(s);
        while (!q.isEmpty()) {
            int v = q.poll();
            for (int u : adj.get(v)) {
                if (d[u] == -1) {
                    d[u] = d[v] + 1;
                    q.add(u);
                }
            }
        }
        return d;
    }


    public static void main(String[] args) {
        //Number 1
        int r1 = 5;
        List<List<Edge>> adj1 = new ArrayList<>();
        for (int i = 0; i < r1; i++) adj1.add(new ArrayList<>());
        addUndirected(adj1, 0, 1, 1);
        addUndirected(adj1, 1, 2, 1);
        addUndirected(adj1, 2, 4, 1);
        addUndirected(adj1, 4, 3, 1);
        addUndirected(adj1, 3, 0, 1);

        List<Integer> entries1 = Arrays.asList(0, 4);
        long delay = totalDelay(r1, adj1, entries1);
        System.out.println("Number 1: суммарная задержка = " + delay);

        //Number 2
        int c2 = 5;
        int[][] flights = {
                {0, 1}, {0, 4},
                {4, 2}, {4, 3},
                {1, 4},
                {2, 3}, {3, 0},
                {2, 0}, {3, 1}
        };
        int home = 0;
        int[] tours = {1, 2, 3};
        int res = minFlights(c2, flights, home, tours);
        System.out.println("Number 2: минимум перелётов = " + res);
    }

    private static void addUndirected(List<List<Edge>> adj, int u, int v, int w) {
        adj.get(u).add(new Edge(v, w));
        adj.get(v).add(new Edge(u, w));
    }
