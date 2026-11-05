import java.util.*;
    static final long INF = Long.MAX_VALUE / 4;

    static int n;
    static List<int[]>[] adj;

    static long[] dijkstra(List<Integer> sources, int[] prev) {
        long[] dist = new long[n];
        Arrays.fill(dist, INF);

        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[1]));
        for (int s : sources) {
            dist[s] = 0;
            if (prev != null) prev[s] = -1;
            pq.add(new long[]{s, 0});
        }

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            int v = (int) cur[0];
            long d = cur[1];
            if (d != dist[v]) continue;
            for (int[] e : adj[v]) {
                int to = e[0];
                long w = e[1];
                if (d + w < dist[to]) {
                    dist[to] = d + w;
                    if (prev != null) prev[to] = v;
                    pq.add(new long[]{to, d + w});
                }
            }
        }
        return dist;
    }

    static List<Integer> buildPath(int[] prev, int target) {
        LinkedList<Integer> path = new LinkedList<>();
        for (int v = target; v != -1; v = prev[v]) path.addFirst(v);
        return path;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        n = sc.nextInt();
        int m = sc.nextInt();

        adj = new ArrayList[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();

        for (int i = 0; i < m; i++) {
            int u = sc.nextInt(), v = sc.nextInt();
            long w = sc.nextLong();
            adj[u].add(new int[]{v, (int) w});
            adj[v].add(new int[]{u, (int) w});
        }

        int k = sc.nextInt();
        List<Integer> entries = new ArrayList<>();
        for (int i = 0; i < k; i++) entries.add(sc.nextInt());

        int S = sc.nextInt(), A = sc.nextInt(), B = sc.nextInt(), C = sc.nextInt();
        int[] candidates = {sc.nextInt(), sc.nextInt(), sc.nextInt()};

        //
        long[] dist = dijkstra(entries, null);

        long total = 0;
        int maxV = 0;
        for (int v = 0; v < n; v++) {
            total += dist[v];
            if (dist[v] > dist[maxV]) maxV = v;
        }

        System.out.println("Number 1");
        System.out.println("Общая задержка: " + total + " м");
        System.out.println("Максимальная задержка: маршрутизатор " + maxV + " (" + dist[maxV] + " м)");
        System.out.printf("Средняя задержка: %.2f м%n", (double) total / n);


        //
        int[] key = {S, A, B, C};
        long[][] d = new long[4][];
        int[][] prev = new int[4][];
        for (int i = 0; i < 4; i++) {
            prev[i] = new int[n];
            d[i] = dijkstra(Collections.singletonList(key[i]), prev[i]);
        }

        int[][] perms = {
                {1, 2, 3}, {1, 3, 2},
                {2, 1, 3}, {2, 3, 1},
                {3, 1, 2}, {3, 2, 1}
        };

        long bestCost = INF;
        int[] bestPerm = null;

        for (int[] p : perms) {
            long cost = d[0][key[p[0]]]
                    + d[p[0]][key[p[1]]]
                    + d[p[1]][key[p[2]]]
                    + d[p[2]][key[0]];
            if (cost < bestCost) {
                bestCost = cost;
                bestPerm = p;
            }
        }

        List<Integer> full = new ArrayList<>();
        int[] orderIdx = {0, bestPerm[0], bestPerm[1], bestPerm[2], 0};

        for (int i = 0; i < 4; i++) {
            int fromIdx = orderIdx[i];
            int toIdx = orderIdx[i + 1];
            int fromV = key[fromIdx];
            int toV = key[toIdx];

            List<Integer> seg = buildPath(prev[fromIdx], toV);
            if (full.isEmpty()) {
                full.addAll(seg);
            } else {
                for (int j = 1; j < seg.size(); j++) full.add(seg.get(j));
            }
        }

        System.out.println("\nNumber 2");
        System.out.println("Оптимальный порядок: S -> " + key[bestPerm[0]]
                + " -> " + key[bestPerm[1]]
                + " -> " + key[bestPerm[2]] + " -> S");
        System.out.print("Полный маршрут: ");
        for (int i = 0; i < full.size(); i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(full.get(i));
        }
        System.out.println();
        System.out.println("Суммарная длина: " + bestCost + " м");

        //
        System.out.println("\nNumber 3");

        long bestReduction = -1;
        int bestCand = -1;

        for (int X : candidates) {
            long[] dX = dijkstra(Collections.singletonList(X), null);
            long newTotal = 0;
            for (int v = 0; v < n; v++) newTotal += Math.min(dist[v], dX[v]);

            long reduction = total - newTotal;
            double pct = total == 0 ? 0 : 100.0 * reduction / total;

            System.out.printf("Кандидат %d: задержка %d м, снижение %d м (%.2f%%)%n",
                    X, newTotal, reduction, pct);

            if (reduction > bestReduction) {
                bestReduction = reduction;
                bestCand = X;
            }
        }

        double bestPct = total == 0 ? 0 : 100.0 * bestReduction / total;
        System.out.printf("%nОптимальный: %d, снижение %d м (%.2f%%)%n",
                bestCand, bestReduction, bestPct);
    }