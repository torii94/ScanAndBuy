package ba.sum.fpmoz.shopitem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Intent intent= getIntent();
        //String imageURL= intent.getStringExtra(EXTRA_URL);
        //String creatorName= intent.getStringExtra((EXTRA_CRATOR));
        //int likeCount = intent.getIntExtra(EXTRA_LIKES,0);

       // ImageView imageView= findViewById(R.id.iamge_view_detail);
        //TextView textViewCreator= findViewById(R.id.text_view_creator_detail);
        //TextView textViewLike= findViewById(R.id.text_view_like_detail);

        //Picasso.get().load(imageURL).fit().centerInside().into(imageView);
        //textViewCreator.setText(creatorName);
        //textViewLike.setText("Likes "+likeCount);



    }
}
