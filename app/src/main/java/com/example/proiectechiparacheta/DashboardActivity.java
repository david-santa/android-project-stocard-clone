package com.example.proiectechiparacheta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proiectechiparacheta.Async.AsyncTaskRunner;
import com.example.proiectechiparacheta.Async.Callback;
import com.example.proiectechiparacheta.Async.HttpManager;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class DashboardActivity extends AppCompatActivity {
    //region DECLARATION ZONE
    public ArrayList<FidelityCard> arrayList = new ArrayList<FidelityCard>();
    public CustomAdapter adapter;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private static final String TAG = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    SQLiteHelper sqLiteHelper;
    //endregion

    //region SQLite
    public void addCard(String cardName, String cardholderName, String barcodeValue){
        boolean insertData = sqLiteHelper.addCard(cardName,cardholderName,barcodeValue);
        if(insertData){
            Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void getCards(){
        arrayList = new ArrayList<FidelityCard>();
        Cursor data = sqLiteHelper.getCards();
        while(data.moveToNext()) {
            int id= Integer.parseInt(data.getString(0));
            String cardName = data.getString(1);
            String cardholderName = data.getString(2);
            String barcodeValue = data.getString(3);
            arrayList.add(new FidelityCard(id,cardName,cardholderName,barcodeValue));
        }
    }
    //endregion

    //region ONCREATE

    public List<FidelityCard> getCardsFromJson(){
        String json = "[\n" +
                "  {\n" +
                "    \"id\": 4,\n" +
                "    \"name\": \"IKEA\",\n" +
                "    \"attributes\": {\n" +
                "      \"cardHolderName\": \"Andutzu Dragutzu\",\n" +
                "      \"barcodeValue\": \"muieTimofte\",\n" +
                "      \"username\": \"ionutcopilfrumos@gmail.com\"\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 3,\n" +
                "    \"name\": \"ANDU\",\n" +
                "    \"attributes\": {\n" +
                "      \"cardHolderName\": \"SUGE PULA\",\n" +
                "      \"barcodeValue\": \"cumasa\",\n" +
                "      \"username\": \"siCuTacsuMustaciosu@gmail.com\"\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 2,\n" +
                "    \"name\": \"PizdaMeaEGrasa\",\n" +
                "    \"attributes\": {\n" +
                "      \"cardHolderName\": \"Mortii mei sunt morti\",\n" +
                "      \"barcodeValue\": \"bagPulasamiBag\",\n" +
                "      \"username\": \"davidGrasuPuliiMele@gmail.com\"\n" +
                "    }\n" +
                "  }\n" +
                "]";
        return JSONParser.fromJson(json);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sqLiteHelper = new SQLiteHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        //Sign out method
        Button btnSignOut = findViewById(R.id.btnSignOut);
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        //addCard("cardTest","test","testBarcode");

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
//------------------------------------Sign out method-------------------------------------------//






        ImageButton btnAdd = findViewById(R.id.btnAdd);
        ListView listView = (ListView) findViewById(R.id.customListView);
        arrayList = (ArrayList<FidelityCard>) getCardsFromJson();
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
                try {
                    getImageForCard((FidelityCard) listView.getItemAtPosition(position));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    //endregion

    //region CONTEXT MENU

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        v.setLongClickable(true);
        if (v.getId() == R.id.customListView) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            FidelityCard card = (FidelityCard) lv.getItemAtPosition(acmi.position);

            //  ============== EDIT  ==============

            menu.add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    editCard(card);
                    return false;
                }
            });

            //  ============== SHOW  ==============

            menu.add("Show").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    try {
                        getImageForCard(card);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });

            //  ============== EXPORT JSON  ==============
            menu.add("Export JSON").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    exportCard(card);
                    return false;
                }
            });

            //  ============== DELETE  ==============

            menu.add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    deleteCard(card);
                    return false;
                }
            });
        }
    }

    //endregion

    //region ONACTIVITYRESULT

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
            createCard(data);
        }
        if (requestCode == 2) {
            //edit an existing FidelityCard
            editCard(data);
        }
    }

    //endregion

    //region UTILITARIES
    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    private void getImageForCard(FidelityCard card) throws IOException {
        Cursor c = sqLiteHelper.getImage(card.id);
        if(c.getCount()>0){
            Log.d("ceva","ceva");
            c.moveToFirst();
            byte[] blob = c.getBlob(1);
            Bitmap image = getImage(blob);
            Popup_Barcode popup_barcode = new Popup_Barcode(image);
            popup_barcode.show(getSupportFragmentManager(), "popupBarcode");
        }
        else {
            Log.d("ceva","altceva");
            //Daca exista imaginea in baza de date o vom lua de acolo. Daca nu exista, o vom lua din API si o vom adauga in baza de date
            String url = buildUrlFromBarcodeValue(card.getBarCode().toString());
                Callable<Bitmap> asyncOperation = new HttpManager(url);
            Callback<Bitmap> mainThreadOperation = getMainThreadOperation(card.id);
            AsyncTaskRunner.executeAsync(asyncOperation, mainThreadOperation);
        }
    }

    private String buildUrlFromBarcodeValue(String barcodeValue) {
        String s = "https://barcodes4.me/barcode/c39/" + barcodeValue + ".png";
        return s;
    }

    private Callback<Bitmap> getMainThreadOperation(int id) {
        return new Callback<Bitmap>() {

            @Override
            public void runResultOnUiThread(Bitmap result) {
                Popup_Barcode popup_barcode = new Popup_Barcode(result);
                popup_barcode.show(getSupportFragmentManager(), "popupBarcode");
                byte[] blob = getBytes(result);
                sqLiteHelper.addImage(blob,id);
            }
        };
    }

    private int getArraylistIndex(int id) {
        for(int i=0;i<arrayList.size();i++){
            if(arrayList.get(i).id==id)
                return i;
        }
        return -1;
    }


    public void scanBarcode(View v) {
        Intent intent = new Intent(this, AddByCamera.class);
        intent.putExtra("requestCode", 3);
        startActivityForResult(intent, 3);
    }

    private void deleteCard(FidelityCard card) {
        sqLiteHelper.deleteCard(card.getId());
        int index = getArraylistIndex(card.id);
        arrayList.remove(index);
        adapter.notifyDataSetChanged();
    }

    private void exportCard(FidelityCard card) {
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
    }

    private void editCard(FidelityCard card) {
        Intent intent = new Intent(DashboardActivity.this, AddByForm.class);
        intent.putExtra("requestCode", 2);
        intent.putExtra("id", card.getId());
        intent.putExtra("name", card.getName().toString());
        intent.putExtra("cardHolderName", card.cardHolderName.toString());
        intent.putExtra("barcode", card.getBarCode().toString());
        startActivityForResult(intent, 2);
    }

    private void editCard(Intent data) {
        FidelityCard card = new FidelityCard();
        int id = data.getIntExtra("id", -1);
        String name = data.getStringExtra("name");
        String cardHolderName = data.getStringExtra("cardHolderName");
        String barcode = data.getStringExtra("barcode");
        if (id != -1){
            sqLiteHelper.updateCard(name,cardHolderName,barcode,id);
        }
        int index = getArraylistIndex(id);
        if(index!=-1){
            arrayList.get(index).setName(name);
            arrayList.get(index).setCardHolderName(cardHolderName);
            arrayList.get(index).setBarCode(barcode);
        }
        adapter.notifyDataSetChanged();
    }

    private void createCard(Intent data) {
        int id=-1;
        String name = data.getStringExtra("name");
        String cardHolderName = data.getStringExtra("cardHolderName");
        String barcode = data.getStringExtra("barcode");
        addCard(name,cardHolderName,barcode);
        Cursor c = sqLiteHelper.getCards();
        while(c.moveToNext()){
            //Log.d("ceva",c.getString(1) + " , " + name);
            if(c.getString(1).equals(name)){
                id=c.getInt(0);
            }
        }
        arrayList.add(new FidelityCard(id, name, cardHolderName, barcode));
        adapter.notifyDataSetChanged();
    }

    //endregion

}