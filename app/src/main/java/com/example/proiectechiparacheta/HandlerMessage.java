package com.example.proiectechiparacheta;

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
