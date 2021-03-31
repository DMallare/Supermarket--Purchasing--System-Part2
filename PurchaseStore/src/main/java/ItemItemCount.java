import java.io.Serializable;

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
