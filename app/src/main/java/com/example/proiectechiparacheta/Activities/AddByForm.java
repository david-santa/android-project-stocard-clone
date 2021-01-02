package com.example.proiectechiparacheta.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.proiectechiparacheta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddByForm extends AppCompatActivity {

    private static final String TAG = "";
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String UserID = user.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_by_form);
        Intent intent = getIntent();

        EditText etName = findViewById(R.id.etName);
        EditText etCardHolderName = findViewById(R.id.etCardHolderName);
        EditText etBarcodeValue = findViewById(R.id.etBarcodeValue);
        TextView etcN = findViewById(R.id.cN);
        TextView etcHn = findViewById(R.id.cHN);
        TextView etbC = findViewById(R.id.bC);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);

        if(intent.getIntExtra("requestCode",-1)==2){
            etName.setText(intent.getStringExtra("name"));
            etCardHolderName.setText(intent.getStringExtra("cardHolderName"));
            etBarcodeValue.setText(intent.getStringExtra("barcode"));
            etcN.setText(intent.getStringExtra("name"));
            etbC.setText(intent.getStringExtra("barcode"));
            etcHn.setText(intent.getStringExtra("cardHolderName"));
        }

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etcN.setText(etName.getText().toString());
            }
        });

        etCardHolderName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etcHn.setText(etCardHolderName.getText().toString());
            }
        });

        etBarcodeValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etbC.setText(etBarcodeValue.getText().toString());
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Intent intent = new Intent(AddByForm.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                if(intent.getIntExtra("requestCode",-1)==-1) finish();
                if(intent.getIntExtra("requestCode",-1)==1){
                    String name = etName.getText().toString();
                    String cardHolderName = etCardHolderName.getText().toString();
                    String barcode = etBarcodeValue.getText().toString();
                    Intent intent2 = new Intent();
                    intent2.putExtra("name",name);
                    intent2.putExtra("cardHolderName",cardHolderName);
                    intent2.putExtra("barcode",barcode);
                    setResult(1,intent2);
                    finish();
                }
                if(intent.getIntExtra("requestCode",-1)==2){
                    String name = etName.getText().toString();
                    String cardHolderName = etCardHolderName.getText().toString();
                    String barcode = etBarcodeValue.getText().toString();
                    Intent intent2 = new Intent();
                    intent2.putExtra("name",name);
                    intent2.putExtra("cardHolderName",cardHolderName);
                    intent2.putExtra("barcode",barcode);
                    intent2.putExtra("id",intent.getIntExtra("id",-1));
                    setResult(2,intent2);
                    finish();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
    }
}