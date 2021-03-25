import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoundedSortedList<T extends Comparable<T>> extends ArrayList<T> {
    private static final int MAX_LENGTH = 10;
    private List<T> items;

    public BoundedSortedList() {
        this.items = new ArrayList<>();
    }

    public synchronized void addItem(T item) {
        if (items.size() == MAX_LENGTH) {
            items.add(item);
        } else {
            for (int i = 0; i < items.size(); i++) {
                if (item.compareTo(items.get(i)) > 0) {
                    items.add(item);
                    break;
                }
            }
        }

        Collections.sort(items);

        // If we have gone beyond the max length of the array, kick
        // out the lowest value
        if (items.size() > MAX_LENGTH) {
            items.remove(0);
        }
    }

}
