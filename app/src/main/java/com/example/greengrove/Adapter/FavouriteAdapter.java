package com.example.greengrove.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.greengrove.Model.Favourite;
import com.example.greengrove.R;
import com.example.greengrove.my_interface.IFavouriteItem;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder>{
    private Context context;
    private List<Favourite> flist;
    private IFavouriteItem iFavouriteItem;
    public FavouriteAdapter(Context context, List<Favourite> flist,IFavouriteItem iFavouriteItem) {
        this.context = context;
        this.flist = flist;
        this.iFavouriteItem = iFavouriteItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_favourite_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Favourite favourite = flist.get(position);
        if(favourite==null){
            return;
        }
        Glide.with(context)
                .load(favourite.getFruit().getImage().get(0))
                .thumbnail(Glide.with(context).load(R.drawable.custom_progress))
                .into(holder.imageViewFV);
        holder.textView.setText(favourite.getFruit().getName());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = flist.get(holder.getAdapterPosition()).getFruit().get_id();
                iFavouriteItem.deleteItem(id);
            }
        });

    }
    @Override
    public int getItemCount() {
        if(flist!=null){
            return flist.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewFV;
        private TextView textView,tvPerson;
        private RatingBar ratingBar;
        private ImageButton imageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFV = itemView.findViewById(R.id.imageViewFavourite);
            textView = itemView.findViewById(R.id.tvNameFavourite);
            tvPerson = itemView.findViewById(R.id.userReviewsFavourite);
            ratingBar = itemView.findViewById(R.id.ratingBarFavourite);
            imageButton = itemView.findViewById(R.id.imgbtnFavourite);
        }
    }
}
