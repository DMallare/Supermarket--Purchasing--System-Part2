import java.io.Serializable;
import java.util.List;

public class PurchaseItems implements Serializable {
    private List<PurchaseItem> items;

    public PurchaseItems() { }

    public List<PurchaseItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "{" +
                "items=" + items +
                "}";
    }
}