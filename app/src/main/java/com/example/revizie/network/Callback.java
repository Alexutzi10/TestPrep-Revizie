package com.example.revizie.network;

public interface Callback <R>{
    void runResultOnUIThread(R result);
}

