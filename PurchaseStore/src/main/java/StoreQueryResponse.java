import java.io.Serializable;
import java.util.List;

/**
 * A class representing the results obtained from the query:
 * What are the top 10 most purchased items at store N?
 */
public class StoreQueryResponse implements Serializable {
    private List<ItemItemCount> items;

    public StoreQueryResponse() {
    }

    public List<ItemItemCount> getItems() {
        return items;
    }

    public void setItems(List<ItemItemCount> items) {
        this.items = items;
    }
}
