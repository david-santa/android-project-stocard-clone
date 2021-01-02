package com.example.proiectechiparacheta.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proiectechiparacheta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    TextView tvMail;
    EditText etTelefon;
    EditText etNume;
    private static final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etTelefon = findViewById(R.id.etTelefon);

            SharedPreferences sharedPreferences
                    = getSharedPreferences("MySharedPref",
                    MODE_PRIVATE);
        SharedPreferences.Editor myEdit
                    = sharedPreferences.edit();

        System.out.println(sharedPreferences.getAll().toString());

        etNume = findViewById(R.id.etNameProfile);
        if(etNume.getText() != null && !etNume.getText().equals("")){
            myEdit.putString("nume",etNume.getText().toString());
        }
        else{
            myEdit.putString("nume","No name");
        }

        etNume.setText(sharedPreferences.getString("nume","No name"));
        if(etNume.getText().toString().isEmpty()) etNume.setText("No name");

        etTelefon.setText(sharedPreferences.getString("tel","No number"));
        if(etTelefon.getText().toString().isEmpty()) etTelefon.setText("No number");
            if(etTelefon.getText() != null && !etTelefon.getText().equals("")){
                myEdit.putString("tel",etTelefon.getText().toString());
            }
            else{
                myEdit.putString("tel","No number");
            }


        ConstraintLayout constraintLayout = findViewById(R.id.profileLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

       // Sign out method
        Button btnSignOut = findViewById(R.id.btnSignOut);
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(Profile.this, LoginActivity.class));
                    finish();
                }
            }
        };
//------------------------------------Sign out method-------------------------------------------//


        tvMail=findViewById(R.id.tvMailUser);
        if( user != null ) {
            String email = user.getEmail();
            String greeting = getString(R.string.hellomail, email);
            tvMail.setText(greeting);
        }

        Button btnDelete = findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User account deleted.");

                                }
                            }
                        });
                auth.signOut();
                Toast.makeText(getApplicationContext(), "User account deleted.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
        TextView tv =findViewById(R.id.tvMail);
        if( user != null ) {
            String email = user.getEmail();
            tv.setText(email);
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences
                = getSharedPreferences("MySharedPref",
                MODE_PRIVATE);
        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();
        myEdit.putString("tel",etTelefon.getText().toString());
        myEdit.putString("nume",etNume.getText().toString());
        myEdit.apply();
    }
}