import java.io.Serializable;

/**
 * A class used to model the data that will be sent in response to requests
 * that ask for the top N items that are purchased at a specific store
 */
public class ItemItemCount implements Serializable {
    private int itemID;
    private int numberOfItems;

    public ItemItemCount() { }

    public ItemItemCount(int itemID, int numberOfItems) {
        this.itemID = itemID;
        this.numberOfItems = numberOfItems;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
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
                "ItemID: " + itemID +
                "numberOfItems: " + numberOfItems +
                "}";
    }
}
