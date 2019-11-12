package ba.sum.fpmoz.shopitem.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Calculus {
    public ArrayList<Product> product ;
    public String userID;
    public double totalPrice;



    public Calculus(ArrayList<Product> product, String userID, double totalPrice) {
        this.product = product;
        this.userID = userID;
        this.totalPrice = totalPrice;
    }

    public Calculus() {
    }

    public ArrayList<Product> getProduct() {
        return product;
    }

    public void setProduct(ArrayList<Product> product) {
        this.product = product;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
