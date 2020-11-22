package com.example.proiectechiparacheta;

public interface Callback<R> {

    void runResultOnUiThread(R result);
}
