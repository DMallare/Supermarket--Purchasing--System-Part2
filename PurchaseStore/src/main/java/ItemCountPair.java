import java.util.Comparator;

public class ItemCountPair implements Comparator<ItemCountPair>, Comparable<ItemCountPair> {
    private String itemId;
    private int count;

    ItemCountPair(String itemId, Integer count) {
        this.itemId = itemId;
        this.count = count;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int compare(ItemCountPair o1, ItemCountPair o2) {
        return 0;
    }

    @Override
    public int compareTo(ItemCountPair o) {
        if (o.getCount() > this.getCount()) {
            return 1;
        } else if (o.getCount() < this.getCount()) {
            return -1;
        }

        return 0;
    }
}