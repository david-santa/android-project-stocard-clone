package com.example.proiectechiparacheta.Async;

public interface Callback<R> {

    void runResultOnUiThread(R result);
}
