package com.example.greengrove.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.greengrove.Adapter.FavouriteAdapter;
import com.example.greengrove.Model.Favourite;
import com.example.greengrove.Model.Fruit;
import com.example.greengrove.Model.Response;
import com.example.greengrove.R;
import com.example.greengrove.my_interface.IFavouriteItem;
import com.example.greengrove.serviecs.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class FavouriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private HttpRequest httpRequest;
    private SharedPreferences sharedPreferences;
    private String id_user;
    private List<Favourite> listFV;
    FavouriteAdapter favouriteAdapter;
    private ProgressBar progressBarRecyFv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favourite, container, false);
        httpRequest = new HttpRequest();
        sharedPreferences = getActivity().getSharedPreferences("INFO", Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString("id","");
        recyclerView =v.findViewById(R.id.recyclerViewFavourite);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        searchView = v.findViewById(R.id.searchViewFavourite);
        progressBarRecyFv = v.findViewById(R.id.progressBarRecyFv);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               if (!query.matches("[a-zA-Z]+")) {
                    // Nếu không phải là chữ cái, hiển thị thông báo
                    Toast.makeText(getContext().getApplicationContext(), "Vui lòng nhập ký tự.", Toast.LENGTH_SHORT).show();
                }
                httpRequest.callAPI().findFavourite(id_user,query).enqueue(getFind);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
             return false;
            }
        });
        onResume();
        return v;
    }
    Callback<Response<ArrayList<Favourite>>> getFind = new Callback<Response<ArrayList<Favourite>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Favourite>>> call, retrofit2.Response<Response<ArrayList<Favourite>>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus()==200){
                    listFV = new ArrayList<>();
                    listFV = response.body().getData();
                    favouriteAdapter = new FavouriteAdapter(getContext(), listFV, new IFavouriteItem() {
                        @Override
                        public void deleteItem(String id) {
                            httpRequest.callAPI().deleteFavourite(id_user,id).enqueue(delete);
                        }
                    });
                    recyclerView.setAdapter(favouriteAdapter);
                }
            }
        }
        @Override
        public void onFailure(Call<Response<ArrayList<Favourite>>> call, Throwable t) {
            Log.d("LISTFV","onFailure"+t.getMessage());
        }
    };
    Callback<Response<ArrayList<Favourite>>> getListFV = new Callback<Response<ArrayList<Favourite>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Favourite>>> call, retrofit2.Response<Response<ArrayList<Favourite>>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus()==200){
                    listFV = new ArrayList<>();
                    listFV = response.body().getData();
                    favouriteAdapter = new FavouriteAdapter(getContext(), listFV, new IFavouriteItem() {
                        @Override
                        public void deleteItem(String id) {
                            httpRequest.callAPI().deleteFavourite(id_user,id).enqueue(delete);
                        }
                    });
                    progressBarRecyFv.setVisibility(View.GONE);
                    recyclerView.setAdapter(favouriteAdapter);
                    Log.d("LISTFV","onFailure"+listFV);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Favourite>>> call, Throwable t) {
            Log.d("LISTFV","onFailure"+t.getMessage());
        }
    };
    Callback<Response<Favourite>> delete = new Callback<Response<Favourite>>() {
        @Override
        public void onResponse(Call<Response<Favourite>> call, retrofit2.Response<Response<Favourite>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus()==200){
                    Toast.makeText(getContext(),"Đã loại khỏi danh sách yêu thích",Toast.LENGTH_LONG).show();
                    onResume();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Favourite>> call, Throwable t) {
            Log.d(">>> delete", "onFailure" + t.getMessage());
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        httpRequest.callAPI().getListFV(id_user).enqueue(getListFV);
        progressBarRecyFv.setVisibility(View.VISIBLE);
        searchView.setQuery("", false);
        searchView.clearFocus();
    }
}