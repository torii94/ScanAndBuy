package ba.sum.fpmoz.shopitem.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Product {

    public String name;
    public double price;
    public String  image;
    public int count;
    public boolean active;


    public Product() {

    }

    public Product(String name, double price, String image, int count, boolean active) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.count=count;
        this.active=active;
    }
}
