import java.io.Serializable;

public class TopTenItemsForStoreRequest implements Serializable {
    private int n;
    private int storeId;

    public TopTenItemsForStoreRequest() { }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
