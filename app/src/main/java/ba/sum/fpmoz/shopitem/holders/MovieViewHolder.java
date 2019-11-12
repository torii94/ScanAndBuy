package ba.sum.fpmoz.shopitem.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ba.sum.fpmoz.shopitem.R;
import ba.sum.fpmoz.shopitem.model.Product;
import ba.sum.fpmoz.shopitem.model.Task;


public class MovieViewHolder extends RecyclerView.ViewHolder {
    View mView;

    ImageView imageImg;
    TextView titleTxt;
    TextView descTxt;


    public MovieViewHolder(@NonNull View itemView) {
        super(itemView);
        this.mView = itemView;
        this.imageImg = itemView.findViewById(R.id.image_view);
        this.titleTxt = itemView.findViewById(R.id.tetx_view_creator);
        this.descTxt = itemView.findViewById(R.id.text_view_likes);
    }


    public void setMovie(Product modeli) {
        this.titleTxt.setText(modeli.name);
        this.descTxt.setText((int) modeli.price);
        Picasso.get().load(modeli.image).into(this.imageImg);
    }

    public void setTask(Task modeli) {
        for(Product p : modeli.getProduct()) {
            this.titleTxt.setText(p.name);
            this.descTxt.setText((int) p.price);
            Picasso.get().load(p.image).into(this.imageImg);
        }
    }
}