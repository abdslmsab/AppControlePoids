package com.example.appcontrolepoids.viewmodel;

import android.text.Editable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.appcontrolepoids.database.AppDatabase;
import com.example.appcontrolepoids.model.Article;
import com.example.appcontrolepoids.util.action.ActionLiveData;

public class AjouterArticleViewModel extends ViewModel {
    //Texte codeOperateurSaisi saisi par l'utilisateur (MutableLiveData : valeur qui peut changer)
    public final MutableLiveData<String> codeOperateurSaisi = new MutableLiveData<>("");

    public void changementCodeOperateur(Editable texte) {
        codeOperateurSaisi.postValue(texte.toString());
    }

    //Permet de réagir à des évènements
    public final ActionLiveData<Boolean> estCodeOperateurValide = new ActionLiveData<>();

    public final MutableLiveData<String> eanSaisi = new MutableLiveData<>("");

    public void changementEan(Editable texte) {
        eanSaisi.postValue(texte.toString());
    }

    public final ActionLiveData<Boolean> estEanValide = new ActionLiveData<>();

    public final MutableLiveData<String> nomSaisi = new MutableLiveData<>("");

    public void changementNom(Editable texte) {
        nomSaisi.postValue(texte.toString());
    }

    public final ActionLiveData<Boolean> estNomValide = new ActionLiveData<>();

    public final MutableLiveData<String> codeArticleSaisi = new MutableLiveData<>("");

    public void changementCodeArticle(Editable texte) {
        codeArticleSaisi.postValue(texte.toString());
    }

    public final ActionLiveData<Boolean> estCodeArticleSaisiValide = new ActionLiveData<>();

    public final MutableLiveData<String> poidsNetSaisi = new MutableLiveData<>("");

    public void changementPoidsNet(Editable texte) {
        poidsNetSaisi.postValue(texte.toString());
    }

    public final ActionLiveData<Boolean> estPoidsNetSaisiValide = new ActionLiveData<>();

    public final MutableLiveData<String> poidsBrutSaisi = new MutableLiveData<>("");

    public void changementPoidsBrut(Editable texte) {
        poidsBrutSaisi.postValue(texte.toString());
    }

    public final ActionLiveData<Boolean> estPoidsBrutSaisiValide = new ActionLiveData<>();

    public final MutableLiveData<String> rendementSaisi = new MutableLiveData<>("");

    public void changementRendement(Editable texte) {
        rendementSaisi.postValue(texte.toString());
    }

    public final ActionLiveData<Boolean> estRendementSaisiValide = new ActionLiveData<>();

    //Vérification de tous les champs éditables
    public void verifierSaisiesValide() {
        estCodeOperateurValide.trigger(() -> Transformations.map(codeOperateurSaisi, code -> code != null && code.length() == 4));
        estEanValide.trigger(() -> Transformations.map(eanSaisi, ean -> ean != null && ean.length() == 13));
        estNomValide.trigger(() -> Transformations.map(nomSaisi, nom -> nom != null && nom.length() > 0 && nom.length() <= 69));
        estCodeArticleSaisiValide.trigger(() -> Transformations.map(codeArticleSaisi, codeArticle -> codeArticle != null && codeArticle.length() > 0 && codeArticle.length() <= 13));
        estPoidsNetSaisiValide.trigger(() -> Transformations.map(poidsNetSaisi, poidsNet -> poidsNet != null && poidsNet.length() > 0 && poidsNet.length() <= 3));
        estPoidsBrutSaisiValide.trigger(() -> Transformations.map(poidsBrutSaisi, poidsBrut -> poidsBrut != null && poidsBrut.length() > 0 && poidsBrut.length() <= 3));
        estRendementSaisiValide.trigger(() -> Transformations.map(rendementSaisi, rendement -> rendement != null && rendement.length() > 0 && rendement.length() <= 3));
    }

    public void insererArticle(Article article) {
        AppDatabase.getInstance().articleDao().insert(article);
    }
}
