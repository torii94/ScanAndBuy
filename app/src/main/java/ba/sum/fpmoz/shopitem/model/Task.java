package ba.sum.fpmoz.shopitem.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Task {
    ArrayList<Product> product ;
    public String userID;


    public Task() {

    }

    public Task(ArrayList<Product> product,String userID) {
        this.product = product;
        this.userID = userID;


    }

    public List<Product> getProduct() {
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
}
