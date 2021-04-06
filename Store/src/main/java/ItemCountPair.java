public class ItemCountPair extends Pair<String, Integer>
        implements Comparable<ItemCountPair> {

    public ItemCountPair(String itemId, Integer count) {
         super(itemId, count);
     }

    @Override
    public String toString() {
        return "Item ID:" + this.getFirst() + " Count: " + this.getSecond();
    }

    @Override
    public int compareTo(ItemCountPair o) {
        return this.getSecond() - o.getSecond();
    }
}