package com.example.appcontrolepoids.viewmodel;

import android.text.Editable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.appcontrolepoids.database.AppDatabase;
import com.example.appcontrolepoids.model.Article;
import com.example.appcontrolepoids.util.action.ActionLiveData;

import java.util.Objects;

public class ArticleViewModel extends ViewModel {

    //Texte ean saisi par l'utilisateur (MutableLiveData : valeur qui peut changer)
    public final MutableLiveData<String> eanSaisi = new MutableLiveData<>("");
    //Permet d'activer le bouton "Valider" seulement lorsqu'au moins 1 chiffre est entré (LiveData : valeur seulement observable)
    public final LiveData<Boolean> activerBoutonValider = Transformations.map(eanSaisi, texte -> texte.length() == 13);
    //Permet de réagir à des évènements

    public final ActionLiveData<Article> article = new ActionLiveData<>();
    public final LiveData<Boolean> articleExiste = Transformations.map(article, Objects::nonNull);

    /**
     * Méthode appelée lorsque l'ean saisi dans l'EditText change
     *
     * @param texte le texte saisi
     */
    public void changementEan(Editable texte) {
        eanSaisi.postValue(texte.toString());
    }

    public void recupererArticle() {
        article.trigger(() -> AppDatabase.getInstance().articleDao().getArticleByEAN(eanSaisi.getValue()));
    }

    public void reinitialiserChamp() {
        eanSaisi.setValue("");
    }
}