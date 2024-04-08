package com.example.greengrove.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.greengrove.Adapter.ProductAdapter;
import com.example.greengrove.Model.Category;
import com.example.greengrove.Model.CategoryMapping;
import com.example.greengrove.Model.Favourite;
import com.example.greengrove.Model.Fruit;
import com.example.greengrove.Model.Response;
import com.example.greengrove.R;
import com.example.greengrove.my_interface.IClickItemInProductAdapter;
import com.example.greengrove.my_interface.MyViewModel;
import com.example.greengrove.my_interface.ViewHolderFruit;
import com.example.greengrove.serviecs.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class FruitFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private HttpRequest httpRequest;
    private List<Fruit> listFrui;
    private SharedPreferences sharedPreferences;
    private String id_user;

    private ViewHolderFruit viewHolderFruit;
    private ProgressBar progressBarRecyFruit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_fruit, container, false);
        recyclerView = v.findViewById(R.id.recycleFruits);
        progressBarRecyFruit = v.findViewById(R.id.progressBarRecyFruit);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        //Call API
        httpRequest = new HttpRequest();
        //Lấy token từ sharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString("id", "");
        productAdapter = new ProductAdapter(getContext(), new IClickItemInProductAdapter() {
            @Override
            public void onClickItemButton(String productID, boolean update) {
                if(update){
                    httpRequest.callAPI().deleteFavourite(id_user,productID).enqueue(delete);
                }else {
                    //chuyen du lieu ve dang json
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id_user", id_user);
                        jsonObject.put("id_fruit", productID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

                    httpRequest.callAPI().addFavourite(requestBody).enqueue(addFavourite);
                }
            }
        });
        httpRequest.callAPI().getListML("65fd54c2582fcf13a3252c09").enqueue(getListML);
        progressBarRecyFruit.setVisibility(View.VISIBLE);
        viewHolderFruit = new ViewModelProvider(requireActivity()).get(ViewHolderFruit.class);
        viewHolderFruit.getFruits().observe(getViewLifecycleOwner(), new Observer<List<Fruit>>() {
            @Override
            public void onChanged(List<Fruit> list) {
                productAdapter = new ProductAdapter(getContext(), new IClickItemInProductAdapter() {
                    @Override
                    public void onClickItemButton(String productID, boolean update) {
                        if(update){
                            httpRequest.callAPI().deleteFavourite(id_user,productID).enqueue(delete);
                        }else {
                            //chuyen du lieu ve dang json
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("id_user", id_user);
                                jsonObject.put("id_fruit", productID);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

                            httpRequest.callAPI().addFavourite(requestBody).enqueue(addFavourite);
                        }
                    }
                });
                productAdapter.setData(list);
                recyclerView.setAdapter(productAdapter);
            }
        });
        return v;
    }
    Callback<Response<Favourite>> addFavourite = new Callback<Response<Favourite>>() {
        @Override
        public void onResponse(Call<Response<Favourite>> call, retrofit2.Response<Response<Favourite>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus()==200){
                    Toast.makeText(getContext(),"Đã thêm vào danh sách yêu thích",Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Favourite>> call, Throwable t) {
            Log.d(">>> AddFavourite", "onFailure" + t.getMessage());
        }
    };
    Callback<Response<Favourite>> delete = new Callback<Response<Favourite>>() {
        @Override
        public void onResponse(Call<Response<Favourite>> call, retrofit2.Response<Response<Favourite>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus()==200){
                    Toast.makeText(getContext(),"Đã loại khỏi danh sách yêu thích",Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Favourite>> call, Throwable t) {
            Log.d(">>> delete", "onFailure" + t.getMessage());
        }
    };
    Callback<Response<ArrayList<Fruit>>> getListML = new Callback<Response<ArrayList<Fruit>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Fruit>>> call, retrofit2.Response<Response<ArrayList<Fruit>>> response) {
            if(response.isSuccessful()){
                //check status code
                if(response.body().getStatus()==200){
                    //Lấy data
                    listFrui= new ArrayList<>();
                    listFrui= response.body().getData();
                    productAdapter.setData(listFrui);
                    progressBarRecyFruit.setVisibility(View.GONE);
                    recyclerView.setAdapter(productAdapter);
            }
        }}
        @Override
        public void onFailure(Call<Response<ArrayList<Fruit>>> call, Throwable t) {
            Log.d(">>> getListFruit","onFailure"+t.getMessage());
        }
    };

}