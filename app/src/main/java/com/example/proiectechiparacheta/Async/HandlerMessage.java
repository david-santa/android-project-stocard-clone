package com.example.proiectechiparacheta.Async;

import com.example.proiectechiparacheta.Async.Callback;

public class HandlerMessage<R> implements Runnable {

    private final R result;
    private final Callback<R> mainThreadOperation;

    public HandlerMessage(R result, Callback<R> mainThreadOperation) {
        this.result = result;
        this.mainThreadOperation = mainThreadOperation;
    }

    @Override
    public void run() {
        mainThreadOperation.runResultOnUiThread(result);
    }
}
