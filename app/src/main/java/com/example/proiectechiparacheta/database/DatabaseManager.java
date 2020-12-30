package com.example.proiectechiparacheta.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.proiectechiparacheta.FidelityCard;
import com.example.proiectechiparacheta.ImageBarcode;
import com.example.proiectechiparacheta.database.dao.FidelityCardDao;
import com.example.proiectechiparacheta.database.dao.ImageBarcodeDao;

@Database(entities={FidelityCard.class, ImageBarcode.class},exportSchema = false, version =2)
public abstract class DatabaseManager extends RoomDatabase {

    private static DatabaseManager databaseManager;

    public static DatabaseManager getInstance(Context context){
        if(databaseManager==null) {
            synchronized (DatabaseManager.class) {
                if (databaseManager == null) {
                    databaseManager = Room.databaseBuilder(context, DatabaseManager.class, "db").fallbackToDestructiveMigration().build();
                }
            }
        }
        return databaseManager;
    }

    public abstract FidelityCardDao fidelityCardDao();
    public abstract ImageBarcodeDao imageBarcodeDao();
}
