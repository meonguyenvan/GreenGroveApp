package com.example.greengrove.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.greengrove.Activities.DetailActivity;
import com.example.greengrove.Model.Fruit;
import com.example.greengrove.R;
import com.example.greengrove.my_interface.IClickItemInProductAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private SharedPreferences sharedPreferences;
    private List<Fruit> mfFruits;
    private IClickItemInProductAdapter clickItemInProductAdapter;
    public ProductAdapter(Context context,IClickItemInProductAdapter listener) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("INFO", Context.MODE_PRIVATE);
        this.clickItemInProductAdapter = listener;
    };

    public void setData(List<Fruit> list) {
        this.mfFruits = list;
        notifyDataSetChanged();
    }



    public List<Fruit> getData() {
        return mfFruits;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_fruit_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        Fruit fruit = mfFruits.get(position);
        if (fruit == null) {
            return;
        }
        Glide.with(context)
                .load(fruit.getImage().get(0))
                .thumbnail(Glide.with(context).load(R.drawable.custom_progress))
                .into(holder.img);
        holder.tvname.setText(fruit.getName());
        String tvprice = fruit.getPrice();
        String[] parts = tvprice.split("\\.");
        holder.tvpricei.setText(parts[0] + ".");
        holder.tvpricef.setText(parts[1] + "$");

        // Lấy ID của trái cây hiện tại
        String fruitId = fruit.get_id();

        // Lấy chuỗi JSON từ SharedPreferences
        String fruitIdListJson = sharedPreferences.getString("fruitIdList", "");

        // Chuyển đổi chuỗi JSON thành danh sách (list) bằng Gson
        Type listType = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> fruitIdList = new Gson().fromJson(fruitIdListJson, listType);
        Log.d(">>>>>>>>>>>>>>>","list"+fruitIdList);
        for (String id : fruitIdList) {
            if (id.equals(fruitId)) {
              holder.imgbtn.setActivated(true);
              break;
            }else {
                holder.imgbtn.setActivated(false);
            }
        }
        holder.imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy ID của trái cây hiện tại
                String productId = mfFruits.get(holder.getAdapterPosition()).get_id();
                // Đảo ngược trạng thái activated của imgbtn
                boolean currentActivatedState = holder.imgbtn.isActivated();
                holder.imgbtn.setActivated(!currentActivatedState);
                 clickItemInProductAdapter.onClickItemButton(productId,currentActivatedState);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", (Serializable) mfFruits.get(holder.getAdapterPosition()));
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtras(bundle);
                (context).startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mfFruits != null) {
            return mfFruits.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageButton imgbtn;
        private ImageView img;
        private TextView tvname, tvpricei, tvpricef;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgbtn = itemView.findViewById(R.id.imgbutton);
            img = itemView.findViewById(R.id.img);
            tvname = itemView.findViewById(R.id.tvNameProduct);
            tvpricei = itemView.findViewById(R.id.tvpricei);
            tvpricef = itemView.findViewById(R.id.tvpricef);
        }
    }
}
