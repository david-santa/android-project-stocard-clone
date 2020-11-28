package com.example.proiectechiparacheta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.clearbit.JSON;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class DashboardActivity extends AppCompatActivity {
    public ArrayList<FidelityCard> arrayList = new ArrayList<FidelityCard>();
    public CustomAdapter adapter;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


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
                    startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };



        ImageButton btnAdd = findViewById(R.id.btnAdd);
        ListView listView = (ListView) findViewById(R.id.customListView);


        arrayList.add(new FidelityCard(1, "IKEA", "David Santa", "123123123"));
        arrayList.add(new FidelityCard(2, "MORTIMEI", "David Santa", "32132131231"));

        adapter = new CustomAdapter(this, arrayList);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for addByCamera
                //scanBarcode(v);
                Intent intent = new Intent(DashboardActivity.this, AddByForm.class);
                intent.putExtra("requestCode", 1);
                startActivityForResult(intent, 1);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getImageForCard((FidelityCard) listView.getItemAtPosition(position));
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

            menu.add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(DashboardActivity.this, AddByForm.class);
                    intent.putExtra("requestCode", 2);
                    intent.putExtra("id", card.getId());
                    intent.putExtra("name", card.getName().toString());
                    intent.putExtra("cardHolderName", card.cardHolderName.toString());
                    intent.putExtra("barcode", card.getBarCode().toString());
                    startActivityForResult(intent, 2);
                    return false;
                }
            });
            menu.add("Show").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    getImageForCard(card);
                    return false;
                }
            });
            menu.add("Export JSON").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("id", card.getId());
                        JSONObject attributes = new JSONObject();
                        attributes.put("cardHolderName", card.getCardHolderName());
                        attributes.put("barcodeValue", card.getBarCode());
                        attributes.put("username", "username");
                        json.put("attributes", attributes);
                        Toast.makeText(getApplicationContext(), json.toString(), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
            menu.add("Delete");
        }
    }

    private void getImageForCard(FidelityCard card) {
        String url = buildUrlFromBarcodeValue(card.getBarCode().toString());
        Callable<Bitmap> asyncOperation = new HttpManager(url);
        Callback<Bitmap> mainThreadOperation = getMainThreadOperation();
        AsyncTaskRunner.executeAsync(asyncOperation, mainThreadOperation);
    }

    private String buildUrlFromBarcodeValue(String barcodeValue) {
        String s = "https://barcodes4.me/barcode/c39/" + barcodeValue + ".png";
        return s;
    }

    private Callback<Bitmap> getMainThreadOperation() {
        return new Callback<Bitmap>() {

            @Override
            public void runResultOnUiThread(Bitmap result) {
                Popup_Barcode popup_barcode = new Popup_Barcode(result);
                popup_barcode.show(getSupportFragmentManager(), "popupBarcode");
            }
        };
    }

    public void scanBarcode(View v) {
        Intent intent = new Intent(this, AddByCamera.class);
        intent.putExtra("requestCode", 3);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra("barcode");
                    Toast.makeText(this, barcode.displayValue, Toast.LENGTH_LONG);
                } else {
                    Toast.makeText(this, "No barcode found!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == 1) {
            //create a new FidelityCard
            String name = data.getStringExtra("name");
            String cardHolderName = data.getStringExtra("cardHolderName");
            String barcode = data.getStringExtra("barcode");
            arrayList.add(new FidelityCard(55, name, cardHolderName, barcode));
            adapter.notifyDataSetChanged();
        }
        if (requestCode == 2) {
            int id = data.getIntExtra("id", -1);
            String name = data.getStringExtra("name");
            String cardHolderName = data.getStringExtra("cardHolderName");
            String barcode = data.getStringExtra("barcode");
            if (id != -1)
                arrayList.get(id - 1).setName(name);
            arrayList.get(id - 1).setCardHolderName(cardHolderName);
            arrayList.get(id - 1).setBarCode(barcode);
            adapter.notifyDataSetChanged();
        }







    }
}