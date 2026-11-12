class PrimNode implements Comparable<PrimNode> {
    int vertex;
    int weight;

    public PrimNode(int vertex, int weight) {
        this.vertex = vertex;
        this.weight = weight;
    }

    @Override
    public int compareTo(PrimNode other) {
        return this.weight - other.weight;
    }
}