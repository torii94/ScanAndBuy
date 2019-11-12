package ba.sum.fpmoz.shopitem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.util.ArrayList;

import ba.sum.fpmoz.shopitem.model.Product;

public class Example_Adapter extends RecyclerView.Adapter<Example_Adapter.ExampleVieewHolder> {
    private Context mContext;
    private ArrayList<Product> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int postion);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }

    public Example_Adapter(Context context, ArrayList<Product> exampleList){
        mContext=context;
        mExampleList=exampleList;
    }

    @NonNull
    @Override
    public ExampleVieewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.example_item, parent,false );

        return new ExampleVieewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleVieewHolder exampleVieewHolder, int position) {
        Product currentItem= mExampleList.get(position);

        String imaageUrl= currentItem.image;
        String creatorName= currentItem.name;
        double likeCount= currentItem.price;


        exampleVieewHolder.mTextViewCreator.setText(creatorName);
        exampleVieewHolder.mTextViewLikes.setText("Likes: "+ likeCount);
        Picasso.get().load(imaageUrl).fit().centerInside().into(exampleVieewHolder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class ExampleVieewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextViewCreator;
        public TextView mTextViewLikes;

        public ExampleVieewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.image_view);
            mTextViewCreator = itemView.findViewById(R.id.tetx_view_creator);
            mTextViewLikes= itemView.findViewById(R.id.text_view_likes);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null ){
                        int position= getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);

                        }
                    }
                }
            });
        }
    }
}
