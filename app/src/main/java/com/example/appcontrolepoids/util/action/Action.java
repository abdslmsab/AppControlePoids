package com.example.appcontrolepoids.util.action;

import androidx.lifecycle.LiveData;

public interface Action<T> {
    LiveData<T> action();
}
