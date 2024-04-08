package com.example.greengrove.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greengrove.MainActivity;
import com.example.greengrove.Model.Users;
import com.example.greengrove.R;
import com.example.greengrove.serviecs.HttpRequest;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import com.example.greengrove.Model.Response;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignIn_Screen extends AppCompatActivity {
    TextView tvCreate, tvError;
    EditText edUserName, edPassword;
    Button btnSignin;
    private HttpRequest httpRequest;
    private ProgressBar progressBar;
    private ConstraintLayout constraintLayout;
    private ImageButton imageButton;
    Animation slideOut;

    FirebaseAuth mAuth;
    GoogleSignInClient googleSignInClient;

    private final ActivityResultLauncher<Intent> activityResultLauncher =registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if(o.getResultCode() ==RESULT_OK){
                        Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(o.getData());
                        try {
                        GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                        String email = signInAccount.getEmail();
                        String name = signInAccount.getDisplayName();
                        String photo = signInAccount.getPhotoUrl().toString();
                            Log.d("LoginActivity", "Email: " + email);
                            Log.d("LoginActivity", "Name: " + name);
                            Log.d("LoginActivity", "Photo URL: " + photo);
                            httpRequest = new HttpRequest();
                            httpRequest.callAPI().loginGoogle(photo,email,name).enqueue(getGG);

                            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
                            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
                            googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Đăng xuất thành công, thực hiện các hành động khác nếu cần
                                    } else {
                                        // Đăng xuất không thành công, xử lý lỗi nếu cần
                                    }
                                }
                            });
                        }catch (ApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
    Callback<Response<Users>> getGG = new Callback<Response<Users>>() {
        @Override
        public void onResponse(Call<Response<Users>> call, retrofit2.Response<Response<Users>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus()==200){
                    SharedPreferences sharedPreferences = getSharedPreferences("INFO", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", response.body().getToken());
                    editor.putString("refreshToken", response.body().getRefreshToken());
                    editor.putString("id", response.body().getData().get_id());
                    editor.putString("avatar", response.body().getData().getAvatar());
                    editor.putString("username", response.body().getData().getUsername());
                    editor.putString("email", response.body().getData().getEmail());
                    editor.apply();
                    Toast.makeText(SignIn_Screen.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                    tvError.setVisibility(View.GONE);
                    progressBar.startAnimation(slideOut);
                    progressBar.setVisibility(View.GONE);
                    constraintLayout.setAlpha(1.0f);
                    startActivity(new Intent(SignIn_Screen.this, MainActivity.class));
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Users>> call, Throwable t) {
            Log.d(">>> login Fail", "onFail" + t.getMessage());
            Toast.makeText(SignIn_Screen.this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
            tvError.setVisibility(View.VISIBLE);
            progressBar.startAnimation(slideOut);
            progressBar.setVisibility(View.GONE);
            constraintLayout.setAlpha(1.0f);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);
        FirebaseApp.initializeApp(this);
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                                .build();
        googleSignInClient = GoogleSignIn.getClient(SignIn_Screen.this,options);
        mAuth = FirebaseAuth.getInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        tvCreate = findViewById(R.id.tvcreate);
        tvError = findViewById(R.id.error);
        edUserName = findViewById(R.id.edUsernameDN);
        edPassword = findViewById(R.id.edPasswordDN);
        progressBar = findViewById(R.id.progressBarSignIn);
        constraintLayout = findViewById(R.id.MainSignIn);
        imageButton = findViewById(R.id.imgbtnGGSI);
        // Load animation
        Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_progressbar);
        slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.progress_bar_out);
        httpRequest = new HttpRequest();
        btnSignin = findViewById(R.id.btnSignin);

        tvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn_Screen.this, SignUp_Screen.class);
                startActivity(intent);
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Users users = new Users();
                String _username = edUserName.getText().toString().trim();
                String _password = edPassword.getText().toString().trim();
                users.setUsername(_username);
                users.setPassword(_password);
                httpRequest.callAPI().login(users).enqueue(responseUser);
                progressBar.startAnimation(slideIn);
                progressBar.setVisibility(View.VISIBLE);
                constraintLayout.setAlpha(0.5f);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });
    }

    Callback<Response<Users>> responseUser = new Callback<Response<Users>>() {
        @Override
        public void onResponse(Call<Response<Users>> call, retrofit2.Response<Response<Users>> response) {
            if (response.isSuccessful()) {

                //check status code
                if (response.body().getStatus() == 200) {
                    SharedPreferences sharedPreferences = getSharedPreferences("INFO", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", response.body().getToken());
                    editor.putString("refreshToken", response.body().getRefreshToken());
                    editor.putString("id", response.body().getData().get_id());
                    editor.putString("avatar", response.body().getData().getAvatar());
                    editor.putString("username", response.body().getData().getUsername());
                    editor.putString("email", response.body().getData().getEmail());
                    editor.apply();
                    Toast.makeText(SignIn_Screen.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                    tvError.setVisibility(View.GONE);
                    progressBar.startAnimation(slideOut);
                    progressBar.setVisibility(View.GONE);
                    constraintLayout.setAlpha(1.0f);
                    startActivity(new Intent(SignIn_Screen.this, MainActivity.class));
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Users>> call, Throwable t) {
            Log.d(">>> login Fail", "onFail" + t.getMessage());
            Toast.makeText(SignIn_Screen.this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
            tvError.setVisibility(View.VISIBLE);
            progressBar.startAnimation(slideOut);
            progressBar.setVisibility(View.GONE);
            constraintLayout.setAlpha(1.0f);
        }


    };
}