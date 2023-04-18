package com.example.appcontrolepoids.viewmodel;

import android.text.Editable;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.appcontrolepoids.util.CombinedLiveData;
import com.example.appcontrolepoids.util.action.ActionLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class PeseesArticleViewModel extends ViewModel {

    public MutableLiveData<Integer> nombrePeseesAEffectuer = new MutableLiveData<>();

    public void calculerCompteur(int rendement, int nombreVenues) {
        int calculCompteur = rendement * nombreVenues;
        if (calculCompteur < 500) {
            nombrePeseesAEffectuer.postValue(30);
        } else if (calculCompteur < 3200) {
            nombrePeseesAEffectuer.postValue(50);
        } else {
            nombrePeseesAEffectuer.postValue(80);
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

    public void verifierSaisieValides() {
        estPeseeValide.trigger(() -> Transformations.map(saisiesPeseesFlottant, pesee -> pesee != null && pesee >= 1));
    }

    public MutableLiveData<List<Float>> listePesees = new MutableLiveData<>(new ArrayList<>());
    public LiveData<Integer> nombrePeseesRestantes = new CombinedLiveData<>(nombrePeseesAEffectuer, listePesees, (aEffectuer, pesees) -> aEffectuer != null ? aEffectuer - pesees.size() : null);

    public void ajouterPesee(Float pesee) {
        ArrayList<Float> nouvelleListPesee = new ArrayList<>(listePesees.getValue());
        nouvelleListPesee.add(pesee);
        listePesees.postValue(nouvelleListPesee);
    }

    public void reinitialiserChamp() {
        peseeSaisie.setValue("");
    }
}
