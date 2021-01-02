package com.example.proiectechiparacheta.service;

import android.content.Context;

import com.example.proiectechiparacheta.Async.AsyncTaskRunner;
import com.example.proiectechiparacheta.Async.Callback;
import com.example.proiectechiparacheta.models.ImageBarcode;
import com.example.proiectechiparacheta.database.DatabaseManager;
import com.example.proiectechiparacheta.database.dao.ImageBarcodeDao;

import java.util.List;
import java.util.concurrent.Callable;

public class ImageService {
    private final ImageBarcodeDao imageBarcodeDao;
    private final AsyncTaskRunner taskRunner;

    public ImageService(Context context) {
        this.imageBarcodeDao = DatabaseManager.getInstance(context).imageBarcodeDao();
        this.taskRunner = new AsyncTaskRunner();
    }

    public void getAll(Callback<List<ImageBarcode>> callback){
        Callable<List<ImageBarcode>> callable = new Callable<List<ImageBarcode>>() {
            @Override
            public List<ImageBarcode> call() throws Exception {
                return imageBarcodeDao.getAllImages();
            }
        };
        taskRunner.executeAsync(callable,callback);
    }

    public void insert(Callback<ImageBarcode> callback, ImageBarcode image){
        Callable<ImageBarcode> callable = new Callable<ImageBarcode>(){

            @Override
            public ImageBarcode call() throws Exception {
                if(image==null){
                    return null;
                }
                long id = imageBarcodeDao.insert(image);
                if(id==-1){
                    return null;
                }
                image.setId((int)id);
                return image;
            }
        };
        taskRunner.executeAsync(callable,callback);
    }

    public void update(Callback<ImageBarcode> callback, ImageBarcode image){
        Callable<ImageBarcode> callable = new Callable<ImageBarcode>() {
            @Override
            public ImageBarcode call() throws Exception {
                if(image==null){
                    return null;
                }
                int count = imageBarcodeDao.update(image);
                if(count!=1){
                    return null;
                }
                return image;
            }
        };
        taskRunner.executeAsync(callable,callback);
    }

    public void delete(Callback<ImageBarcode>callback, ImageBarcode image){
        Callable<ImageBarcode> callable = new Callable<ImageBarcode>() {
            @Override
            public ImageBarcode call() throws Exception {
                if(image == null){
                    return null;
                }
                imageBarcodeDao.delete(image);
                return image;
            }
        };
        taskRunner.executeAsync(callable,callback);
    }

    public void getForId(Callback<List<ImageBarcode>> callback, int cId){
        Callable<List<ImageBarcode>> callable = new Callable<List<ImageBarcode>>() {
            @Override
            public List<ImageBarcode> call() throws Exception {
                if(cId!=-1)
                    return imageBarcodeDao.getImageOf(cId);
                return null;
            }
        };
        taskRunner.executeAsync(callable,callback);
    }

}

