import java.io.Serializable;
import java.util.List;

/**
 * A class representing the results obtained from the query:
 * What are the top 5 stores for sales for item N?
 */
public class ItemQueryResponse implements Serializable {
    private List<StoreItem> stores;

    public ItemQueryResponse() {
    }

    public List<StoreItem> getStores() {
        return stores;
    }

    public void setStores(List<StoreItem> stores) {
        this.stores = stores;
    }
}
