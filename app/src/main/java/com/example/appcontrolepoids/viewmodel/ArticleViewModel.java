package com.example.appcontrolepoids.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.appcontrolepoids.database.AppDatabase;

//Encapsulasion de la logique de récupération des données de la base de données fournis à la couche d'interface utilisateur de manière asynchrone
public class ArticleViewModel extends ViewModel {
private final AppDatabase database;

    ArticleViewModel(Context context) {
        this.database = AppDatabase.getInstance(context);
    }

    //Utilisation de la méthode getArticleByEAN() de l'objet database pour exécuter la requête SQL qui vérifie si un article correspondant à ce code EAN existe dans la base de données
    public LiveData<Boolean> articleExiste(String ean) {
        return Transformations.map(database.articleDao().getArticleByEAN(ean), article -> article != null);
    }
}