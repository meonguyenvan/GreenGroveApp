package com.example.greengrove.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.greengrove.Adapter.CategoryAdapter;
import com.example.greengrove.Adapter.ProductAdapter;
import com.example.greengrove.Model.Categor;
import com.example.greengrove.Model.Category;
import com.example.greengrove.Model.CategoryMapping;
import com.example.greengrove.Model.Favourite;
import com.example.greengrove.Model.Fruit;
import com.example.greengrove.Model.Response;
import com.example.greengrove.R;
import com.example.greengrove.my_interface.IClickItem;
import com.example.greengrove.my_interface.IClickItemInProductAdapter;
import com.example.greengrove.my_interface.MyViewModel;
import com.example.greengrove.serviecs.HttpRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


public class RecyclerViewFruitFragment extends Fragment {
    private RecyclerView recyclerView;
    private CategoryAdapter categoryadapter;
    private HttpRequest httpRequest;
    private SharedPreferences sharedPreferences;
    private String token, id_user;
    private List<Fruit> listFruits;
    private List<Favourite> listFV;
    private CategoryMapping mapping;
    private MyViewModel viewModel;
    private ProgressBar barRecyHome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_view_fruit, container, false);
        recyclerView = v.findViewById(R.id.recycleHome);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //Call API
        httpRequest = new HttpRequest();
        mapping = new CategoryMapping();
        //Lấy token từ sharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        id_user = sharedPreferences.getString("id", "");
        barRecyHome = v.findViewById(R.id.progressBarRecyHome);
        onResume();
        httpRequest.callAPI().getListCate().enqueue(getListCase);
        categoryadapter = new CategoryAdapter(getContext(), new IClickItem() {
            @Override
            public void onClickItemButtons(String product, boolean update) {
                if(update){
                    httpRequest.callAPI().deleteFavourite(id_user,product).enqueue(delete);
                }else {
                    //chuyen du lieu ve dang json
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("id_user", id_user);
                        jsonObject.put("id_fruit", product);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

                    httpRequest.callAPI().addFavourite(requestBody).enqueue(addFavourite);
                }
            }
        });
        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        // Lắng nghe sự thay đổi của LiveData chứa danh sách
        viewModel.getCategories().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                // Xử lý danh sách mới được nhận từ ViewModel ở đây
                // Ví dụ: cập nhật giao diện người dùng với danh sách mới
                categoryadapter = new CategoryAdapter(getContext(), new IClickItem() {
                    @Override
                    public void onClickItemButtons(String product, boolean update) {
                        if(update){
                            httpRequest.callAPI().deleteFavourite(id_user,product).enqueue(delete);
                        }else {
                            //chuyen du lieu ve dang json
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("id_user", id_user);
                                jsonObject.put("id_fruit", product);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

                            httpRequest.callAPI().addFavourite(requestBody).enqueue(addFavourite);
                        }
                    }
                });
                categoryadapter.setDatas(categories);
                recyclerView.setAdapter(categoryadapter);
            }
        });
        barRecyHome.setVisibility(View.VISIBLE);
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
    Callback<Response<ArrayList<Favourite>>> getListFV = new Callback<Response<ArrayList<Favourite>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Favourite>>> call, retrofit2.Response<Response<ArrayList<Favourite>>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus()==200){
                    listFV = new ArrayList<>();
                    listFV = response.body().getData();
                    List<String> fruitIdList = new ArrayList<>();
                    if(listFV!=null) {
                        for (Favourite favourite : listFV) {
                            // Lấy ID của trái cây
                            String fruitId = favourite.getFruit().get_id();
                            // Thêm fruitId vào danh sách fruitIdList
                            fruitIdList.add(fruitId);
                        }
                        // Lưu danh sách fruitIdList vào SharedPreferences (nếu cần)
                        // Lưu trữ danh sách fruitIdList dưới dạng một chuỗi JSON
                        String fruitIdListJson = new Gson().toJson(fruitIdList);
                        sharedPreferences.edit().putString("fruitIdList", fruitIdListJson).apply();
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Favourite>>> call, Throwable t) {
            Log.d(">>> getListFV", "onFailure" + t.getMessage());
        }
    };
    Callback<Response<ArrayList<Categor>>> getListCase = new Callback<Response<ArrayList<Categor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Categor>>> call, retrofit2.Response<Response<ArrayList<Categor>>> response) {
            if (response.isSuccessful()) {
                //check status code
                if (response.body().getStatus() == 200) {
                    ArrayList<Categor> list = response.body().getData();
                    mapping.addCategories(list);

                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Categor>>> call, Throwable t) {
            Log.d(">>> getListCate", "onFailure" + t.getMessage());
        }
    };
    Callback<Response<ArrayList<Fruit>>> getListFruitRespone = new Callback<Response<ArrayList<Fruit>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Fruit>>> call, retrofit2.Response<Response<ArrayList<Fruit>>> response) {
            if (response.isSuccessful()) {
                //check status code
                if (response.body().getStatus() == 200) {
                    //Lấy data
                    listFruits = new ArrayList<>();
                    listFruits = response.body().getData();
                    // Tạo danh sách riêng biệt cho mỗi loại trái cây
                    List<Fruit> fruitsList = new ArrayList<>();
                    List<Fruit> vegetablesList = new ArrayList<>();
                    List<Fruit> meatsList = new ArrayList<>();

                    // Lọc trái cây theo category và thêm vào danh sách tương ứng
                    for (Fruit fruit : listFruits) {
                        String categoryName = mapping.getCategoryName(fruit.getCategory());
                        if (categoryName != null) {
                            switch (categoryName) {
                                case "Fruit":
                                    fruitsList.add(fruit);
                                    break;
                                case "Vegetable":
                                    vegetablesList.add(fruit);
                                    break;
                                case "Meat":
                                    meatsList.add(fruit);
                                    break;
                                default:
                                    Toast.makeText(getContext(), "Có trong danh sách nhưng chưa sử dung", Toast.LENGTH_LONG).show();
                                    break;
                            }
                        } else {
                            Toast.makeText(getContext(), "Không có tên trong danh sách", Toast.LENGTH_LONG).show();
                        }
                    }
                    List<Category> list = new ArrayList<>();
                    list.add(new Category("Fruit", fruitsList));
                    list.add(new Category("Vegetable", vegetablesList));
                    list.add(new Category("Meat", meatsList));
                    categoryadapter.setDatas(list);
                    barRecyHome.setVisibility(View.GONE);
                    recyclerView.setAdapter(categoryadapter);
                    //Thông báo ra màn hình thông tin lấy từ messenger
                    Toast.makeText(getContext(), response.body().getMessenger(), Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Fruit>>> call, Throwable t) {
            Log.d(">>> getListFruit", "onFailure" + t.getMessage());
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        httpRequest.callAPI().getListFV(id_user).enqueue(getListFV);
        httpRequest.callAPI().getListFruit("Bearer " + token).enqueue(getListFruitRespone);
    }
};

