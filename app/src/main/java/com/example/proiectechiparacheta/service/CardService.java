package com.example.proiectechiparacheta.service;

import android.content.Context;

import com.example.proiectechiparacheta.Async.AsyncTaskRunner;
import com.example.proiectechiparacheta.Async.Callback;
import com.example.proiectechiparacheta.FidelityCard;
import com.example.proiectechiparacheta.database.DatabaseManager;
import com.example.proiectechiparacheta.database.dao.FidelityCardDao;

import java.util.List;
import java.util.concurrent.Callable;

public class CardService {
    private final FidelityCardDao fidelityCardDao;
    private final AsyncTaskRunner taskRunner;

    public CardService(Context context){
        fidelityCardDao = DatabaseManager.getInstance(context).fidelityCardDao();
        taskRunner = new AsyncTaskRunner();
    }

    public void getAll(Callback<List<FidelityCard>> callback){
        Callable<List<FidelityCard>> callable = new Callable<List<FidelityCard>>() {
            @Override
            public List<FidelityCard> call() throws Exception {
                return fidelityCardDao.getAllCards();
            }
        };
        taskRunner.executeAsync(callable,callback);
    }

    public void insert(Callback<FidelityCard> callback, FidelityCard card){
        Callable<FidelityCard> callable = new Callable<FidelityCard>(){

            @Override
            public FidelityCard call() throws Exception {
               if(card==null){
                   return null;
               }
               long id = fidelityCardDao.insert(card);
               if(id==-1){
                   return null;
               }
               card.setId((int)id);
               return card;
            }
        };
        taskRunner.executeAsync(callable,callback);
    }

    public void update(Callback<FidelityCard> callback, FidelityCard card){
        Callable<FidelityCard> callable = new Callable<FidelityCard>() {
            @Override
            public FidelityCard call() throws Exception {
                if(card==null){
                    return null;
                }
                int count = fidelityCardDao.update(card);
                if(count!=1){
                    return null;
                }
                return card;
            }
        };
        taskRunner.executeAsync(callable,callback);
    }

    public void delete(Callback<FidelityCard>callback, FidelityCard card){
        Callable<FidelityCard> callable = new Callable<FidelityCard>() {
            @Override
            public FidelityCard call() throws Exception {
                if(card == null){
                    return null;
                }
                fidelityCardDao.delete(card);
                return card;
            }
        };
        taskRunner.executeAsync(callable,callback);
    }
}
