package com.example.greengrove.Adapter;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greengrove.Model.Category;
import com.example.greengrove.Model.Fruit;
import com.example.greengrove.R;
import com.example.greengrove.my_interface.IClickItem;
import com.example.greengrove.my_interface.IClickItemInProductAdapter;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private Context context;
    private List<Category> mCategories;
    private IClickItem clickItemInProductAdapter;
    public CategoryAdapter(Context context,IClickItem clickItemInProductAdapter) {
        this.context = context;
        this.clickItemInProductAdapter = clickItemInProductAdapter;
    }

    public void setDatas(List<Category> list){
        this.mCategories = list;
        notifyDataSetChanged();
    }
    public List<Category> getData() {
        return mCategories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_category,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = mCategories.get(position);
        if(category==null){
            return;
        }
        holder.nameCategory.setText(category.getNameCategory());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
        holder.rcvCategory.setLayoutManager(linearLayoutManager);
        ProductAdapter adapter = new ProductAdapter(context, new IClickItemInProductAdapter() {
            @Override
            public void onClickItemButton(String productID, boolean update) {
                clickItemInProductAdapter.onClickItemButtons(productID,update);
            }
        });
        adapter.setData(category.getFruits());
        holder.rcvCategory.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        if(mCategories!=null){
            return  mCategories.size();
        }
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        private TextView nameCategory;
        private RecyclerView rcvCategory;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            nameCategory= itemView.findViewById(R.id.tvcategory);
            rcvCategory = itemView.findViewById(R.id.rcv_category);
        }
    }
}
