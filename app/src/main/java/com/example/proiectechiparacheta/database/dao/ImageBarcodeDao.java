package com.example.proiectechiparacheta.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proiectechiparacheta.FidelityCard;
import com.example.proiectechiparacheta.ImageBarcode;

import java.util.List;

@Dao
public interface ImageBarcodeDao {

    @Query("select * from images")
    List<ImageBarcode> getAllImages();

    @Insert
    long insert(ImageBarcode image);

    @Update
    int update(ImageBarcode image);

    @Delete
    int delete(ImageBarcode image);
}
