import java.io.Serializable;
import java.util.List;

/**
 * A class representing the results obtained from the query:
 * What are the top N most purchased items at store S?
 */
public class StoreQueryResponse implements Serializable {
    private List<ItemItemCount> store;

    public StoreQueryResponse() { }

    public List<ItemItemCount> getItems() {
        return store;
    }

    public void setItems(List<ItemItemCount> store) {
        this.store = store;
    }
}
