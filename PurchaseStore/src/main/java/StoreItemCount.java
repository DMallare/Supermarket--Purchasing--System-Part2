import java.io.Serializable;

public class StoreItemCount implements Serializable {
    private int storeID;
    private int numberOfItems;

    public StoreItemCount() { }

    public StoreItemCount(int storeID, int numberOfItems) {
        this.storeID = storeID;
        this.numberOfItems = numberOfItems;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    @Override
    public String toString() {
        return "{" +
                "storeID: " + storeID +
                "numberOfItems: " + numberOfItems +
                "}";
    }
}
