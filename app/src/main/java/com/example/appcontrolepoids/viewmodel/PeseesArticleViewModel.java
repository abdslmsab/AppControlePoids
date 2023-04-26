package com.example.appcontrolepoids.viewmodel;

import android.text.Editable;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.appcontrolepoids.util.CombinedTwoLiveData;
import com.example.appcontrolepoids.util.action.ActionLiveData;

import java.util.ArrayList;
import java.util.List;

public class PeseesArticleViewModel extends ViewModel {

    public MutableLiveData<Integer> nombrePeseesAEffectuer = new MutableLiveData<>();

    public float coefficient;

    private int poidsBrut;

    public void init(int poidsBrut) {
       this.poidsBrut = poidsBrut;
    }

    public void calculerCompteur(int rendement, int nombreVenues) {
        int calculCompteur = rendement * nombreVenues;
        if (calculCompteur < 500) {
            nombrePeseesAEffectuer.postValue(30);
            coefficient = 0.239F;
        } else if (calculCompteur < 3200) {
            nombrePeseesAEffectuer.postValue(50);
            coefficient = 0.184F;
        } else {
            nombrePeseesAEffectuer.postValue(80);
            coefficient = 0.144F;
        }
    }

    public MutableLiveData<String> peseeSaisie = new MutableLiveData<>("");

    public void changementPesees(Editable texte) {
        peseeSaisie.postValue(texte.toString());
    }

    public final LiveData<Float> saisiesPeseesFlottant = Transformations.map(peseeSaisie, texte -> {
        try {
            return Float.parseFloat(texte);
        } catch (NumberFormatException e) {
            return null;
        }
    });

    public final ActionLiveData<Boolean> estPeseeValide = new ActionLiveData<>();

    public final ActionLiveData<Boolean> estPeseeAberrante = new ActionLiveData<>();

    public void verifierSaisieValides() {
        estPeseeValide.trigger(() -> Transformations.map(saisiesPeseesFlottant, pesee -> pesee != null && pesee >= 1));
    }

    public void verifierSaisieDonneeAberrante(){
        estPeseeAberrante.trigger(() -> Transformations.map(saisiesPeseesFlottant, pesee -> {
            if (pesee == null){
                return false;
            }
            double poidsMin = poidsBrut - (poidsBrut * 0.1);
            double poidsMax = poidsBrut + (poidsBrut * 0.1);
            return pesee < poidsMin || pesee > poidsMax;
        }));
    }

    //Permet de verifier, d'ajouter et de valider la pesée lors du clique sur le bouton "Valider" ou la touche "Ok" du clavier
    public void actionValiderSaisie() {
        verifierSaisieValides();
        verifierSaisieDonneeAberrante();
        if (Boolean.TRUE.equals(estPeseeValide.getValue()) && Boolean.FALSE.equals(estPeseeAberrante.getValue())) {
            ajouterPesee(saisiesPeseesFlottant.getValue());
            reinitialiserChamp();
        }
    }

    //Permet de quand même ajouter la saisie aberrante lorsque l'utilisateur appuie sur "Oui"
    public void actionAjouterPeseeAberrante(){
        verifierSaisieValides();
        if (Boolean.TRUE.equals(estPeseeValide.getValue())) {
            ajouterPesee(saisiesPeseesFlottant.getValue());
            reinitialiserChamp();
        }
    }

    public MutableLiveData<List<Float>> listePesees = new MutableLiveData<>(new ArrayList<>());

    public void reinitialiserListePesees() {
        listePesees.postValue(new ArrayList<>());
    }
    public LiveData<Integer> nombrePeseesRestantes = new CombinedTwoLiveData<>(nombrePeseesAEffectuer, listePesees, (aEffectuer, pesees) -> aEffectuer != null ? aEffectuer - pesees.size() : null);

    public void ajouterPesee(Float pesee) {
        ArrayList<Float> nouvelleListPesee = new ArrayList<>(listePesees.getValue());
        nouvelleListPesee.add(pesee);
        listePesees.postValue(nouvelleListPesee);
    }

    public void reinitialiserChamp() {
        peseeSaisie.setValue("");
    }
}
