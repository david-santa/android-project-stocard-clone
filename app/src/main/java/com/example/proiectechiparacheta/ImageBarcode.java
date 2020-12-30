package com.example.proiectechiparacheta;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Blob;

@Entity(tableName = "images")
public class ImageBarcode {
    @PrimaryKey(autoGenerate = true)
            @ColumnInfo(name="id")
    public int id;
    @ColumnInfo(name="image",typeAffinity = ColumnInfo.BLOB)
    public byte[] image;

    public ImageBarcode(int id, byte[] image) {
        this.id = id;
        this.image = image;
    }
}
