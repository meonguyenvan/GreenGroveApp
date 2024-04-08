package com.example.greengrove.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greengrove.Activities.DetailActivity;
import com.example.greengrove.Adapter.CartAdapter;
import com.example.greengrove.Model.Cart;
import com.example.greengrove.Model.Response;
import com.example.greengrove.R;
import com.example.greengrove.my_interface.ITotalPriceChange;
import com.example.greengrove.serviecs.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class CartsFragment extends Fragment{
    private RecyclerView recyclerView;
    private HttpRequest httpRequest;
    private SharedPreferences sharedPreferences;
    private String id_user;
    private List<Cart> list;
    private TextView tvPriceTotal;
    CartAdapter cartAdapter;
    private ProgressBar barCart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_carts, container, false);
        httpRequest = new HttpRequest();
        cartAdapter = new CartAdapter(getContext());
        sharedPreferences = getActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString("id","");
        cartAdapter.setListener(new ITotalPriceChange() {
            @Override
            public void onTotalPriceChange(double totalPrice) {
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String formatPrice = decimalFormat.format(totalPrice);
                tvPriceTotal.setText("$ "+formatPrice);
            }

            @Override
            public void onDelteCartItem(String id_fruit) {
                httpRequest.callAPI().deleteCart(id_user,id_fruit).enqueue(deleteCart);
            }

            @Override
            public void onSaveQuantity(String id_fruit, int quantity) {
                //chuyen du lieu ve dang json
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id_user", id_user);
                    jsonObject.put("id_fruit",id_fruit);
                    jsonObject.put("quantity",quantity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                httpRequest.callAPI().updateCart(requestBody).enqueue(updateCart);
            }
        });
        barCart = v.findViewById(R.id.progressBarRecyCart);
        recyclerView = v.findViewById(R.id.recyclerViewCart);
        tvPriceTotal = v.findViewById(R.id.tvPriceTotal);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        onResume();
        return v;
    }
    Callback<Response<Cart>> updateCart = new Callback<Response<Cart>>() {
        @Override
        public void onResponse(Call<Response<Cart>> call, retrofit2.Response<Response<Cart>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus()==200){
                    Toast.makeText(getContext(),"Cập nhật sản phẩm thành công",Toast.LENGTH_LONG).show();
                    onResume();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Cart>> call, Throwable t) {
            Log.d("updateCart","onFailure"+t.getMessage());
        }
    };
    Callback<Response<ArrayList<Cart>>> getListCart = new Callback<Response<ArrayList<Cart>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Cart>>> call, retrofit2.Response<Response<ArrayList<Cart>>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus()==200){
                    list= new ArrayList<>();
                    list = response.body().getData();
                    Log.d("getListCart","onFailure"+list);
                    cartAdapter.setDatas(list);
                    barCart.setVisibility(View.GONE);
                    recyclerView.setAdapter(cartAdapter);

                }
            }
        }
        @Override
        public void onFailure(Call<Response<ArrayList<Cart>>> call, Throwable t) {
            Log.d("getListCart","onFailure"+t.getMessage());
        }
    };
    Callback<Response<Cart>> deleteCart = new Callback<Response<Cart>>() {
        @Override
        public void onResponse(Call<Response<Cart>> call, retrofit2.Response<Response<Cart>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus()==200){
                    Toast.makeText(getContext(),"Xóa sản phẩm thành công",Toast.LENGTH_LONG).show();
                    onResume();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Cart>> call, Throwable t) {
            Log.d("deleteCart","onFailure"+t.getMessage());
            Toast.makeText(getContext(),"Xóa sản phẩm không thành công",Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        httpRequest.callAPI().getListCart(id_user).enqueue(getListCart);
        barCart.setVisibility(View.VISIBLE);
    }
}