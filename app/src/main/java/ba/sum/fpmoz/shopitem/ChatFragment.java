package ba.sum.fpmoz.shopitem;

import ba.sum.fpmoz.shopitem.holders.MovieViewHolder;
import ba.sum.fpmoz.shopitem.R;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

import ba.sum.fpmoz.shopitem.holders.MovieViewHolder;
import ba.sum.fpmoz.shopitem.model.Product;

public class ChatFragment extends Fragment {

    private View contactView;
    private RecyclerView myContactList;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUserID;
    ArrayList<Product> _Productist = new ArrayList<Product>();
    private String _selectedProduct="-LVmVe1eiN9pBLRgKkhY";



    public ChatFragment() {

    }


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

              View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

                //contactView=inflater.inflate(R.layout.fragment_message, container, false);

                myContactList = (RecyclerView)rootView.findViewById(R.id.contact_list_chat);
                myContactList.setLayoutManager(new LinearLayoutManager(getContext()));


                mAuth= FirebaseAuth.getInstance();
                mDatabase= FirebaseDatabase.getInstance().getReference();



            mAuth= FirebaseAuth.getInstance();
           // mDatabase= FirebaseDatabase.getInstance().getReference().child("todos/products/"+_selectedProduct);


            Button buttonBarCodeScan = rootView.findViewById(R.id.btn_scan);
            buttonBarCodeScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //initiate scan with our custom scan activity
                    //new IntentIntegrator(getActivity()).setCaptureActivity(ScannerActivity.class).initiateScan();
                    IntentIntegrator.forSupportFragment(ChatFragment.this).setCaptureActivity(ScannerActivity.class).initiateScan();
                    getProduct(_selectedProduct);
                }
            });


                return rootView;
        }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String barcode = result.getContents();
        _selectedProduct=barcode.substring(7, barcode.length());
        Log.d("aaa", _selectedProduct);
    }


    public void getProduct(final String key) {
        //super.onStart();

        FirebaseRecyclerOptions options= new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(mDatabase, Product.class).build();

        FirebaseRecyclerAdapter<Product, MessageFragment.ContactViewHolder> adapter = new FirebaseRecyclerAdapter<Product, MessageFragment.ContactViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MessageFragment.ContactViewHolder holder, int position, @NonNull Product model) {
                //String userID= getRef(position).getKey();



                mDatabase.child("todos/products/"+key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {


                        String profileImage = dataSnapshot.child("image").getValue().toString();
                        String profileusername = dataSnapshot.child("name").getValue().toString();

                        String profileprice = dataSnapshot.child("price").getValue().toString();

                        holder.userName.setText(profileusername);
                        holder.userStatus.setText(profileprice);
                        Picasso.get().load(profileImage).placeholder(R.drawable.ic_shopping_cart_black_24dp).into(holder.profileProduct);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public MessageFragment.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.example_item,viewGroup,false);
                MessageFragment.ContactViewHolder viewHolder = new MessageFragment.ContactViewHolder(view);
                return  viewHolder;

            }

        };


            myContactList.setAdapter(adapter);
            Log.d("aaaa", String.valueOf(myContactList));



            adapter.startListening();

    }

    public  static  class ContactViewHolder extends RecyclerView.ViewHolder{

        TextView userName, userStatus;
        ImageView profileProduct;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);


            userName= itemView.findViewById(R.id.tetx_view_creator);
            userStatus = itemView.findViewById(R.id.text_view_likes);
            profileProduct= (ImageView)itemView.findViewById(R.id.image_view);


        }
    }


}

