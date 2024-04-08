package com.example.greengrove.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.greengrove.MainActivity;
import com.example.greengrove.Model.Response;
import com.example.greengrove.Model.Users;
import com.example.greengrove.R;
import com.example.greengrove.serviecs.HttpRequest;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ProfileActivity extends AppCompatActivity {
    private LinearLayout imageView,name,username,email;
    private ShapeableImageView shapeableImageView;
    private SharedPreferences sharedPreferences;
    private HttpRequest httpRequest;
    private String id;
    private TextView tvNamePF,tvUserNamePF,tvEmailPF;
    private File file;
    private ImageButton imgbtn,imgbtnBack;

    private static final String PREF_FIRST_TIME_RESUME = "first_time_resume";
    private static final String PREF_NAME = "name";
    private static final String PREF_USER_NAME = "user_name";
    private static final String PREF_USER_EMAIL = "user_email";
    private static final String PREF_USER_AVATAR = "user_avatar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        httpRequest = new HttpRequest();
        sharedPreferences = getSharedPreferences("INFO", Context.MODE_PRIVATE);
        id= sharedPreferences.getString("id","");
        imgbtn = findViewById(R.id.btnCheck);
        tvNamePF = findViewById(R.id.tvNamePF);
        tvUserNamePF = findViewById(R.id.tvUserNamePF);
        tvEmailPF = findViewById(R.id.tvEmailPF);
        imgbtnBack = findViewById(R.id.imgbtnBackAC);
        imageView = findViewById(R.id.linearImageViewPF);
        name = findViewById(R.id.linearLayoutName);
        username = findViewById(R.id.linearLayoutUserName);
        email = findViewById(R.id.linearLayoutEmail);
        shapeableImageView= findViewById(R.id.shapeableImageViewPFAC);
        imageView = findViewById(R.id.linearImageViewPF);
        name = findViewById(R.id.linearLayoutName);
        isFirstTimeResume();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,NameActivity.class));
                finish();
            }
        });

        String nameFromIntent = getIntent().getStringExtra("name");
        if (nameFromIntent != null) {
            // Xóa giá trị cũ của PREF_NAME
            sharedPreferences.edit().remove(PREF_NAME)
                                    .remove("avatar")
                                    .remove("username")
                                    .remove("email").apply();

            // Lưu giá trị mới nhận được từ Intent vào SharedPreferences
            sharedPreferences.edit()
                    .putString(PREF_NAME, nameFromIntent)
                    .apply();
            tvNamePF.setText(getIntent().getStringExtra("name"));
        }else{
            String name = sharedPreferences.getString(PREF_NAME,"");
            tvNamePF.setText(name);
        }
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUp();
            }
        });
        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.putExtra("bottom_fragment", 3); // 3 là index của Fragment 4 trong bottom navigation
                startActivity(intent);
                finish();
            }
        });

    }
    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        getImage.launch(intent);
    }
    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri imagePath = data.getData();
                            file = createFileFromUri(imagePath, "avatar");
                            Glide.with(ProfileActivity.this)
                                    .load(file)
                                    .thumbnail(Glide.with(ProfileActivity.this).load(R.drawable.img))
                                    .centerCrop()
                                    .circleCrop()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(shapeableImageView);
                        }
                    }
                }
            });

    private File createFileFromUri(Uri path, String name) {
        File _file = new File(ProfileActivity.this.getCacheDir(), name + ".png");
        try {
            InputStream in = ProfileActivity.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void isFirstTimeResume() {
        boolean isFirstTimeResume = sharedPreferences.getBoolean(PREF_FIRST_TIME_RESUME, true);
        if (isFirstTimeResume) {
            // Gọi HTTP request
            httpRequest.callAPI().getUser(id).enqueue(getUser);

            // Đặt isFirstTime thành false để đánh dấu rằng request đã được gọi
            sharedPreferences.edit().putBoolean(PREF_FIRST_TIME_RESUME, false).apply();
        } else {
            // Lấy dữ liệu từ SharedPreferences và cập nhật UI
            String userName = sharedPreferences.getString(PREF_USER_NAME, "");
            String name = sharedPreferences.getString(PREF_NAME,"");
            String userEmail = sharedPreferences.getString(PREF_USER_EMAIL, "");
            String userAvatar = sharedPreferences.getString(PREF_USER_AVATAR, "");

            if (!name.isEmpty()) tvNamePF.setText(name);
            if(!userName.isEmpty()) tvUserNamePF.setText(userName);
            if (!userEmail.isEmpty()) tvEmailPF.setText(userEmail);
            if (!userAvatar.isEmpty()) {
                Glide.with(ProfileActivity.this)
                        .load(userAvatar)
                        .thumbnail(Glide.with(ProfileActivity.this).load(R.drawable.custom_progress))
                        .into(shapeableImageView);
            }
        }
    }

    Callback<Response<Users>> getUser = new Callback<Response<Users>>() {
        @Override
        public void onResponse(Call<Response<Users>> call, retrofit2.Response<Response<Users>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus()==200){
                    Users userData = response.body().getData();
                    updateUI(userData);

                    sharedPreferences.edit()
                            .remove(PREF_USER_NAME) // Xóa giá trị cũ của PREF_USER_NAME
                            .remove(PREF_USER_EMAIL) // Xóa giá trị cũ của PREF_USER_EMAIL
                            .remove(PREF_USER_AVATAR) // Xóa giá trị cũ của PREF_USER_AVATAR
                            .remove(PREF_NAME) // Xóa giá trị cũ của PREF_NAME
                            .remove("avatar")
                            .remove("username")
                            .remove("email")
                            .putString("avatar",userData.getAvatar())
                            .putString("username",userData.getUsername())
                            .putString("email",userData.getEmail())
                            .putString(PREF_USER_NAME, userData.getUsername()) // Đặt giá trị mới cho PREF_USER_NAME
                            .putString(PREF_USER_EMAIL, userData.getEmail()) // Đặt giá trị mới cho PREF_USER_EMAIL
                            .putString(PREF_USER_AVATAR, userData.getAvatar()) // Đặt giá trị mới cho PREF_USER_AVATAR
                            .putString(PREF_NAME, userData.getName()) // Đặt giá trị mới cho PREF_NAME
                            .apply();
                }
            }
        }
        @Override
        public void onFailure(Call<Response<Users>> call, Throwable t) {
            // Xử lý lỗi khi request thất bại
        }
    };

    private void updateUI(Users userData) {
        Glide.with(ProfileActivity.this)
                .load(userData.getAvatar())
                .thumbnail(Glide.with(ProfileActivity.this).load(R.drawable.custom_progress))
                .into(shapeableImageView);
        tvNamePF.setText(userData.getName());
        tvUserNamePF.setText(userData.getUsername());
        tvEmailPF.setText(userData.getEmail());
    }
    private void handleUp() {
        // Tạo các phần dữ liệu RequestBody cho các trường thông tin người dùng
        RequestBody requestBodyUsername = RequestBody.create(MediaType.parse("text/plain"), tvUserNamePF.getText().toString());
        RequestBody requestBodyEmail = RequestBody.create(MediaType.parse("text/plain"), tvEmailPF.getText().toString());
        RequestBody requestBodyName = RequestBody.create(MediaType.parse("text/plain"), tvNamePF.getText().toString());

        // Tạo MultipartBody.Part cho hình ảnh avatar nếu có
        MultipartBody.Part multipartBody;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            multipartBody = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
        }else {
            multipartBody = null;
        }
        httpRequest.callAPI().updateUser(id,requestBodyUsername,requestBodyEmail,requestBodyName,multipartBody).enqueue(updateUser);
}
Callback<Response<Users>> updateUser = new Callback<Response<Users>>() {
    @Override
    public void onResponse(Call<Response<Users>> call, retrofit2.Response<Response<Users>> response) {
        if(response.isSuccessful()){
            if(response.body().getStatus()==200){
                Toast.makeText(ProfileActivity.this,"Cập nhật thành công",Toast.LENGTH_LONG).show();
                sharedPreferences.edit().putBoolean(PREF_FIRST_TIME_RESUME,true).apply();
            }
        }
    }
    @Override
    public void onFailure(Call<Response<Users>> call, Throwable t) {

    }
};
}