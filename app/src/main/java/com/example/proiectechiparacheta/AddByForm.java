package com.example.proiectechiparacheta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddByForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_by_form);
        Intent intent = getIntent();

        EditText etName = findViewById(R.id.etName);
        EditText etCardHolderName = findViewById(R.id.etCardHolderName);
        EditText etBarcodeValue = findViewById(R.id.etBarcodeValue);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);

        if(intent.getIntExtra("requestCode",-1)==2){
            etName.setText(intent.getStringExtra("name"));
            etCardHolderName.setText(intent.getStringExtra("cardHolderName"));
            etBarcodeValue.setText(intent.getStringExtra("barcode"));
        }

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
}