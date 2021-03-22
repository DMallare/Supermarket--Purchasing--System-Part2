import java.io.Serializable;

public class Purchase implements Serializable {
    private int storeID;
    private int customerID;
    private String date;
    private String purchaseItems;

    public Purchase() { }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPurchaseItems() {
        return purchaseItems;
    }

    public void setPurchaseItems(String purchaseItems) {
        this.purchaseItems = purchaseItems;
    }
}
