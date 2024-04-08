package com.example.greengrove.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.greengrove.Model.Cart;
import com.example.greengrove.Model.Category;
import com.example.greengrove.R;
import com.example.greengrove.my_interface.ITotalPriceChange;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<Cart> nlist;
    private int quantity;
    private ITotalPriceChange change;
    private double totalPrice=0.0;
    public CartAdapter(Context context) {
        this.context = context;
    }

    public void setListener(ITotalPriceChange change) {
        this.change = change;
    }

    public void setDatas(List<Cart> list){
        this.nlist = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = nlist.get(position);
        if(cart==null){
            return;
        }
        updateTotalPrice();
        Glide.with(context)
                .load(cart.getFruit().getImage().get(0))
                .thumbnail(Glide.with(context).load(R.drawable.custom_progress))
                .into(holder.imageView);
        holder.tvName.setText(cart.getFruit().getName());
        holder.tvPrice.setText(cart.getFruit().getPrice());
        holder.edSL.setText(String.valueOf(cart.getQuantity()));
        holder.imgbtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.setQuantity(cart.getQuantity() + 1);
                holder.edSL.setText(String.valueOf(cart.getQuantity()));
                String productId = nlist.get(holder.getAdapterPosition()).getFruit().get_id();
                updateTotalPrice();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scheduleQuantityUpdate(productId,cart.getQuantity());
                    }
                }, 2000);
            }
        });
        holder.imgbtnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productId = nlist.get(holder.getAdapterPosition()).getFruit().get_id();
                if(cart.getQuantity()==0){
                    cart.setQuantity(0);

                    holder.edSL.setText(String.valueOf(cart.getQuantity()));
                    updateTotalPrice();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scheduleQuantityUpdate(productId,0);
                        }
                    }, 2000);
                }
                else{
                    cart.setQuantity(cart.getQuantity() -1);
                    holder.edSL.setText(String.valueOf(cart.getQuantity()));
                    updateTotalPrice();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scheduleQuantityUpdate(productId,cart.getQuantity());
                        }
                    }, 2000);
                }
            }
        });
        holder.edSL.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String productId = nlist.get(holder.getAdapterPosition()).getFruit().get_id();
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Xử lý sự kiện khi người dùng ấn tick
                    String numberInput = v.getText().toString().trim();
                    cart.setQuantity(Integer.valueOf(numberInput));
                    updateTotalPrice();
                    // Ví dụ: ẩn bàn phím ảo
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    v.clearFocus();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scheduleQuantityUpdate(productId,cart.getQuantity());
                        }
                    }, 2000);
                    return true;
                } else if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Xử lý sự kiện khi người dùng ấn tick
                    String numberInput = v.getText().toString().trim();
                    cart.setQuantity(Integer.valueOf(numberInput));
                    updateTotalPrice();
                    // Ví dụ: ẩn bàn phím ảo
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    v.clearFocus();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scheduleQuantityUpdate(productId,cart.getQuantity());
                        }
                    }, 2000);
                    return true;
                }
                return false;
            }
        });
        holder.imgbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productId = nlist.get(holder.getAdapterPosition()).getFruit().get_id();
                change.onDelteCartItem(productId);
            }
        });
//        holder.edSL.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//
//                final Handler handler = new Handler(Looper.getMainLooper());
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        String numberInput = s.toString().trim();
//                        // Thực hiện cập nhật chỉ khi không còn ký tự nào được nhập trong khoảng thời gian chờ
//                        cart.setQuantity(Integer.valueOf(numberInput));
//                    }
//                },1000);
//
//            }
//        });
    }
    @Override
    public int getItemCount() {
        if(nlist!=null){
            return nlist.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView tvName,tvPrice;
        private ImageButton imgbtnPlus,imgbtnMinus,imgbtnDelete;
        private EditText edSL;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewCart);
            tvName = itemView.findViewById(R.id.tvNameCart);
            tvPrice = itemView.findViewById(R.id.tvPriceCart);
            edSL = itemView.findViewById(R.id.edSLCart);
            imgbtnPlus = itemView.findViewById(R.id.imgbtnPlusCart);
            imgbtnMinus = itemView.findViewById(R.id.imgbtnMinusCart);
            imgbtnDelete = itemView.findViewById(R.id.imgbtnDeleteCart);
        }
    }
    private void updateTotalPrice(){
        totalPrice = 0;
        for (Cart cart: nlist){
            double price = Double.parseDouble(cart.getFruit().getPrice());
            totalPrice+=((cart.getQuantity()) *price);
        }
        change.onTotalPriceChange(totalPrice);
    }
    private void scheduleQuantityUpdate(String id_fruit,int quantity) {
        change.onSaveQuantity(id_fruit,quantity);
    }
}
