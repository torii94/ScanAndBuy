package ba.sum.fpmoz.shopitem;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import ba.sum.fpmoz.shopitem.model.Product;
import ba.sum.fpmoz.shopitem.model.Task;
import ba.sum.fpmoz.shopitem.model.User;


public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private TextView displayName;
    private ImageView profileImage;
    private TextView bonus;
    private DatabaseReference mDatabase;
    private Button shopNow;
    private TextView uuidTXT;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_profile, container,false);

        mDatabase= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        this.profileImage= rootView.findViewById(R.id.user_image);
        this.bonus=rootView.findViewById(R.id.txtBon);
        this.displayName=rootView.findViewById(R.id.display_nameTxt);
        this.uuidTXT=rootView.findViewById(R.id.uuidtxt);


        if (!mAuth.getCurrentUser().isAnonymous()) {
            this.displayName.setText(mAuth.getCurrentUser().getDisplayName());
            Picasso.get().load(mAuth.getCurrentUser().getPhotoUrl()).placeholder(R.drawable.ic_shopping_cart_black_24dp).resize(175, 175).transform(new CircleTransform()).into(profileImage);
            this.uuidTXT.setText(mAuth.getCurrentUser().getUid());
        }

        // displayName.setText(mAuth.getCurrentUser().getDisplayName());
        mDatabase.child("todos/users/"+mAuth.getCurrentUser().getUid().toString() ).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("usao", "usao u snapshot");

                User user= new User();
                user.bonus= Integer.parseInt(snapshot.child("bonus").getValue().toString());
                bonus.setText(String.valueOf(user.bonus)+" points");

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




    return rootView;
    }

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
