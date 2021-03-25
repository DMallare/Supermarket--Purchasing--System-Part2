import java.util.ArrayList;
import java.util.List;

public class BoundedSortedList<T extends Comparable<T>> extends ArrayList<T> {
    private static final int MAX_LENGTH = 10;
    private List<T> items;

    public BoundedSortedList() {
        this.items = new ArrayList<>();
    }

    public synchronized void addItem(T item) {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (item.compareTo(items.get(i)) > 0) {
                shift(i, item);
                break;
            }
        }
    }

    private synchronized void shift(int index, T item) {
        T newValue = item;
        for (int i = index; i >= 0; i--) {
            T oldValue = items.get(i);
            items.set(i, newValue);
            newValue = oldValue;
        }
    }

}
