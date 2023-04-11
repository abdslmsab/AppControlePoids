package com.example.appcontrolepoids.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DateViewModel extends ViewModel {
    private MutableLiveData<String> dateActuelle = new MutableLiveData<>();

    public LiveData<String> getDateActuelle() {
        return dateActuelle;
    }

    public void setDateActuelle(String date) {
        dateActuelle.setValue(date);
    }
}
