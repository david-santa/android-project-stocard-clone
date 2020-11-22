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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    public ArrayList<FidelityCard> arrayList = new ArrayList<FidelityCard>();
    public CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ImageButton btnAdd = findViewById(R.id.btnAdd);
        ListView listView = (ListView) findViewById(R.id.customListView);


        arrayList.add(new FidelityCard(1,"IKEA","David Santa","123123123"));
        arrayList.add(new FidelityCard(2,"MORTIMEI","David Santa","32132131231"));

        adapter = new CustomAdapter(this, arrayList);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for addByCamera
                //scanBarcode(v);
                Intent intent = new Intent(DashboardActivity.this,AddByForm.class);
                startActivityForResult(intent,1);
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
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        if(requestCode==1){
            //create a new FidelityCard
            String name = data.getStringExtra("name");
            String cardHolderName = data.getStringExtra("cardHolderName");
            String barcode = data.getStringExtra("barcode");
        arrayList.add(new FidelityCard(55,name,cardHolderName,barcode));
        adapter.notifyDataSetChanged();
        }
    }
}