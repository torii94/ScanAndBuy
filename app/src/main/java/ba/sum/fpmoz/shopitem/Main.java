package ba.sum.fpmoz.shopitem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.View;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import ba.sum.fpmoz.shopitem.model.User;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private TextView displayName;
    private ImageView profileImage;
    private TextView bonus;
    private DatabaseReference mDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.displayName= findViewById(R.id.nav_user_display_name);
       // bonus= findViewById(R.id.nav_bonus);


        //mDatabase=FirebaseDatabase.getInstance().getReference().child("todos/users/" + mAuth.getCurrentUser().getUid());


    //   FirebaseUser currentUser = mAuth.getCurrentUser();

       // Log.d("gmailuser", currentUser.getDisplayName());
        //Log.d("gmailuser", currentUser.getProviderId());
        //Log.d("gmailuser", currentUser.getMetadata().toString());


        //Log.d("gmailuser", currentUser.getUid());
        //Log.d("gmailuser", String.valueOf(currentUser.getPhotoUrl()));






        drawer= findViewById(R.id.drawer_layout);

        NavigationView navigationView= findViewById(R.id.naw_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        mAuth = FirebaseAuth.getInstance();
        android.view.View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.nav_user_display_name);
        this.profileImage= hView.findViewById(R.id.nav_user_photo);
        this.bonus=hView.findViewById(R.id.nav_bonus);

        mDatabase= FirebaseDatabase.getInstance().getReference();

        if(!mAuth.getCurrentUser().isAnonymous()) {

            //Log.d("mailcheck", mAuth.getCurrentUser().getEmail());
            nav_user.setText(mAuth.getCurrentUser().getDisplayName());
            Log.d("mailcheck", mAuth.getCurrentUser().getDisplayName());

            Picasso.get().load(mAuth.getCurrentUser().getPhotoUrl()).placeholder(R.drawable.ic_shopping_cart_black_24dp).resize(175, 175).transform(new CircleTransform()).into(profileImage);


            // displayName.setText(mAuth.getCurrentUser().getDisplayName());
            mDatabase.child("todos/users/" + mAuth.getCurrentUser().getUid().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    User user = new User();
                    user.bonus = Integer.parseInt(snapshot.child("bonus").getValue().toString());
                    bonus.setText(String.valueOf(user.bonus));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }


        if(savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ScannerFragment()).commit();

            navigationView.setCheckedItem(R.id.id_shop);
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.id_shop:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ScannerFragment()).commit();
                break;
            case R.id.id_task:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TaskListFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
            case R.id.id_proizvodi:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AllProductsFragment()).commit();
                break;
            case R.id.id_akcija:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ActionProductsFragment()).commit();
                break;
            case R.id.id_logout:
                mAuth.signOut();


                Intent intent= new Intent(Main.this, Login.class);
                startActivity(intent);
                finish();

                break;


        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    OnCompleteListener<AuthResult> completeListener = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                Log.d("Userpostojimain", "ne postoji");
            }
        }
    };


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
