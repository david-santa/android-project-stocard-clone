package com.example.proiectechiparacheta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLiteHelper";
    private static final String TABLE_NAME_1 = "card";
    private static final String CARD_COL1 = "ID_CARD";
    private static final String CARD_COL2 = "card_name";
    private static final String CARD_COL3 = "card_holder_name";
    private static final String CARD_COL4 = "barcode_value";
    private static final String TABLE_NAME_2 = "barcode";
    private static final String BARCODE_COL1 = "ID";
    private static final String BARCODE_COL2 = "image";

    public SQLiteHelper(@Nullable Context context) {
        super(context, TABLE_NAME_1, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableCard = "CREATE TABLE " + TABLE_NAME_1 + " (ID_CARD INTEGER PRIMARY KEY AUTOINCREMENT, " + CARD_COL2 + " TEXT, " + CARD_COL3 + " TEXT, " + CARD_COL4 + " TEXT)";
        String createTableBarcode = "CREATE TABLE " + TABLE_NAME_2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + BARCODE_COL2 + " BLOB, ID_CARD INTEGER, FOREIGN KEY(ID_CARD) REFERENCES CARD(ID_CARD));";
        db.execSQL(createTableCard);
        db.execSQL(createTableBarcode);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME_1);
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME_2);
        onCreate(db);
    }

    public boolean addCard(String cardName, String cardholderName, String barcodeValue){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CARD_COL2, cardName);
        contentValues.put(CARD_COL3, cardholderName);
        contentValues.put(CARD_COL4, barcodeValue);

        int result = (int) db.insert(TABLE_NAME_1,null,contentValues);
        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }


    public Cursor getCards(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_1, null);
        return data;
    }
    public int getCardId(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + CARD_COL1 + " FROM " + TABLE_NAME_1 + " WHERE " + CARD_COL2 + "='" + name + "'";
        Cursor data = db.rawQuery(query,null);
        return data.getInt(1);
    }

    public void updateCard(String newName, String newCardholderName, String newBarcodeValue, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE "+ TABLE_NAME_1 + " SET " + CARD_COL2 + " = " + "'" + newName + "'" + "," + CARD_COL3 + " = " + "'"+ newCardholderName + "'" + "," + CARD_COL4 + " = " + "'" + newBarcodeValue + "'" + " WHERE "+CARD_COL1 + "=" + id;
        db.execSQL(query);
    }

    public void deleteCard(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_1 + " WHERE "
                + CARD_COL1 + "=" + id;
        db.execSQL(query);
    }
}
