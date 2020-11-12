package com.example.proiectechiparacheta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ImageButton btnAdd = findViewById(R.id.imageButton);
        ListView listView = (ListView) findViewById(R.id.customListView);

        ArrayList<FidelityCard> arrayList = new ArrayList<FidelityCard>();
        arrayList.add(new FidelityCard(1,"IKEA","David Santa","123123123"));
        arrayList.add(new FidelityCard(2,"MORTIMEI","David Santa","32132131231"));
        arrayList.add(new FidelityCard(3,"TEIMORTI","David Santa","69696969"));
        arrayList.add(new FidelityCard(3,"TEIMORTI","David Santa","69696969"));
        arrayList.add(new FidelityCard(3,"TEIMORTI","David Santa","69696969"));
        arrayList.add(new FidelityCard(3,"TEIMORTI","David Santa","69696969"));
        arrayList.add(new FidelityCard(3,"TEIMORTI","David Santa","69696969"));
        arrayList.add(new FidelityCard(3,"TEIMORTI","David Santa","69696969"));
        arrayList.add(new FidelityCard(3,"TEIMORTI","David Santa","69696969"));
        arrayList.add(new FidelityCard(3,"TEIMORTI","David Santa","69696969"));

        CustomAdapter adapter = new CustomAdapter(this, arrayList);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcode(v);
            }
        });


    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        v.setLongClickable(true);
        if (v.getId() == R.id.customListView) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            FidelityCard card = (FidelityCard) lv.getItemAtPosition(acmi.position);

            menu.add("One");
            menu.add("Two");
            menu.add("Three");
            menu.add(card.name);
        }
    }

    public void scanBarcode(View v){
        Intent intent = new Intent(this,AddByCamera.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==0){
            if(resultCode== CommonStatusCodes.SUCCESS){
                if(data!=null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    Toast.makeText(this,barcode.displayValue,Toast.LENGTH_LONG);
                }
                else{
                    Toast.makeText(this,"No barcode found!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}