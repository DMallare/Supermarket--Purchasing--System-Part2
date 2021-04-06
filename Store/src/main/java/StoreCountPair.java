public class StoreCountPair extends Pair<Integer, Integer>
        implements Comparable<StoreCountPair> {

    public StoreCountPair(Integer storeId, Integer count) {
        super(storeId, count);
    }

    @Override
    public String toString() {
        return "Store ID:" + this.getFirst() + " Count: " + this.getSecond();
    }

    @Override
    public int compareTo(StoreCountPair o) {
        return this.getSecond() - o.getSecond();
    }
}