package com.example.proiectechiparacheta;
import com.google.android.gms.vision.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FidelityCard {
    int id;
    String name;
    String cardHolderName;
    String barCode;

    public FidelityCard(){
        this.id = -1;
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



}
