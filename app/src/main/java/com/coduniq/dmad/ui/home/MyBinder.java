package com.coduniq.dmad.ui.home;

import android.os.Binder;

public class MyBinder extends Binder {
    public MyBinder getService(){
        return MyBinder.this;
    }
}
