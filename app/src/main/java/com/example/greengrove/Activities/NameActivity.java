package com.example.greengrove.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.greengrove.R;

public class NameActivity extends AppCompatActivity {
    private ImageButton imgbtnBack;
    private EditText edName;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        imgbtnBack = findViewById(R.id.imgBackName);
        edName = findViewById(R.id.editTextName);
        button = findViewById(R.id.btnLuu);
        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edName.getText().toString().trim().isEmpty()){
                    Toast.makeText(NameActivity.this,"Vui lòng nhập tên mới",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(NameActivity.this,ProfileActivity.class);
                    intent.putExtra("name",edName.getText().toString().trim());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}