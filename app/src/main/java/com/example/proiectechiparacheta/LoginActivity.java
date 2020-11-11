package com.example.proiectechiparacheta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        ImageButton btnLogin = (ImageButton) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(etUsername.getText().toString().equals("a") && etPassword.getText().toString().equals("a")){
                    Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Wrong username or password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}