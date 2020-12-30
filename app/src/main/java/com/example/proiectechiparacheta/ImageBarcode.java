package com.example.proiectechiparacheta;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Blob;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "images", foreignKeys = @ForeignKey(entity = FidelityCard.class, parentColumns = "id", childColumns = "cardId", onDelete = CASCADE))
public class ImageBarcode {
    @PrimaryKey(autoGenerate = true)
            @ColumnInfo(name="id")
    public int id;
    @ColumnInfo(name="image",typeAffinity = ColumnInfo.BLOB)
    public byte[] image;
    @ColumnInfo(name="cardId")
    public int cardId;

    public ImageBarcode(int id, byte[] image, int userId) {
        this.id = id;
        this.image = image;
        this.cardId = cardId;
    }

    @Ignore
    public ImageBarcode(byte[] image){
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }
}
