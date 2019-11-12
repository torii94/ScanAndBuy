package ba.sum.fpmoz.shopitem;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ba.sum.fpmoz.shopitem.holders.MovieViewHolder;
import ba.sum.fpmoz.shopitem.model.Product;
import ba.sum.fpmoz.shopitem.model.Task;

import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class MessageFragment extends  Fragment {

    private View contactView;
    private RecyclerView myContactList;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUserID;
    ArrayList<Product> _Productist = new ArrayList<Product>();
    EditText inputSearch;
    private DatabaseReference mDatabaseSelected= FirebaseDatabase.getInstance().getReference().child("todos/tasks");

    Task t= new Task();
    Product p = new Product();
    ArrayList<Product> _products = new ArrayList<Product>();







    public MessageFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contactView=inflater.inflate(R.layout.fragment_message, container, false);

        myContactList = (RecyclerView)contactView.findViewById(R.id.contact_list);
        GridLayoutManager layoutManager = new GridLayoutManager(contactView.getContext(), 2);
        myContactList.setLayoutManager(layoutManager);



        mAuth= FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("todos/products");
        setHasOptionsMenu(true);


        return  contactView;

    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions options= new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(mDatabase, Product.class).build();

       FirebaseRecyclerAdapter<Product, ContactViewHolder> adapter = new FirebaseRecyclerAdapter<Product, ContactViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactViewHolder holder, int position, @NonNull Product model) {
                String userID= getRef(position).getKey();



                mDatabase.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {

                        try {

                            String profileImage = dataSnapshot.child("image").getValue().toString();
                            String profileusername = dataSnapshot.child("name").getValue().toString();

                            String profileprice = dataSnapshot.child("price").getValue().toString();

                            holder.userName.setText(profileusername);
                            holder.userStatus.setText(profileprice);
                            holder.url = profileImage;
                            Picasso.get().load(profileImage).placeholder(R.drawable.ic_shopping_cart_black_24dp).into(holder.profileProduct);

                        }catch (NullPointerException ex){}

                        //   _Productist.add((Product) dataSnapshot.getChildren() );
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
                final ContactViewHolder viewHolder = new ContactViewHolder(view);


                t.setUserID("nesto drugo");
                final String _id = UUID.randomUUID().toString();
                mDatabaseSelected.child(_id);

                //viewHolder.userStatus.setText(_Productist.get(i).name);

//
//                Log.d("tonisaa",_Productist.get(i).name.toString());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {




                        p.name= viewHolder.userName.getText().toString();
                        p.active=true;
                        p.price=Double.parseDouble(viewHolder.userStatus.getText().toString());
                        p.count=1;
                        p.image=viewHolder.url;

                       // _products.add(p);

                        AddTaskList(p, mDatabaseSelected);
                        //t.setProduct(_products);

                       // p.image= viewHolder.profileProduct.getimage





                    }
                });


                return  viewHolder;

            }

        };
        myContactList.setAdapter(adapter);
        adapter.startListening();
    }

    private void AddTaskList(Product p, DatabaseReference data){

        Product product= new Product();

        product.image=p.image;
        product.count=p.count;
        product.price=p.price;
        product.active=p.active;
        product.name=p.name;

        _products.add(product);
         t.setProduct(_products);

         data.setValue(t);

            Log.d("tonisaa",p.name);



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

    private void firebaseSearch(String searchText){

        mDatabase= FirebaseDatabase.getInstance().getReference().child("todos/products");
        Query query = mDatabase.orderByChild("name").startAt(searchText).endAt(searchText+"\uf8ff");

        FirebaseRecyclerOptions options= new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class).build();

        FirebaseRecyclerAdapter<Product, ContactViewHolder> adapter = new FirebaseRecyclerAdapter<Product, ContactViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactViewHolder holder, int position, @NonNull Product model) {
                String userID= getRef(position).getKey();



                mDatabase.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {


                        String profileImage = dataSnapshot.child("image").getValue().toString();
                        String profileusername = dataSnapshot.child("name").getValue().toString();

                        String profileprice = dataSnapshot.child("price").getValue().toString();

                        holder.userName.setText(profileusername);
                        holder.userStatus.setText(profileprice);
                        holder.url=profileImage;

                        Picasso.get().load(profileImage).placeholder(R.drawable.ic_shopping_cart_black_24dp).into(holder.profileProduct);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.example_item,viewGroup,false);
                final ContactViewHolder viewHolder = new ContactViewHolder(view);


                t.setUserID("nesto drugo");
                String _id = UUID.randomUUID().toString();
                mDatabaseSelected.child(_id);

                //viewHolder.userStatus.setText(_Productist.get(i).name);

//
//                Log.d("tonisaa",_Productist.get(i).name.toString());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        p.name= viewHolder.userName.getText().toString();
                        p.active=true;
                        p.price=Double.parseDouble(viewHolder.userStatus.getText().toString());
                        p.count=1;
                        p.image=viewHolder.url;

                        // _products.add(p);

                        AddTaskList(p, mDatabaseSelected);
                        // p.image= viewHolder.profileProduct.getimage


                        //Log.d("tonisa", p.image.toString());

                    }
                });








                return  viewHolder;

            }





        };
        myContactList.setAdapter(adapter);
        adapter.startListening();
    }
    /*
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Product user = singleSnapshot.getValue(Product.class);
                    Log.d("gre",user.name);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        */


    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.scan_menu, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) search.getActionView();

        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        if (mSearchView != null) {
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            mSearchView.setIconifiedByDefault(true);
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("welll", " this worked");
                    firebaseSearch(newText);
                    return false;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                // do stuff, like showing settings fragment
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
