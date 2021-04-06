import java.io.Serializable;

/**
 * A class used to model the data that will be sent in response to requests
 * that ask for the top N stores that sell the most of a specific item
 */
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
