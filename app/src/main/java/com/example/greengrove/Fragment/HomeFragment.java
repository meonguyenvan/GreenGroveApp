package com.example.greengrove.Fragment;
import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.greengrove.Adapter.CategoryAdapter;
import com.example.greengrove.Adapter.ListCategoryAdapter;
import com.example.greengrove.Adapter.PhotoAdapter;
import com.example.greengrove.Adapter.ProductAdapter;
import com.example.greengrove.Adapter.ViewPager2Adapter;
import com.example.greengrove.Model.Categor;
import com.example.greengrove.Model.Category;
import com.example.greengrove.Model.CategoryMapping;
import com.example.greengrove.Model.Fruit;
import com.example.greengrove.Model.Photo;
import com.example.greengrove.Model.Response;
import com.example.greengrove.R;
import com.example.greengrove.my_interface.MyViewModel;
import com.example.greengrove.my_interface.ViewHolderFruit;
import com.example.greengrove.serviecs.HttpRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment {
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PhotoAdapter photoAdapter;
    private List<Photo> mListPhoto;
    private Timer mtimer;
    private RecyclerView rcv,recyclerCategory;
    private ListCategoryAdapter listCategoryAdapter;
    private ViewPager2 viewPager2;
    private ViewPager2Adapter viewPager2Adapter;
    private SearchView searchView;
    private HttpRequest httpRequest;
    private CategoryMapping mapping;
    private MyViewModel viewModel;
    private ViewHolderFruit viewHolderFruit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = v.findViewById(R.id.viewpager);
        searchView = v.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        circleIndicator = v.findViewById(R.id.circle);
        recyclerCategory = v.findViewById(R.id.recyclerCategory);
        httpRequest = new HttpRequest();
        mapping = new CategoryMapping();
        // Khởi tạo ViewModel
        // Tạo ViewModelProvider.Factory cho MyViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        viewHolderFruit = new ViewModelProvider(requireActivity()).get(ViewHolderFruit.class);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        recyclerCategory.setLayoutManager(linearLayoutManager);
        httpRequest.callAPI().getListCate().enqueue(getListCase);
        listCategoryAdapter = new ListCategoryAdapter(getList(), new ListCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                onResume();
                switch (position){
                    case 0:
                        viewPager2.setCurrentItem(0);
                        break;
                    case 1:
                        viewPager2.setCurrentItem(1);
                        break;
                    case 2:
                        viewPager2.setCurrentItem(2);
                        break;
                    case 3:
                        viewPager2.setCurrentItem(3);
                        break;
                    default:
                        viewPager2.setCurrentItem(0);
                }
            }
        });

        recyclerCategory.setAdapter(listCategoryAdapter);
//        rcv = v.findViewById(R.id.recycleHome);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
//        rcv.setLayoutManager(linearLayoutManager);
//        categoryadapter = new CategoryAdapter(getContext());
        viewPager2 = v.findViewById(R.id.ViewPagerHome);
        viewPager2Adapter = new ViewPager2Adapter(requireActivity());
        viewPager2.setAdapter(viewPager2Adapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                onResume();
                switch (position) {
                    case 0:
                        listCategoryAdapter.setSelectedItem(0);
                        break;
                    case 1:
                        listCategoryAdapter.setSelectedItem(1);
                        break;
                    case 2:
                        listCategoryAdapter.setSelectedItem(2);
                        break;
                    case 3:
                        listCategoryAdapter.setSelectedItem(3);
                        break;
                    default:
                        listCategoryAdapter.setSelectedItem(0);
                        break;
                }
            }
        });
        mListPhoto=getListPhoto();
        photoAdapter = new PhotoAdapter(getContext(),mListPhoto);
        viewPager.setAdapter(photoAdapter);
        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        autoSlideImage();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Kiểm tra xem chuỗi có phải là một số hay không
                if (isNumeric(query)) {
                    // Nếu là số, kiểm tra xem nó là một số duy nhất hay là một khoảng giá
                    if (isSinglePrice(query)) {
                        Toast.makeText(getContext(),"Tìm kiếm theo 1 giá",Toast.LENGTH_LONG).show();
                        httpRequest.callAPI().getListSearchPrice(query,"").enqueue(getListSearchPrice);
                    } else if (isPriceRange(query)) {
                        // Tách ra hai giá từ chuỗi khoảng giá
                        String[] prices = query.split("-");
                        if (prices.length == 2) {
                            String priceStart = prices[0].trim(); // Giá bắt đầu
                            String priceEnd = prices[1].trim(); // Giá kết thúc
                            Toast.makeText(getContext(),"Tìm kiếm theo khoảng giá từ " + priceStart + " đến " + priceEnd,Toast.LENGTH_LONG).show();
                            httpRequest.callAPI().getListSearchPrice(priceStart,priceEnd).enqueue(getListSearchPrice);
                        } else {
                            // Trường hợp không hợp lệ của chuỗi khoảng giá
                            Toast.makeText(getContext(),"Định dạng khoảng giá không hợp lệ",Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(),"Tìm kiếm theo tên",Toast.LENGTH_LONG).show();
                    httpRequest.callAPI().getListSearch(query).enqueue(getListSearch);


                }
                return false;
            };

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return v;
    }
    Callback<Response<ArrayList<Fruit>>> getListSearchPrice = new Callback<Response<ArrayList<Fruit>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Fruit>>> call, retrofit2.Response<Response<ArrayList<Fruit>>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus()==200){
                    List<Fruit> list = new ArrayList<>();
                    list = response.body().getData();
                    Log.d("getListSearchPrice","onFailure"+list);
                    // Tạo danh sách riêng biệt cho mỗi loại trái cây
                    List<Fruit> fruitsList = new ArrayList<>();
                    List<Fruit> vegetablesList = new ArrayList<>();
                    List<Fruit> meatsList = new ArrayList<>();

                    // Lọc trái cây theo category và thêm vào danh sách tương ứng
                    for (Fruit fruit : list) {
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
                    List<Category> listcate = new ArrayList<>();
                    listcate.add(new Category("Fruit", fruitsList));
                    listcate.add(new Category("Vegetable", vegetablesList));
                    listcate.add(new Category("Meat", meatsList));
                    setList(listcate);
                    setListFruit(fruitsList);
                    //Thông báo ra màn hình thông tin lấy từ messenger
                    Toast.makeText(getContext(), response.body().getMessenger(), Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Fruit>>> call, Throwable t) {
            Log.d(">>> getListFruitPrice", "onFailure" + t.getMessage());
        }
    };
    Callback<Response<ArrayList<Fruit>>> getListSearch = new Callback<Response<ArrayList<Fruit>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Fruit>>> call, retrofit2.Response<Response<ArrayList<Fruit>>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus()==200){
                    List<Fruit> list = new ArrayList<>();
                    list = response.body().getData();
                    Log.d("getListSearchPrice","onFailure"+list);
                    // Tạo danh sách riêng biệt cho mỗi loại trái cây
                    List<Fruit> fruitsList = new ArrayList<>();
                    List<Fruit> vegetablesList = new ArrayList<>();
                    List<Fruit> meatsList = new ArrayList<>();

                    // Lọc trái cây theo category và thêm vào danh sách tương ứng
                    for (Fruit fruit : list) {
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
                    List<Category> listcate = new ArrayList<>();
                    listcate.add(new Category("Fruit", fruitsList));
                    listcate.add(new Category("Vegetable", vegetablesList));
                    listcate.add(new Category("Meat", meatsList));
                    setList(listcate);
                    setListFruit(fruitsList);
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
    private List<Photo> getListPhoto(){
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.img_1));
        list.add(new Photo(R.drawable.img_5));
        list.add(new Photo(R.drawable.img_3));
        return list;
    }
    private List<Category> getList(){
        List<Category> list = new ArrayList<>();
        list.add(new Category("All"));
        list.add(new Category("Fruits"));
        list.add(new Category("Vegetables"));
        list.add(new Category("Meats"));
        list.add(new Category("Dairys"));
        return list;
    }
    private void autoSlideImage(){
        if(mListPhoto==null ||mListPhoto.isEmpty() ||viewPager==null){
            return;
        }
        //init
        if(mtimer==null){
            mtimer= new Timer();
        }
        mtimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = mListPhoto.size()-1;
                        if(currentItem<totalItem){
                            currentItem++;
                            viewPager.setCurrentItem(currentItem);
                        }else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        },500,3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mtimer!=null){
            mtimer.cancel();
            mtimer=null;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        searchView.setQuery("", false);
        searchView.clearFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
        }
    }
    // Phương thức kiểm tra xem chuỗi có phải là giá tiền hay không
    private boolean isNumeric(String str) {
        // Kiểm tra xem chuỗi có chứa dấu gạch ngang "-" không
        if (str.contains("-")) {
            // Tách chuỗi thành các phần
            String[] parts = str.split("-");
            // Kiểm tra xem các phần sau khi tách có phải là số không
            for (String part : parts) {
                try {
                    Double.parseDouble(part.trim());
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            return true;
        } else {
            // Nếu không chứa "-", kiểm tra xem chuỗi có phải là số không
            try {
                Double.parseDouble(str);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }
    private boolean isSinglePrice(String str) {
        double price;
        try {
            price = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return price >= 0; // Giá trị số lớn hơn hoặc bằng 0 được coi là một giá hợp lệ
    }

    private boolean isPriceRange(String str) {
        return str.matches("\\d+-\\d+");
    }
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
    // Method to set list
    private void setList(List<Category> newList) {
        viewModel.setCategories(newList);
    }
    private void setListFruit(List<Fruit> newFruit){
        viewHolderFruit.setFruits(newFruit);
    }

}