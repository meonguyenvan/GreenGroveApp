package com.example.greengrove.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.example.greengrove.MainActivity;
import com.example.greengrove.R;

import java.util.logging.LogRecord;

public class Spash_Screen extends AppCompatActivity {
    private ProgressBar progressBar;
    private static final long DELAY_TIME = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        progressBar = findViewById(R.id.progressBar);
        progressBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Spash_Screen.this, SignIn_Screen.class);
                startActivity(intent);
                finish();
            }
        },DELAY_TIME);
//        // Lấy WindowInsetsController từ Window của Activity
//        WindowInsetsController insetsController = getWindow().getInsetsController();
//
//        // Kiểm tra nếu có hỗ trợ ẩn status bars
//        if (insetsController != null) {
//            // Ẩn thanh trạng thái
//            insetsController.hide(WindowInsets.Type.statusBars());
//        }
    }
}