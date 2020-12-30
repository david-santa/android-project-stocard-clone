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

    @Query("select * from cards where name LIKE :filter")
    List<FidelityCard> getFilteredCards(String filter);

    @Query("select count(*) from cards where userId = :uid")
    int getNumberOfCards(String uid);

    @Insert
    long insert(FidelityCard card);

    @Update
    int update(FidelityCard card);

    @Delete
    int delete(FidelityCard card);
}
