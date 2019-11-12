package ba.sum.fpmoz.shopitem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

import ba.sum.fpmoz.shopitem.model.Product;

public class TaskListFragment extends Fragment {

    private View contactView;
    private RecyclerView myContactList;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUserID;
    ArrayList<Product> _Productist = new ArrayList<Product>();
    EditText inputSearch;
    private DatabaseReference mDatabaseSelected= FirebaseDatabase.getInstance().getReference().child("todos/tasks");



    public TaskListFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contactView=inflater.inflate(R.layout.fragment_task, container, false);

        myContactList = (RecyclerView)contactView.findViewById(R.id.task_list);
        GridLayoutManager layoutManager = new GridLayoutManager(contactView.getContext(), 2);
        myContactList.setLayoutManager(layoutManager);


        mAuth= FirebaseAuth.getInstance();
        Log.d("bazaa", mAuth.getCurrentUser().getUid().toString());
        try {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("todos/tasks/"+mAuth.getCurrentUser().getUid()
                    +"/product");
        }catch (NullPointerException ex){}


        return  contactView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(mDatabase, Product.class).build();

        FirebaseRecyclerAdapter<Product, ContactViewHolder> adapter =new FirebaseRecyclerAdapter<Product, ContactViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactViewHolder holder, int position, @NonNull Product model) {
                String userID= getRef(position).getKey();
                mDatabase.child(userID.toString());


                Log.d("probaa",userID.toString());
                Log.d("bazaa", mDatabase.getRef().toString());

                mDatabase.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            String profileImage = dataSnapshot.child("image").getValue().toString();
                            String profileusername = dataSnapshot.child("name").getValue().toString();

                            double profileprice = Double.parseDouble(dataSnapshot.child("price").getValue().toString());

                            holder.userName.setText(profileusername);
                            holder.userStatus.setText(String.format("%.2f", profileprice)+" KM");
                            holder.url=profileImage;
                            Picasso.get().load(profileImage).placeholder(R.drawable.ic_shopping_cart_black_24dp).into(holder.profileProduct);
                        }catch (NullPointerException ex){}


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.example_item,viewGroup,false);
                ContactViewHolder viewHolder = new ContactViewHolder(view);
                return  viewHolder;

            }
        };
        myContactList.setAdapter(adapter);
        adapter.startListening();
    }


        public  static  class ContactViewHolder extends RecyclerView.ViewHolder{

            TextView userName, userStatus;
            ImageView profileProduct;
            String url;

            public ContactViewHolder(@NonNull View itemView) {
                super(itemView);


                userName= itemView.findViewById(R.id.tetx_view_creator);
                userStatus = itemView.findViewById(R.id.text_view_likes);
                profileProduct= (ImageView)itemView.findViewById(R.id.image_view);
                url= "";


            }
        }


}
