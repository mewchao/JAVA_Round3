package src.main.com.example.model;

import java.util.Date;

public class Order {
    private int id_order;
    private int id_good;
    private double prices_order;
    private int nums_good;
    private Date time_order;
    private Date gmt_create;
    private Date gmt_modified;

    public Order(int id_order, int id_good, double prices_order, int nums_good, Date time_order, Date gmt_create, Date gmt_modified) {
        this.id_order = id_order;
        this.id_good = id_good;
        this.prices_order = prices_order;
        this.nums_good = nums_good;
        this.time_order = time_order;
        this.gmt_create = gmt_create;
        this.gmt_modified = gmt_modified;
    }

    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    public int getId_good() {
        return id_good;
    }

    public void setId_good(int id_good) {
        this.id_good = id_good;
    }

    public double getPrices_order() {
        return prices_order;
    }

    public void setPrices_order(double prices_order) {
        this.prices_order = prices_order;
    }

    public int getNums_good() {
        return nums_good;
    }

    public void setNums_good(int nums_good) {
        this.nums_good = nums_good;
    }

    public Date getTime_order() {
        return time_order;
    }

    public void setTime_order(Date time_order) {
        this.time_order = time_order;
    }

    public Date getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(Date gmt_create) {
        this.gmt_create = gmt_create;
    }

    public Date getGmt_modified() {
        return gmt_modified;
    }

    public void setGmt_modified(Date gmt_modified) {
        this.gmt_modified = gmt_modified;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id_order=" + id_order +
                ", id_good=" + id_good +
                ", prices_order=" + prices_order +
                ", nums_good=" + nums_good +
                ", time_order=" + time_order +
                ", gmt_create=" + gmt_create +
                ", gmt_modified=" + gmt_modified +
                '}';
    }
}