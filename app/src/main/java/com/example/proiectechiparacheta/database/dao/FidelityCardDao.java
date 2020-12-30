package com.example.proiectechiparacheta.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proiectechiparacheta.FidelityCard;

import java.util.List;

@Dao
public interface FidelityCardDao {

    @Query("select * from cards")
    List<FidelityCard> getAllCards();

    @Insert
    long insert(FidelityCard card);

    @Update
    int update(FidelityCard card);

    @Delete
    int delete(FidelityCard card);
}
