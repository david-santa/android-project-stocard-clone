package com.example.proiectechiparacheta;
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

    @Ignore
    public FidelityCard(){
        this.id = 2;
        this.name = "Default";
        this.cardHolderName = "Default";
        this.barCode = "Default";
    }


    public FidelityCard(int id, String name, String cardHolderName, String barCode) {
        this.id = id;
        this.name = name;
        this.cardHolderName = cardHolderName;
        this.barCode = barCode;
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
}
