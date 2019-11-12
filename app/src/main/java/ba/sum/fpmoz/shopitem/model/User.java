package ba.sum.fpmoz.shopitem.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String name;
    public String  lastName;
    public String image;
    public int bonus;


    public User() {

    }

    public User(String name, String lastName, String image, int bonus) {
        this.name = name;
        this.lastName = lastName;
        this.image = image;
        this.bonus=bonus;

    }
}
