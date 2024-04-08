package com.example.greengrove.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.greengrove.Activities.DetailActivity;
import com.example.greengrove.Activities.ProfileActivity;
import com.example.greengrove.Activities.SignIn_Screen;
import com.example.greengrove.Model.Response;
import com.example.greengrove.Model.Users;
import com.example.greengrove.R;
import com.example.greengrove.serviecs.HttpRequest;
import com.google.android.material.imageview.ShapeableImageView;

import retrofit2.Call;
import retrofit2.Callback;

public class ProfileFragment extends Fragment {
    TextView tvLogout,tvUserName,tvEmail;
    private SharedPreferences sharedPreferences;
    private ShapeableImageView shapeableImageView;
    private String avatar,username,email;
    private LinearLayout linearLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        shapeableImageView = v.findViewById(R.id.shapeableImageViewPF);
        tvUserName = v.findViewById(R.id.tvUserNameProfile);
        tvEmail = v.findViewById(R.id.tvEmailProfile);
        tvLogout = v.findViewById(R.id.tvLogout);
        linearLayout = v.findViewById(R.id.linearLayoutPF);
        onResume();
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            // Kiểm tra xem Bundle có chứa dữ liệu không
//            Users users = (Users) bundle.getSerializable("user");
//            if (users != null) {
//                // Nếu có ảnh, hiển thị ảnh bằng Glide
//                Glide.with(ProfileFragment.this)
//                        .load(users.getAvatar())
//                        .thumbnail(Glide.with(ProfileFragment.this).load(R.drawable.img_4))
//                        .into(shapeableImageView);
//                tvUserName.setText(users.getName());
//                tvEmail.setText(users.getEmail());
//            }
//        }
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Xóa tất cả dữ liệu trong SharedPreferences
                editor.apply();
                // Kết thúc hoạt động hiện tại để ngăn chặn quay lại màn hình trước đó
                getActivity().finish();

                // Chuyển đến màn hình đăng nhập với cờ FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK
                Intent intent = new Intent(getContext(), SignIn_Screen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfileActivity.class));
            }
        });
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences = getActivity().getSharedPreferences("INFO", MODE_PRIVATE);
        avatar = sharedPreferences.getString("avatar","");
        username = sharedPreferences.getString("username","");
        email = sharedPreferences.getString("email","");
        // Nếu có ảnh, hiển thị ảnh bằng Glide
        Glide.with(ProfileFragment.this)
                .load(avatar)
                .thumbnail(Glide.with(ProfileFragment.this).load(R.drawable.img_4))
                .into(shapeableImageView);
        tvUserName.setText(username);
        tvEmail.setText(email);
    }
}