package com.example.appcontrolepoids.viewmodel;

import android.text.Editable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.appcontrolepoids.database.AppDatabase;

//Encapsulasion de la logique de récupération des données de la base de données fournis à la couche d'interface utilisateur de manière asynchrone
public class ArticleViewModel extends ViewModel {

    public final MutableLiveData<String> ean = new MutableLiveData<>("");

    public void onEanChange(Editable text) {
        ean.postValue(text.toString());
    }

    //Utilisation de la méthode getArticleByEAN() de l'objet database pour exécuter la requête SQL qui vérifie si un article correspondant à ce code EAN existe dans la base de données
    public LiveData<Boolean> articleExiste() {
        return Transformations.map(AppDatabase.getInstance().articleDao().getArticleByEAN(ean.getValue()), article -> article != null);
    }
}