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

        EditText etName = findViewById(R.id.etName);
        EditText etCardHolderName = findViewById(R.id.etCardHolderName);
        EditText etBarcodeValue = findViewById(R.id.etBarcodeValue);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String cardHolderName = etCardHolderName.getText().toString();
                String barcode = etBarcodeValue.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("name",name);
                intent.putExtra("cardHolderName",cardHolderName);
                intent.putExtra("barcode",barcode);
                setResult(1,intent);
                finish();
            }
        });
    }
}