package com.example.greengrove.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greengrove.Model.Users;
import com.example.greengrove.R;
import com.example.greengrove.serviecs.HttpRequest;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;

//import retrofit2.Response;
import com.example.greengrove.Model.Response;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SignUp_Screen extends AppCompatActivity {
    TextInputLayout txtlayoutusername,txtlayoutemail,txtlayoutname,txtlayoutpassword;
    EditText username, password, email, name;
    TextView tvDN;
    Button btnSignup;
    ImageButton imgbtnGG,imgbtnFB;
    File file;
    CheckBox chk;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private HttpRequest httpRequest;
    private boolean isUsernameValid = false;
    private boolean isEmailValid = false;
    private boolean isNameValid = false;
    private boolean isPasswordValid = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        initializeViews();
        setListeners();
    }
    private void initializeViews() {
        httpRequest = new HttpRequest();
        txtlayoutusername = findViewById(R.id.txtlayoutusername);
        txtlayoutemail = findViewById(R.id.txtlayoutEmail);
        txtlayoutname = findViewById(R.id.txtlayoutName);
        txtlayoutpassword = findViewById(R.id.txtlayoutPassword);
        username = findViewById(R.id.edUserName);
        password = findViewById(R.id.edPassword);
        email = findViewById(R.id.edEmail);
        name = findViewById(R.id.edName);
        tvDN = findViewById(R.id.tvDN);
        btnSignup = findViewById(R.id.btnSignup);
        imgbtnGG = findViewById(R.id.imgbtnGG);
        imgbtnFB = findViewById(R.id.imgbtnFB);
        chk = findViewById(R.id.chkbox);
        // Tạo một SpannableString cho văn bản của CheckBox
        SpannableString spannableString = new SpannableString("Để đăng ký tài khoản, bạn đồng ý Term & Conditions and Privacy Policy");
        // Định dạng phần được gạch chân và đổi màu cho "Term & Conditions"
        int startIndex1 = spannableString.toString().indexOf("Term & Conditions");
        int endIndex1 = startIndex1 + "Term & Conditions".length();

        if (startIndex1 != -1 && endIndex1 != -1) {
            spannableString.setSpan(new UnderlineSpan(), startIndex1, endIndex1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), startIndex1, endIndex1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

// Định dạng phần được gạch chân và đổi màu cho "Privacy Policy"
        int startIndex2 = spannableString.toString().indexOf("Privacy Policy");
        int endIndex2 = startIndex2 + "Privacy Policy".length();
        if (startIndex2 != -1 && endIndex2 != -1) {
            spannableString.setSpan(new UnderlineSpan(), startIndex2, endIndex2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), startIndex2, endIndex2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
// Đặt văn bản đã định dạng vào CheckBox
        chk.setText(spannableString);
        chk.setChecked(true);
    }
    private void setListeners() {
        // TextWatcher cho username
        username.addTextChangedListener(getTextWatcher(txtlayoutusername, s -> s.length() >=6, ""));
        // TextWatcher cho email
        email.addTextChangedListener(getTextWatcher(txtlayoutemail, this::isValidEmail, ""));
        // TextWatcher cho name
        name.addTextChangedListener(getTextWatcher(txtlayoutname, s -> s.length() >=6, ""));
        // TextWatcher cho password
        password.addTextChangedListener(getTextWatcher(txtlayoutpassword, this::isValidPassword, ""));
        // Xử lý sự kiện click nút đăng ký
        btnSignup.setOnClickListener(v -> {
            if (isUsernameValid && isEmailValid && isNameValid && isPasswordValid && chk.isChecked()) {
                // Xử lý đăng ký tài khoản
                handleSignUp();
            } else {
                // Hiển thị thông báo khi không thỏa mãn các yêu cầu
                Toast.makeText(SignUp_Screen.this, "Vui lòng hoàn thành tất cả các trường và đồng ý với điều khoản", Toast.LENGTH_LONG).show();
            }
        });
        tvDN.setOnClickListener(v ->{
            startActivity(new Intent(SignUp_Screen.this,SignIn_Screen.class));
        });
        imgbtnGG.setOnClickListener(v -> {

        });
        imgbtnFB.setOnClickListener(v -> {

        });
    }

    private boolean isValidEmail(CharSequence charSequence) {
        // Chuyển đổi CharSequence thành String
        String email = charSequence.toString();

        // Kiểm tra xem chuỗi email có rỗng không và có đúng định dạng không
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(CharSequence charSequence) {
        // Chuyển đổi CharSequence thành String
        String password = charSequence.toString();

        // Kiểm tra xem mật khẩu có ít nhất 8 ký tự, một chữ hoa và một ký tự đặc biệt hay không
        String passwordPattern = "^(?=.*[A-Z])(?=.*[!@#$%^&*()-+])(?=\\S+$).{8,}$";
        return password.matches(passwordPattern);
    }

    private TextWatcher getTextWatcher(TextInputLayout layout, Validator validator, String errorMessage) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (validator.isValid(s)) {
                    layout.setBoxStrokeColor(ContextCompat.getColor(SignUp_Screen.this, R.color.green));
                    updateValidationStatus(layout, true);
                } else {
                    layout.setBoxStrokeColor(ContextCompat.getColor(SignUp_Screen.this, R.color.red));
                    updateValidationStatus(layout, false);
                }
            }
        };
    }

    private interface Validator {
        boolean isValid(CharSequence s);
    }

    private void updateValidationStatus(TextInputLayout layout, boolean isValid) {
        if (layout == txtlayoutusername) {
            isUsernameValid = isValid;
        } else if (layout == txtlayoutemail) {
            isEmailValid = isValid;
        } else if (layout == txtlayoutname) {
            isNameValid = isValid;
        } else if (layout == txtlayoutpassword) {
            isPasswordValid = isValid;
        }
    }
    private void handleSignUp() {
        //Sử dụng requestBody
        RequestBody _username = RequestBody.create(MediaType.parse("multipart/form-data"), username.getText().toString());
        RequestBody _password = RequestBody.create(MediaType.parse("multipart/form-data"), password.getText().toString());
        RequestBody _email = RequestBody.create(MediaType.parse("multipart/form-data"), email.getText().toString());
        RequestBody _name = RequestBody.create(MediaType.parse("multipart/form-data"), name.getText().toString());
        MultipartBody.Part multipartBody;
        if (file != null) {
            multipartBody = null;
//            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
//            multipartBody = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
        } else {
            multipartBody = null;
        }

        // Gọi API đăng ký
        httpRequest.callAPI().register(_username, _password, _email, _name, multipartBody).enqueue(responseUser);
    }
    Callback<Response<Users>> responseUser = new Callback<Response<Users>>() {
        @Override
        public void onResponse(Call<Response<Users>> call, retrofit2.Response<Response<Users>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus()==200){
                    Toast.makeText(SignUp_Screen.this,"Đăng ký thành công",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignUp_Screen.this,SignIn_Screen.class));
                }
            }
        };
        @Override
        public void onFailure(Call<Response<Users>> call, Throwable t) {
            Log.d(">>>>Get","onFailure"+t.getMessage());
        }
    };
};
//// Hiển thị hộp thoại cảnh báo chỉ có thông báo
//            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp_Screen.this);
//                    builder.setMessage("Đăng ký thành công! Đang chuyển sang trang đăng nhập...");
//                    builder.setCancelable(false); // Ngăn người dùng đóng hộp thoại bằng cách nhấn ra ngoài
//
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//
//                    // Tự động chuyển sang trang đăng nhập sau một khoảng thời gian nhất định
//                    new Handler().postDelayed(new Runnable() {
//@Override
//public void run() {
//        // Đoạn mã để chuyển sang trang đăng nhập ở đây (ví dụ: Intent)
//
//        // Ví dụ:
//        Intent intent = new Intent(SignUp_Screen.this, LoginActivity.class);
//        startActivity(intent);
//        finish(); // Kết thúc activity hiện tại để người dùng không thể quay lại từ trang đăng nhập
//        }
//        }, 3000); // Thời gian chờ trước khi chuyển trang (3 giây trong ví dụ này)
//        }