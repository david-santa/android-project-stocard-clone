package com.example.proiectechiparacheta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proiectechiparacheta.Async.AsyncTaskRunner;
import com.example.proiectechiparacheta.Async.Callback;
import com.example.proiectechiparacheta.Async.HttpManager;
import com.example.proiectechiparacheta.service.CardService;
import com.example.proiectechiparacheta.service.ImageService;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class DashboardActivity extends AppCompatActivity {
    //region DECLARATION ZONE
    public ArrayList<FidelityCard> arrayList = new ArrayList<FidelityCard>();
    public CustomAdapter adapter;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private static final String TAG = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CardService cardService;
    ImageService imageService;
    //endregion
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private int noOfFav;
    TextView tvNumber;

    private int determineNoOfFav(){
        int i = 0;
        for(FidelityCard card : arrayList){
            if(card.id==1){
                i++;
            }
        }
        return i;
    }

    private Callback<ImageBarcode> insertImageOnDbCallback(){
        return new Callback<ImageBarcode>() {
            @Override
            public void runResultOnUiThread(ImageBarcode result) {
                Log.d("DB","Image added on db");
            }
        };
    }


    private Callback<List<FidelityCard>> getAllCardsFromDbCallback(){
        return new Callback<List<FidelityCard>>() {
            @Override
            public void runResultOnUiThread(List<FidelityCard> result) {
                if(result!=null){
                    arrayList.clear();
//                    arrayList.addAll(result);
                    for(FidelityCard card:result){
                        if(card.userId.equals(user.getUid())){
                            arrayList.add(card);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                SharedPreferences sharedPreferences
                        = getSharedPreferences("MySharedPref",
                        MODE_PRIVATE);
                sharedPreferences.getInt("noOfFav",0);
                Map<String, ?> allEntries = sharedPreferences.getAll();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    if(entry.getKey().contains("fav")){
                        for(FidelityCard card : arrayList){
                            if(String.valueOf(card.id).equals(entry.getValue().toString())){
                                card.isFav=true;
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
        };
    }

    private Callback<List<FidelityCard>> getFilteredCardsFromDbCallback(){
        return new Callback<List<FidelityCard>>() {
            @Override
            public void runResultOnUiThread(List<FidelityCard> result) {
                if(result!=null){
                    arrayList.clear();
//                    arrayList.addAll(result);
                    for(FidelityCard card:result){
                        if(card.userId.equals(user.getUid())){
                            arrayList.add(card);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                if(result.isEmpty()){
                    arrayList.clear();
                    adapter.notifyDataSetChanged();
                }
                SharedPreferences sharedPreferences
                        = getSharedPreferences("MySharedPref",
                        MODE_PRIVATE);
                sharedPreferences.getInt("noOfFav",0);
                Map<String, ?> allEntries = sharedPreferences.getAll();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    if(entry.getKey().contains("fav")){
                        for(FidelityCard card : arrayList){
                            if(String.valueOf(card.id).equals(entry.getValue().toString())){
                                card.isFav=true;
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
        };
    }

    private Callback<FidelityCard> insertIntoDbCallback(){
        return new Callback<FidelityCard>() {
            @Override
            public void runResultOnUiThread(FidelityCard result) {
                if(result!=null){
                    arrayList.add(result);
                   System.out.println(result.toString());
                    adapter.notifyDataSetChanged();
                }
            }
        };
    }

    private Callback<FidelityCard> updateIntoDbCallback(){
        return new Callback<FidelityCard>() {
            @Override
            public void runResultOnUiThread(FidelityCard result) {
                for(FidelityCard card:arrayList){
                    if(card.getId() == result.getId()){
                        card.setBarCode(result.getBarCode());
                        card.setCardHolderName(result.getCardHolderName());
                        card.setName(result.getName());
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        };
    }

    private Callback<FidelityCard> deleteFromDbCallback(int position){
       return new Callback<FidelityCard>() {
           @Override
           public void runResultOnUiThread(FidelityCard result) {
               if(result!=null){
                   arrayList.remove(position);
                   adapter.notifyDataSetChanged();               }
           }
       };
    }

    Callback<Integer> getNumberOfCardsFromDbCallback(){
        return new Callback<Integer>(){

            @Override
            public void runResultOnUiThread(Integer result) {
                if(result!=-1){
                    tvNumber.setText("Number of cards: " + String.valueOf(result));
                }
            }
        };
    }

    Callback<List<ImageBarcode>> getBarcodeImage(FidelityCard card){
        return new Callback<List<ImageBarcode>>() {
            @Override
            public void runResultOnUiThread(List<ImageBarcode> result) {
                if(result.isEmpty()){
                    Log.d("From internet","a");
                    try{
                        getImageForCard(card);
                    }
                    catch (IOException e){
                        Log.d("error",e.toString());
                    }
                }
                else{
                    Log.d("From database","b");
                    ImageBarcode image = result.get(0);
                    Popup_Barcode popup_barcode = new Popup_Barcode(getImage(image.getImage()));
                    popup_barcode.show(getSupportFragmentManager(), "popupBarcode");
                }
            }
        };
    }

    //region ONCREATE

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        cardService = new CardService(getApplicationContext());
        imageService = new ImageService(getApplicationContext());
        cardService.getAll(getAllCardsFromDbCallback());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Sign out method
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
//------------------------------------Sign out method-------------------------------------------//


        ImageButton btnAdd = findViewById(R.id.btnAdd);
        ListView listView = (ListView) findViewById(R.id.customListView);
        adapter = new CustomAdapter(this, arrayList);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        ImageButton btnExport = findViewById(R.id.btnExport);
        ImageButton btnCamera = findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this,AddByCamera.class);
                startActivity(intent);
            }
        });
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toWrite = "[";
                for(int i=0;i<arrayList.size();i++){
                    toWrite+=arrayList.get(i).toString();
                    Log.d("object", arrayList.get(i).toString());
                    if(i!=arrayList.size()-1)
                    toWrite+=",";
                }
                toWrite += "]";
                writeStringAsFile(toWrite,"json.txt");
                Log.d("amscris",toWrite);
            }
        });

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
                FidelityCard card = (FidelityCard) listView.getItemAtPosition(position);
                imageService.getForId(getBarcodeImage(card),card.getId());
            }
        });

        EditText etFilter = findViewById(R.id.etFilter);
        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                cardService.getFiltered(getFilteredCardsFromDbCallback(),"%"+etFilter.getText().toString()+"%");
                Log.d("Aftertext","dasdadasdadsasd");
            }
        });
        tvNumber = findViewById(R.id.numOfCards);
        cardService.getNumber(getNumberOfCardsFromDbCallback(),user.getUid());





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

            menu.add("Add/Remove Favorite").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    SharedPreferences sharedPreferences
                            = getSharedPreferences("MySharedPref",
                            MODE_PRIVATE);
                    SharedPreferences.Editor myEdit
                            = sharedPreferences.edit();
                   card.isFav = !card.isFav;
                   if(card.isFav) {
                       noOfFav++;
                       myEdit.putInt("fav"+String.valueOf(card.getId()),card.getId());
                       myEdit.putInt("noOfFav",noOfFav);
                       FidelityCard tempCard = card;
                       arrayList.remove(card);
                       arrayList.add(0, card);
                       myEdit.commit();
                   }
                   else{
                       myEdit.remove("fav"+String.valueOf(card.getId()));
                       noOfFav--;
                       myEdit.putInt("noOfFav",noOfFav);
                       myEdit.commit();
                   }
                   adapter.notifyDataSetChanged();
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

    public void writeStringAsFile(String fileContents, String fileName) {
        Context context = DashboardActivity.this;
        try {
            FileWriter out = new FileWriter(new File("/data/data/com.example.proiectechiparacheta/files/json.txt"));
            out.write(fileContents);
            out.close();
        } catch (IOException e) {
            Log.d("error",e.toString());
        }
    }

    public String readFileAsString(String fileName) {
        Context context = DashboardActivity.this;
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        try (BufferedReader in = new BufferedReader(new FileReader(new File("/data/data/com.example.proiectechiparacheta/files/json.txt")))){
            Log.d("ceva",context.getFilesDir().toString());
            while ((line = in.readLine()) != null) stringBuilder.append(line);
        } catch (FileNotFoundException e) {
           Log.d("error",e.toString());
        } catch (IOException e) {
            Log.d("error",e.toString());
        }
        Log.d("stringCitit",stringBuilder.toString());
        return stringBuilder.toString();
    }

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
            Log.d("ceva","altceva");
            //Daca exista imaginea in baza de date o vom lua de acolo. Daca nu exista, o vom lua din API si o vom adauga in baza de date

            String url = buildUrlFromBarcodeValue(card.getBarCode().toString());
                Callable<Bitmap> asyncOperation = new HttpManager(url);
            Callback<Bitmap> mainThreadOperation = getMainThreadOperation(card.id);
            AsyncTaskRunner.executeAsync(asyncOperation, mainThreadOperation);
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
                imageService.insert(insertImageOnDbCallback(),new ImageBarcode(blob,id));
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
        int index = getArraylistIndex(card.id);
        cardService.delete(deleteFromDbCallback(index),arrayList.get(index));
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
        int index = getArraylistIndex(id);
        if(index!=-1){
            arrayList.get(index).setName(name);
            arrayList.get(index).setCardHolderName(cardHolderName);
            arrayList.get(index).setBarCode(barcode);
        }
        adapter.notifyDataSetChanged();
        cardService.update(updateIntoDbCallback(),arrayList.get(index));
    }


    private void createCard(Intent data) {
        String name = data.getStringExtra("name");
        String cardHolderName = data.getStringExtra("cardHolderName");
        String barcode = data.getStringExtra("barcode");
        String userId = user.getUid();
        cardService.insert(insertIntoDbCallback(),new FidelityCard(name,cardHolderName,barcode,userId));
    }

    public List<FidelityCard> getCardsFromJson(){
        return JSONParser.fromJson(readFileAsString("json.txt"));
    }

    //endregion

}