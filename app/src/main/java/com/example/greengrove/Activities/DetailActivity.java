package com.example.greengrove.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.greengrove.Model.Cart;
import com.example.greengrove.Model.Fruit;
import com.example.greengrove.Model.Response;
import com.example.greengrove.Model.Review;
import com.example.greengrove.R;
import com.example.greengrove.serviecs.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class DetailActivity extends AppCompatActivity {
    private ImageButton imgbtnBack,imgbtnPlus,imgbtnMinus;
    private TextView tvHeader,tvNameFruitDetail,tvPriceIDetail,tvPriceFDetail,tvAvg,tvPerson,tvDescription;
    private EditText edSL;
    private ImageView imgFruit,imgStart1,imgStart2,imgStart3,imgStart4,imgStart5;
    private Button btnAddToCard;
    private int count=1;
    private HttpRequest httpRequest;
    private SharedPreferences sharedPreferences;
    private String id_user,id_fruit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        httpRequest = new HttpRequest();
        sharedPreferences = getSharedPreferences("INFO", Context.MODE_PRIVATE);
        id_user= sharedPreferences.getString("id","");
        imgbtnBack = findViewById(R.id.imgbtnBack);
        imgbtnPlus = findViewById(R.id.imgbtnPlus);
        imgbtnMinus = findViewById(R.id.imgbtnMinus);
        tvHeader = findViewById(R.id.tvHeader);
        tvNameFruitDetail = findViewById(R.id.tvNameFruitDetail);
        tvPriceIDetail = findViewById(R.id.tvPriceIFruitDetail);
        tvPriceFDetail = findViewById(R.id.tvPriceFFruitDetail);
        tvAvg = findViewById(R.id.tvAvg);
        tvPerson = findViewById(R.id.tvPerson);
        edSL = findViewById(R.id.edSLDetail);
        tvDescription = findViewById(R.id.descriptionFruitDetails);
        imgFruit = findViewById(R.id.imageFruitDetails);
        btnAddToCard = findViewById(R.id.btnAddToCart);
        imgStart1 = findViewById(R.id.start1);
        imgStart2 = findViewById(R.id.start2);
        imgStart3 = findViewById(R.id.start3);
        imgStart4 = findViewById(R.id.start4);
        imgStart5 = findViewById(R.id.start5);
        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Fruit fruit = (Fruit) getIntent().getExtras().getSerializable("item");
        id_fruit = fruit.get_id();
        httpRequest.callAPI().getReviews(id_fruit).enqueue(getReview);
        tvHeader.setText(fruit.getName());
        Glide.with(DetailActivity.this)
                .load(fruit.getImage().get(0))
                .thumbnail(Glide.with(DetailActivity.this).load(R.drawable.custom_progress))
                .into(imgFruit);
        tvNameFruitDetail.setText(fruit.getName());
        String price = fruit.getPrice();
        String[] parts = price.split("\\.");
        tvPriceIDetail.setText(parts[0]+".");
        tvPriceFDetail.setText(parts[1]+"$");
        String shortDescription = fruit.getDescription().substring(0,((fruit.getDescription().length())/2));
        String fullDescription =fruit.getDescription();
        SpannableString spannableString = new SpannableString(shortDescription + "Read more...");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // Khi người dùng nhấn vào "Read more...", thay đổi nội dung của TextView để hiển thị đầy đủ mô tả
                tvDescription.setText(fullDescription);
            }
        };
        spannableString.setSpan(clickableSpan, shortDescription.length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.GREEN);
        spannableString.setSpan(colorSpan, shortDescription.length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvDescription.setText(spannableString);
        tvDescription.setMovementMethod(LinkMovementMethod.getInstance());
        edSL.setText(String.format("%02d", count));
        imgbtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                edSL.setText(String.format("%02d", count));
            }
        });
        imgbtnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==0){
                    edSL.setText(String.format("%02d", count));
                }else {
                    count--;
                    edSL.setText(String.format("%02d", count));
                }
            }
        });
        edSL.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Xử lý sự kiện khi người dùng ấn tick
                    count = Integer.parseInt(v.getText().toString().trim());
                    // Ví dụ: ẩn bàn phím ảo
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    v.clearFocus();
                    return true;
                } else if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Xử lý sự kiện khi người dùng ấn tick
                    count = Integer.parseInt(v.getText().toString().trim());
                    // Ví dụ: ẩn bàn phím ảo
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    v.clearFocus();
                    return true;
                }
                return false;
            }
        });
        btnAddToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==00 ||count==0){
                    Toast.makeText(DetailActivity.this,"Không thể thêm sản phẩm vào giỏ hàng với số lượng bằng 0",Toast.LENGTH_LONG).show();
                    return;
                }
                //chuyen du lieu ve dang json
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id_user",id_user);
                    jsonObject.put("id_fruit",id_fruit);
                    jsonObject.put("quantity",count);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                httpRequest.callAPI().addCart(requestBody).enqueue(addCart);
            }
        });
    }

    Callback<Response<Cart>> addCart = new Callback<Response<Cart>>() {
        @Override
        public void onResponse(Call<Response<Cart>> call, retrofit2.Response<Response<Cart>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus()==200){
                    Toast.makeText(DetailActivity.this,"Thêm vào giỏ hàng thành công",Toast.LENGTH_LONG).show();
                }
                if(response.body().getStatus()==400){
                    Toast.makeText(DetailActivity.this,"Sản phẩm đã có trong giỏ hàng",Toast.LENGTH_LONG).show();
                }
            }
        }
        @Override
        public void onFailure(Call<Response<Cart>> call, Throwable t) {
            Log.d("addCartFailse","onFailure"+t.getMessage());
            //onFailurejava.lang.IllegalStateException: Expected BEGIN_OBJECT but was BEGIN_ARRAY at line 1 column 72 path $.data
            //Không cần lấy data về thì để nó null là ok
            Toast.makeText(DetailActivity.this,"Thêm vào giỏ hàng không thành công",Toast.LENGTH_LONG).show();
        }
    };

    Callback<Response<Review>> getReview = new Callback<Response<Review>>() {
        @Override
        public void onResponse(Call<Response<Review>> call, retrofit2.Response<Response<Review>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus()==200){
                    Toast.makeText(DetailActivity.this,"CallAPI thành công",Toast.LENGTH_LONG).show();
                    float avgStart = response.body().getData().getAverageRating();
                    int numberOfReviews = response.body().getData().getNumberOfReviews();
                    tvAvg.setText(String.valueOf(avgStart));
                    tvPerson.setText(String.valueOf(numberOfReviews)+" reviews");
                    if (avgStart >= 0.5) {
                        imgStart1.setImageResource(R.drawable.start);
                    } else {
                        imgStart1.setImageResource(R.drawable.unstart);
                    }
                    if (avgStart >= 1.5) {
                        imgStart2.setImageResource(R.drawable.start);
                    } else {
                        imgStart2.setImageResource(R.drawable.unstart);
                    }
                    if (avgStart >= 2.5) {
                        imgStart3.setImageResource(R.drawable.start);
                    } else {
                        imgStart3.setImageResource(R.drawable.unstart);
                    }
                    if (avgStart >= 3.5) {
                        imgStart4.setImageResource(R.drawable.start);
                    } else {
                        imgStart4.setImageResource(R.drawable.unstart);
                    }
                    if (avgStart >= 4.5) {
                        imgStart5.setImageResource(R.drawable.start);
                    } else {
                        imgStart5.setImageResource(R.drawable.unstart);
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Review>> call, Throwable t) {
            Log.d("getReviews","onFailure"+t.getMessage());
        }
    };
}