import java.util.Date;

public class Product {
    private int uk_id_goods;
    private String name_goods;
    private double price_goods;
    private Date gmt_modified;
    private Date gmt_create;

    public Product(int uk_id_goods, String name_goods, double price_goods, Date gmt_modified, Date gmt_create) {
        this.uk_id_goods = uk_id_goods;
        this.name_goods = name_goods;
        this.price_goods = price_goods;
        this.gmt_modified = gmt_modified;
        this.gmt_create = gmt_create;
    }

    public int getUk_id_goods() {
        return uk_id_goods;
    }

    public void setUk_id_goods(int uk_id_goods) {
        this.uk_id_goods = uk_id_goods;
    }

    public String getName_goods() {
        return name_goods;
    }

    public void setName_goods(String name_goods) {
        this.name_goods = name_goods;
    }

    public double getPrice_goods() {
        return price_goods;
    }

    public void setPrice_goods(double price_goods) {
        this.price_goods = price_goods;
    }

    public Date getGmt_modified() {
        return gmt_modified;
    }

    public void setGmt_modified(Date gmt_modified) {
        this.gmt_modified = gmt_modified;
    }

    public Date getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(Date gmt_create) {
        this.gmt_create = gmt_create;
    }

    @Override
    public String toString() {
        return "Product{" +
                "uk_id_goods=" + uk_id_goods +
                ", name_goods='" + name_goods + '\'' +
                ", price_goods=" + price_goods +
                ", gmt_modified=" + gmt_modified +
                ", gmt_create=" + gmt_create +
                '}';
    }
}