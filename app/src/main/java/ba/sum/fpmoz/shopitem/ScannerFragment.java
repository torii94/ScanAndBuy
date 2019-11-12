package ba.sum.fpmoz.shopitem;

import android.animation.Animator;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

import ba.sum.fpmoz.shopitem.model.Calculus;
import ba.sum.fpmoz.shopitem.model.Product;
import ba.sum.fpmoz.shopitem.model.Task;
import ba.sum.fpmoz.shopitem.model.User;

public class ScannerFragment extends Fragment {

    private View contactView;
    private RecyclerView myContactList;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseBill;
    private DatabaseReference mDatabaseUser;
    private DatabaseReference mDatabaseCount;
    private DatabaseReference mDatabaseRem;




    private FirebaseAuth mAuth;
    private String currentUserID;
    ArrayList<Product> _Productist = new ArrayList<Product>();
    private String _selectedProduct;
    Task t= new  Task();
    Product p = new Product();
    ArrayList<Product> _products = new ArrayList<Product>();
    private DatabaseReference mDatabaseSelected;
    Calculus calculus=new Calculus();
    TextView textViewTotal;
    double totalprice=0.0;
    Integer num;
    private ImageView plus;
    private ImageView minus;
    private TextView countTxt;
    Integer counts;
    boolean scanMenu=true;
    boolean validateMenu;
    FirebaseRecyclerAdapter<Product, ScannerFragment.ContactViewHolder> adapter;
    FloatingActionButton fab;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab3;
    boolean isFABOpen=false;
    int totalbonus;
    int uBonus;
    User userBonus= new User();
    LinearLayout fabLayout1;
    LinearLayout fabLayout3;
    boolean useBonus=false;








    public ScannerFragment() {

    }


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

              View rootView = inflater.inflate(R.layout.fragment_scanner, container, false);

              setHasOptionsMenu(true);



                myContactList = (RecyclerView)rootView.findViewById(R.id.contact_list_chat);
                myContactList.setLayoutManager(new LinearLayoutManager(getContext()));


                mAuth= FirebaseAuth.getInstance();
                mDatabase= FirebaseDatabase.getInstance().getReference();
                mDatabaseBill= FirebaseDatabase.getInstance().getReference();
                mDatabaseRem= FirebaseDatabase.getInstance().getReference();

            calculus.setUserID("nesto drugo");
                final String _id = UUID.randomUUID().toString();
                //mDatabaseSelected.child(_id);
            try {
                mDatabaseSelected=FirebaseDatabase.getInstance().getReference().child("todos/calculus/"+mAuth.getCurrentUser().getUid());
            }catch (Exception ex){}



                this.textViewTotal = rootView.findViewById(R.id.totalPriceTxt);

                this.countTxt= rootView.findViewById(R.id.textView5);


            mAuth= FirebaseAuth.getInstance();
           // mDatabase= FirebaseDatabase.getInstance().getReference().child("todos/products/"+_selectedProduct);




            //textViewTotal.setText(String.valueOf(totalprice));
            //countTxt.setText("1");

            textViewTotal.setText(String.format("%.2f", totalprice)+" KM");
            GetBonus();


            fabLayout1= (LinearLayout)rootView.findViewById(R.id.fabLayout1);
            fabLayout3= (LinearLayout)rootView.findViewById(R.id.fabLayout3);

            fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            fab1 = (FloatingActionButton)  rootView.findViewById(R.id.fab1);
            fab3 = (FloatingActionButton)  rootView.findViewById(R.id.fab3);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isFABOpen){
                        showFABMenu();
                    }else{
                        closeFABMenu();
                    }
                }
            });

            fab3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!useBonus) {
                        Calculate(uBonus);


                        try {
                            mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("todos/users/" + mAuth.getCurrentUser().getUid());

                        } catch (NullPointerException ex) {
                        }

                        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                userBonus.bonus = Integer.parseInt(dataSnapshot.child("bonus").getValue().toString());

                                //Calculate(userBonus.bonus);


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });

                    }
                    useBonus=true;
                }
            });

            fab1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetBonus();
                    Toast.makeText(getContext(), String.valueOf(uBonus*0.1)+ " KM", Toast.LENGTH_SHORT).show();

                }
            });


            return rootView;
        }


    private void showFABMenu(){
        isFABOpen=true;
        fabLayout1.setVisibility(View.VISIBLE);
        //  fabLayout2.setVisibility(View.VISIBLE);
        fabLayout3.setVisibility(View.VISIBLE);
        // fabBGLayout.setVisibility(View.VISIBLE);

        fab.animate().rotationBy(180).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                if (fab.getRotation() != 180) {
                    fab.setRotation(180);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        fabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        // fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fabLayout3.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        //fabLayout3.animate().translationY(-getResources().getDimension(R.dimen.standard_165));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        // fabBGLayout.setVisibility(View.GONE);
        fab.animate().rotationBy(-180);
        fabLayout1.animate().translationY(0);
//        fabLayout2.animate().translationY(0);
        fabLayout3.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!isFABOpen){
                    fabLayout1.setVisibility(View.GONE);
                    // fabLayout2.setVisibility(View.GONE);
                    fabLayout3.setVisibility(View.GONE);
                }
                if (fab.getRotation() != -180) {
                    fab.setRotation(-180);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Log.d("validatee", String.valueOf(num));

        Log.d("validatee", String.valueOf(scanMenu));


        if(scanMenu == true){
            scanMenu=true;



                String barcode = result.getContents();
                _selectedProduct = barcode.substring(0, barcode.length());
                Log.d("aaa", _selectedProduct);
                getProduct(_selectedProduct);
                //firebaseSearch(_selectedProduct);
                //textViewTotal.setText(String.valueOf(totalprice));
            textViewTotal.setText(String.format("%.2f", totalprice)+" KM");

                Log.d("validatee", "Prvi");


                GetBill();

        }
        if(!scanMenu){
            scanMenu=false;

            String barcode = result.getContents();

            _selectedProduct = barcode.substring(0, barcode.length());
            getValidate(_selectedProduct);
            Log.d("validatee", "Drugi");

        }
        else {
            return;
        }
        }catch (NullPointerException ex){}






    }


    public void getProduct(final String key) {

        try {


            mDatabase.child("todos/products/" + key.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Log.d("usao", "usao u snapshot");

                    Product product = new Product();
                    product.name = snapshot.child("name").getValue().toString();
                    product.price = (double) snapshot.child("price").getValue();
                    product.image = snapshot.child("image").getValue().toString();
                    //product.count= Integer.parseInt(snapshot.child("count").getValue().toString());

                    product.count = 1;
                    totalprice += product.price;

                    _products.add(product);

                    //AddTaskList(mDatabaseSelected);

                    calculus.setProduct(_products);

                    Log.d("usao", "Usao sam u dodavanje");

                    //Log.d("tonisaaa", String.valueOf(totalprice));


                    calculus.setTotalPrice(totalprice);
                    mDatabaseSelected.setValue(calculus);
                    //textViewTotal.setText(String.valueOf(totalprice));
                    textViewTotal.setText(String.format("%.2f", totalprice)+" KM");


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }catch (Exception ex){ Toast.makeText(getContext(), "Product doesn't exist! ", Toast.LENGTH_LONG).show();}



//        Adapter adapter = (Adapter) _products;
   //     myContactList.setAdapter((RecyclerView.Adapter) adapter);

    }

    private void firebaseSearch(final String key){


        mDatabase= FirebaseDatabase.getInstance().getReference().child("todos/products/"+key.toString());

        FirebaseRecyclerOptions options= new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(mDatabase, Product.class).build();

        adapter = new FirebaseRecyclerAdapter<Product, ScannerFragment.ContactViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ScannerFragment.ContactViewHolder holder, int position, @NonNull Product model) {
                String userID= getRef(position).getKey();
                Log.d("bazap", mDatabase.getRef().toString());

                mDatabase.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot snapshot) {


                        Product product= new Product();
                        product.name= snapshot.child("name").getValue().toString();
                        product.price= (double) snapshot.child("price").getValue();
                        product.image=snapshot.child("image").getValue().toString();
                        //product.count= Integer.parseInt(snapshot.child("count").getValue().toString());
                        Log.d("bazap", product.name.toString());


                        product.count=1;
                        totalprice+=product.price;

                        _products.add(product);

                        //AddTaskList(mDatabaseSelected);

                        calculus.setProduct(_products);

                        Log.d("usao","Usao sam u dodavanje");

                        //Log.d("tonisaaa", String.valueOf(totalprice));


                        calculus.setTotalPrice(totalprice);
                       // mDatabaseSelected.setValue(calculus);
                        //textViewTotal.setText(String.valueOf(totalprice));
                        textViewTotal.setText(String.format("%.2f", totalprice)+" KM");



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ScannerFragment.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.example_scan_item,viewGroup,false);
                final ScannerFragment.ContactViewHolder viewHolder = new ScannerFragment.ContactViewHolder(view);

                return  viewHolder;

            }

        };
        myContactList.setAdapter(adapter);
        adapter.startListening();
    }



    public void getValidate(final String key) {
        try {
            GetBonus();


            mDatabase.child("todos/sale/" + key.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Log.d("usao", "usao u snapshot");

                    try {
                    Calculus cal = new Calculus();



                    cal.totalPrice = (double) snapshot.child("totalPrice").getValue();

                    Log.d("usao", "Usao sam u dodavanje");

                    //Log.d("tonisaaa", String.valueOf(totalprice));
                    Log.d("pricee", String.valueOf(totalprice));
                    Log.d("pricee", String.valueOf(cal.totalPrice));


                    if (cal.totalPrice >= totalprice - 1) {

                        User user = new User();

                        try {
                            mDatabaseBill = FirebaseDatabase.getInstance().getReference().child("todos/calculus/" + mAuth.getCurrentUser().getUid() + "/product");

                        } catch (NullPointerException ex) {
                        }

                        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("todos/users/" + mAuth.getCurrentUser().getUid());
                        Log.d("istii", mDatabaseUser.getRef().toString());


                        int b = SetBonus(totalprice) + uBonus;

                        Log.d("bonuss", String.valueOf(b));
                        Log.d("bonuss", String.valueOf(uBonus));
                        Log.d("bonuss", String.valueOf(SetBonus(totalprice)));

                        mDatabaseUser.child("bonus").setValue(b);

                        Log.d("istii", mDatabaseBill.getRef().toString());

                        _products.clear();
                        mDatabaseBill.removeValue();
                        GetBill();
                        Toast.makeText(getContext(), "Successful, Thank you!", Toast.LENGTH_LONG).show();
                        totalprice = 0.00;
                        textViewTotal.setText(String.format("%.2f", totalprice)+" KM");
                        GetBonus();
                    } else {
                        Toast.makeText(getContext(), "Validation unsuccessful. please try again!", Toast.LENGTH_LONG).show();
                    }

                }catch (NullPointerException exe){Toast.makeText(getContext(), "Wrong QR!", Toast.LENGTH_LONG).show();}


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }catch (Exception ex){Toast.makeText(getContext(), "wrong qr code!", Toast.LENGTH_LONG).show();}
    }


        public  static  class ContactViewHolder extends RecyclerView.ViewHolder{

        TextView userName, userStatus,countTxt;
        ImageView profileProduct;
        String url;
        Button remove;
        ImageView plus;
        ImageView minus;
        Integer count;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);


            userName= itemView.findViewById(R.id.tetx_view_creator);
            userStatus = itemView.findViewById(R.id.text_view_likes);
            profileProduct= (ImageView)itemView.findViewById(R.id.image_view);
            remove= itemView.findViewById(R.id.button2);
            plus=itemView.findViewById(R.id.plusImage);
            minus=itemView.findViewById(R.id.minusImage);
            countTxt=itemView.findViewById(R.id.textView5);
            url= "";
            count=1;


        }
    }

    private void GetBill(){

        int posRemove;

        try {
            mDatabaseBill= FirebaseDatabase.getInstance().getReference().child("todos/calculus/"+mAuth.getCurrentUser().getUid()+"/product");

        }catch (NullPointerException ex){}

        FirebaseRecyclerOptions options= new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(mDatabaseBill, Product.class).build();

           adapter = new FirebaseRecyclerAdapter<Product, ScannerFragment.ContactViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ScannerFragment.ContactViewHolder holder, int position, @NonNull Product model) {


                     String userID = getRef(position).getKey();

                    Log.d("keyys", userID.toString());


                mDatabaseBill.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {

                        try {

                            String profileImage = dataSnapshot.child("image").getValue().toString();
                            String profileusername = dataSnapshot.child("name").getValue().toString();

                            double profileprice = Double.parseDouble(dataSnapshot.child("price").getValue().toString());
                            String cnt=  dataSnapshot.child("count").getValue().toString();

                            Log.d("cnt", cnt.toString());


                            holder.userName.setText(profileusername);
                            holder.userStatus.setText(String.format("%.2f", profileprice)+" KM");
                            holder.url = profileImage;
                            holder.count= Integer.valueOf(cnt);
                            holder.countTxt.setText(cnt);

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
            public ScannerFragment.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.example_scan_item,viewGroup,false);
                final ScannerFragment.ContactViewHolder viewHolder = new ScannerFragment.ContactViewHolder(view);

                viewHolder.plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            mDatabaseCount= FirebaseDatabase.getInstance().getReference().child("todos/calculus/"+mAuth.getCurrentUser().getUid()+"/product");

                        }catch (NullPointerException ex){}


                        mDatabaseCount.child(String.valueOf(viewHolder.getAdapterPosition())).child("count").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {


                                    String key = adapter.getRef(viewHolder.getAdapterPosition()).getKey();


                                    counts = Integer.valueOf(dataSnapshot.getValue().toString());

                                    Log.d("countss", counts.toString());

                                    int broj = counts + 1;

                                    mDatabaseCount.child(String.valueOf(key) + "/count").setValue(broj);
                                    Product p = new Product();
                                    Log.d("probaa", String.valueOf(viewHolder.getLayoutPosition()));
                                    p = _products.get(viewHolder.getLayoutPosition());
                                    p.count = broj;
                                    Log.d("korisnik", p.name + " " + String.valueOf(p.price) + p.image);


                                    _products.indexOf(p);
                                    for (Product pr : _products) {
                                        Log.d("korisnikLista", pr.name + " " + String.valueOf(pr.price) + pr.image);

                                    }

                                    viewHolder.countTxt.setText(String.valueOf(broj));
                                    totalprice += p.price;
                                    mDatabaseSelected.child("totalPrice").setValue(totalprice);
                                    //Log.d("totalprice", String.valueOf(mDatabaseSelected.getRef()));


                                    //textViewTotal.setText(String.valueOf(totalprice));
                                    textViewTotal.setText(String.format("%.2f", totalprice)+" KM");

                                }catch (NullPointerException ex){ }
                                GetBill();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });




                    }
                });
                viewHolder.minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            mDatabaseCount= FirebaseDatabase.getInstance().getReference().child("todos/calculus/"+mAuth.getCurrentUser().getUid()+"/product");

                        }catch (NullPointerException ex){}

                        mDatabaseCount.child(String.valueOf(viewHolder.getAdapterPosition())).child("count").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String key = adapter.getRef(viewHolder.getAdapterPosition()).getKey();

                                counts= Integer.valueOf(dataSnapshot.getValue().toString());
                                Log.d("countss", counts.toString());

                                int broj= counts-1;

                                mDatabaseCount.child(String.valueOf(key)+"/count").setValue(broj);
                                Product p= new Product();
                                Log.d("probaa", String.valueOf(key));
                                p=_products.get(viewHolder.getLayoutPosition());
                                p.count=broj;
                                Log.d("korisnik", p.name+" "+String.valueOf(p.price)+p.image);


                                _products.indexOf(p);

                                viewHolder.countTxt.setText(String.valueOf(broj));
                                totalprice-= p.price;
                                textViewTotal.setText(String.format("%.2f", totalprice)+" KM");
                                mDatabaseSelected.child("totalPrice").setValue(totalprice);


                                GetBill();


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });




                    }
                });

                viewHolder.remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // notifyDataSetChanged();
                        try {
                            mDatabaseCount= FirebaseDatabase.getInstance().getReference().child("todos/calculus/"+mAuth.getCurrentUser().getUid()+"/product");

                        }catch (NullPointerException ex){}

                       // int userID= Integer.parseInt(getRef(1).getKey());
                        //Log.d("idlist", String.valueOf(userID));
                        String key = adapter.getRef(viewHolder.getAdapterPosition()).getKey();

                        Log.d("idlist", (key));



                        try {
                            mDatabaseRem= FirebaseDatabase.getInstance().getReference().child("todos/calculus/"+mAuth.getCurrentUser().getUid());

                        }catch (NullPointerException ex){}
                        //DatabaseCount.child(String.valueOf(key));

                        Product p= new Product();
                      //  Log.d("probaa", String.valueOf((int) viewHolder.getItemId()));
                        p=_products.get(viewHolder.getLayoutPosition());
                        Log.d("probaaa", mDatabaseCount.getRef().toString());


                        Log.d("probaaa", String.valueOf(v.getId()));
                        Log.d("probaaa", String.valueOf(viewHolder.itemView.getId()));
                        Log.d("probaaa", String.valueOf(viewHolder.getOldPosition()));

                        RemoveList(p,viewHolder,mDatabaseCount);


                        _products.indexOf(p);

                       mDatabaseCount.child(String.valueOf(key)).removeValue();
                         // Log.d("probaaa", mDatabaseCount.getRef().toString());


                        totalprice= totalprice - (p.price* p.count);
                        //textViewTotal.setText(String.valueOf(totalprice));
                        textViewTotal.setText(String.format("%.2f", totalprice)+" KM");
                        mDatabaseRem.child("totalPrice").setValue(totalprice);



                        //  _products.remove(viewHolder.getLayoutPosition());

                        _products.remove(p);


                       // notifyItemRemoved(viewHolder.getAdapterPosition());
                        //notifyItemRangeChanged(viewHolder.getAdapterPosition(),_products.size());

                        GetBill();

                        Calculus calculus= new Calculus();

                    }
                });

                return  viewHolder;

            }


        };
        myContactList.setAdapter(adapter);
        adapter.startListening();
    }


    private void RemoveList(Product product, RecyclerView.ViewHolder viewHolder,DatabaseReference databaseReference){

        Log.d("probametoda", String.valueOf(viewHolder.getLayoutPosition()));
        Log.d("probametoda", String.valueOf(viewHolder.getItemId()));
        Log.d("probametoda", String.valueOf(viewHolder.getOldPosition()));

    }

    private void Calculate(int bonus){
        try {
            mDatabaseRem= FirebaseDatabase.getInstance().getReference().child("todos/calculus/"+mAuth.getCurrentUser().getUid());

        }catch (NullPointerException ex){}


        totalprice -= (bonus*0.1);
        textViewTotal.setText(String.format("%.2f", totalprice)+" KM");
        mDatabaseRem.child("totalPrice").setValue(totalprice);

    }


    private void GetBonus(){
        try {
            mDatabaseUser= FirebaseDatabase.getInstance().getReference().child("todos/users/"+mAuth.getCurrentUser().getUid());

        }catch (NullPointerException ex){}

        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userBonus.bonus= Integer.parseInt(dataSnapshot.child("bonus").getValue().toString());
                uBonus=userBonus.bonus;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    private int SetBonus(double price){
        totalbonus=0;

        if(price > 3 && price <10){
            totalbonus=1;
        }
        if(price >= 10 && price <30){
            totalbonus=10;
        }
        if(price >= 30 && price <60){
            totalbonus=30;
        }
        if(price >= 60 && price <100){
            totalbonus=60;
        }
        if(price >= 100 && price <250){
            totalbonus=100;
        }
        if(price >= 250){
            totalbonus=250;
        }

        return totalbonus;


    }

    private void AddTaskList(DatabaseReference databaseReference){

      //  Product product= new Product();

        //t.setProduct(_products);

        // textViewTotal.findViewById(R.id.totalPriceTxt);

        //calculus.setProduct(_products);

        Log.d("usao","Usao sam u dodavanje");

        databaseReference.setValue(calculus);


    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.scan_camera, menu);
        MenuItem btn = menu.findItem(R.id.camera);
        MenuItem scan= menu.findItem(R.id.validateBtn);


        if(mAuth.getCurrentUser().isAnonymous()) {
            scan.setVisible(false);
        }



        scan.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                IntentIntegrator.forSupportFragment(ScannerFragment.this).setCaptureActivity(ScannerActivity.class).initiateScan();

               // validateMenu=false;
                scanMenu=false;


                return Boolean.parseBoolean(null);


            }
        });


            btn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    IntentIntegrator.forSupportFragment(ScannerFragment.this).setCaptureActivity(ScannerActivity.class).initiateScan();
                    scanMenu = true;

                    // validateMenu=true;

                    return Boolean.parseBoolean(null);
                }

            });



        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Mode(id);
        switch (id) {
            case R.id.camera:
                IntentIntegrator.forSupportFragment(ScannerFragment.this).setCaptureActivity(ScannerActivity.class).initiateScan();

            case R.id.validateBtn:
                IntentIntegrator.forSupportFragment(ScannerFragment.this).setCaptureActivity(ScannerActivity.class).initiateScan();





                // do stuff, like showing settings fragment

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void Mode(int mode){
        num= mode;
    }

}

