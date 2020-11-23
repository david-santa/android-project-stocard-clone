package com.example.proiectechiparacheta;

import android.os.Handler;

import java.util.concurrent.Callable;

public class RunnableTask<R> implements Runnable{
    private final Handler handler;
    private final Callable<R> asyncOperation;
    private final Callback<R> mainThreadOperation;

    public RunnableTask(Handler handler, Callable<R> asyncOperation, Callback<R> mainThreadOperation) {
        this.handler = handler;
        this.asyncOperation = asyncOperation;
        this.mainThreadOperation = mainThreadOperation;
    }

    @Override
    public void run() {
        try{
            R result = asyncOperation.call();
            handler.post(new HandlerMessage<R>(result,mainThreadOperation));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
