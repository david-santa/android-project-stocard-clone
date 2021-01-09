package com.example.proiectechiparacheta.models;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.android.gms.vision.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@Entity(tableName = "cards")
public class FidelityCard {
    @ColumnInfo(name="id")
            @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name="name")
    public String name;
    @ColumnInfo(name="card_holder_name")
    public String cardHolderName;
    @ColumnInfo(name="barcode")
    public String barCode;
    @ColumnInfo(name="userId")
    public String userId;
    @Ignore
    public boolean isFav;

    @Ignore
    public FidelityCard(){
        this.name = "Default";
        this.cardHolderName = "Default";
        this.barCode = "Default";
        isFav=false;
    }

    public FidelityCard(int id, String name, String cardHolderName, String barCode,String userId) {
        this.id = id;
        this.name = name;
        this.cardHolderName = cardHolderName;
        this.barCode = barCode;
        this.userId = userId;
        isFav=false;
    }

    @Ignore
    public FidelityCard(String name, String cardHolderName, String barCode, String userId) {
        this.name = name;
        this.cardHolderName = cardHolderName;
        this.barCode = barCode;
        this.userId = userId;
        isFav=false;
    }

    @Ignore
    public FidelityCard(int id, String name, String cardHolderName, String barCode) {
        this.id = id;
        this.name = name;
        this.cardHolderName = cardHolderName;
        this.barCode = barCode;
        isFav=false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ", \"name\":\"" + name + "\"," +
                "\"attributes\":{" +
                "\"cardHolderName\":\"" + cardHolderName + '\"' +
                ", \"barcodeValue\":\"" + barCode + '\"' +
                "}}";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }
}
