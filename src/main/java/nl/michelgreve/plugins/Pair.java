package nl.michelgreve.plugins;

class Pair<FIRST, SECOND> {
    private final FIRST first;
    private final SECOND second;

    public Pair(final FIRST first, final SECOND second) {
        super();
        this.first = first;
        this.second = second;
    }

    public int hashCode() {
        int hashFirst = first != null ? first.hashCode() : 0;
        int hashSecond = second != null ? second.hashCode() : 0;

        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    public boolean equals(final Object other) {
        if (other instanceof Pair) {
            final Pair<?, ?> otherPair = (Pair<?, ?>) other;
            return ((this.first == otherPair.first || (this.first != null && otherPair.first != null && this.first.equals(otherPair.first))) && (this.second == otherPair.second || (this.second != null
                    && otherPair.second != null && this.second.equals(otherPair.second))));
        }

        return false;
    }

    public String toString() {
        return "(" + first + "/" + second + ")";
    }

    public FIRST getFirst() {
        return first;
    }

    public SECOND getSecond() {
        return second;
    }
}