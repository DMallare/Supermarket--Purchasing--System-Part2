import java.io.Serializable;

public class Query implements Serializable {
    private int n;
    private int id;
    private String type;

    public Query() { }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
