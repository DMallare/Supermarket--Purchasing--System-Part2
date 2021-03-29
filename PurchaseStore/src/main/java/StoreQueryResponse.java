import java.io.Serializable;
import java.util.List;

/**
 * A class representing the results obtained from the query:
 * What are the top 10 most purchased items at store N?
 */
public class StoreQueryResponse implements Serializable {
    private List<PurchaseItem> items;

    public StoreQueryResponse() {
    }

    public List<PurchaseItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItem> stores) {
        this.items = items;
    }
}
