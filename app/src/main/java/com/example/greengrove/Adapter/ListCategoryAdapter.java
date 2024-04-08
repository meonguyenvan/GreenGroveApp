package com.example.greengrove.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greengrove.Model.Category;
import com.example.greengrove.R;

import java.util.List;

public class ListCategoryAdapter extends RecyclerView.Adapter<ListCategoryAdapter.ViewHolder> {
    private Context context;
    private List<Category> mlist;
    private OnItemClickListener listener;
    private int selectedItem=0;
    public interface OnItemClickListener {
        void onItemClick(int item);
    }

    public ListCategoryAdapter(List<Category> mlist, OnItemClickListener listener) {
        this.mlist = mlist;
        this.listener = listener;
    }
    public void setSelectedItem(int position) {
        int previousSelectedItem = selectedItem;
        selectedItem = position;
        notifyItemChanged(previousSelectedItem);
        notifyItemChanged(selectedItem);
    }

    public int getSelectedItem() {
        return selectedItem;
    }
    public void setData(List<Category> list){
        this.mlist = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_item_category,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = mlist.get(position);
        if (category == null) {
            return;
        }

        holder.tvNameCategory.setText(category.getNameCategory());
        int iconResourceId = category.getIconResourceId(category.getNameCategory());
        if (iconResourceId != -1) {
            holder.imgNameCategory.setImageResource(category.getIconResourceId(category.getNameCategory()));
            holder.imgNameCategory.setVisibility(View.VISIBLE);

        } else {
            // Xử lý khi không có hình ảnh được tìm thấy
            holder.imgNameCategory.setVisibility(View.GONE); // Ẩn hình ảnh
        }

        // Xác định xem item hiện tại có phải là phần tử được chọn không (position == 0)
        if (position == selectedItem) {
            holder.itemView.setActivated(true); // Đặt trạng thái được chọn cho phần tử đầu tiên

        } else {
            holder.itemView.setActivated(false); // Đặt trạng thái không được chọn cho các phần tử khác
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousSelectedItem = selectedItem; // Lưu trữ vị trí của phần tử được chọn trước đó
                selectedItem = holder.getAdapterPosition();// Cập nhật vị trí của phần tử được chọn hiện tại
                if (selectedItem != RecyclerView.NO_POSITION) {
                    listener.onItemClick(selectedItem);
                    // Đặt trạng thái được chọn cho phần tử hiện tại
                    holder.itemView.setActivated(true);
                    // Nếu có phần tử được chọn trước đó, đặt trạng thái không được chọn cho nó
                    if (previousSelectedItem != RecyclerView.NO_POSITION) {
                        notifyItemChanged(previousSelectedItem);
                    }
                    // Thông báo rằng dữ liệu đã thay đổi cho phần tử được chọn trước đó và phần tử mới được chọn
                    notifyItemChanged(selectedItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mlist!=null){
            return mlist.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameCategory;
        ImageView imgNameCategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameCategory= itemView.findViewById(R.id.tvNameCategory);
            imgNameCategory= itemView.findViewById(R.id.imgNameCategory);
        }
    }
}
