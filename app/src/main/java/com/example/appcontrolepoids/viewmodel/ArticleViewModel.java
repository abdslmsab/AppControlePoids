package com.example.appcontrolepoids.viewmodel;

import android.text.Editable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.appcontrolepoids.database.AppDatabase;
import com.example.appcontrolepoids.util.action.ActionLiveData;

import java.util.Objects;

public class ArticleViewModel extends ViewModel {

    public final MutableLiveData<String> ean = new MutableLiveData<>("");

    public final ActionLiveData<Boolean> articleExiste = new ActionLiveData<>();

    public void onEanChange(Editable text) {
        ean.postValue(text.toString());
    }

    public void checkArticleExist() {
        articleExiste.trigger(() -> Transformations.map(AppDatabase.getInstance().articleDao().getArticleByEAN(ean.getValue()), Objects::nonNull));
    }
}