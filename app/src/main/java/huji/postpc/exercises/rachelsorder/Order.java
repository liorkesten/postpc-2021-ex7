package huji.postpc.exercises.rachelsorder;

import java.util.UUID;

public class Order {
    private final String id;
    private String customerName;
    private int pickles;
    private boolean hummus;
    private boolean tahini;
    private String comment;
    private String status; //TODO Change it to enum.

    public Order() {
        id = UUID.randomUUID().toString();
        customerName = "empty";
    }

    /**
     * Cto'r for order that copy all fields expect the id.
     *
     * @param orderToCopy
     */
    public Order(Order orderToCopy) {
        this.id = orderToCopy.id;
        this.customerName = orderToCopy.customerName;
        this.comment = orderToCopy.comment;
        this.pickles = orderToCopy.pickles;
        this.hummus = orderToCopy.hummus;
        this.tahini = orderToCopy.tahini;
        this.status = orderToCopy.status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", customerName='" + customerName + '\'' +
                ", pickles=" + pickles +
                ", hummus=" + hummus +
                ", tahini=" + tahini +
                ", Comment='" + comment + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public Order(String customerName, int pickles, boolean hummus, boolean tahini, String comment, String status) {
        this.id = UUID.randomUUID().toString();
        this.customerName = customerName;
        this.pickles = pickles;
        this.hummus = hummus;
        this.tahini = tahini;
        this.comment = comment;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getPickles() {
        return pickles;
    }

    public void setPickles(int pickles) {
        this.pickles = pickles;
    }

    public boolean isHummus() {
        return hummus;
    }

    public void setHummus(boolean hummus) {
        this.hummus = hummus;
    }

    public boolean isTahini() {
        return tahini;
    }

    public void setTahini(boolean tahini) {
        this.tahini = tahini;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
