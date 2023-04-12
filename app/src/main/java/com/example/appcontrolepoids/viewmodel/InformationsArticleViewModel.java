package com.example.appcontrolepoids.viewmodel;

import android.text.Editable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.appcontrolepoids.util.action.ActionLiveData;

public class InformationsArticleViewModel extends ViewModel {

    //Texte codeOperateurSaisi saisi par l'utilisateur (MutableLiveData : valeur qui peut changer)
    public final MutableLiveData<String> codeOperateurSaisi = new MutableLiveData<>("");

    public void changementCodeOperateur(Editable texte) {
        codeOperateurSaisi.postValue(texte.toString());
    }

    //Permet de réagir à des évènements
    public final ActionLiveData<Boolean> estCodeOperateurValide = new ActionLiveData<>();

    //Texte codeOperateurSaisi saisi par l'utilisateur (MutableLiveData : valeur qui peut changer)
    public final MutableLiveData<String> nombreVenuesSaisi = new MutableLiveData<>("");

    public void changementNombreVenues(Editable texte) {
        nombreVenuesSaisi.postValue(texte.toString());
    }

    public final LiveData<Integer> saisieNombreVenuesEntier = Transformations.map(nombreVenuesSaisi, texte -> {
        try {
            return Integer.parseInt(texte);
        } catch (NumberFormatException e) {
            return null;
        }
    });

    //Permet de réagir à des évènements
    public final ActionLiveData<Boolean> estNombreVenuesValide = new ActionLiveData<>();

    public final MutableLiveData<String> numeroLotSaisi = new MutableLiveData<>("");

    public void changementNumeroLot(Editable texte) {
        numeroLotSaisi.postValue(texte.toString());
    }

    //Permet de réagir à des évènements
    public final ActionLiveData<Boolean> estNumeroLotValide = new ActionLiveData<>();

    public final MutableLiveData<String> ddmSaisie = new MutableLiveData<>("");

    public void changementDdm(Editable texte) {
        ddmSaisie.postValue(texte.toString());
    }

    //Permet de réagir à des évènements
    public final ActionLiveData<Boolean> estDdmValide = new ActionLiveData<>();

    //Vérification de tous les champs éditables
    public void verifierSaisiesValide() {
        estCodeOperateurValide.trigger(() -> Transformations.map(codeOperateurSaisi, code -> code != null && code.length() == 4));
        estNombreVenuesValide.trigger(() -> Transformations.map(saisieNombreVenuesEntier, venues -> venues != null && venues >= 1));
        estNumeroLotValide.trigger(() -> Transformations.map(numeroLotSaisi, numero -> numero != null && numero.length() == 5));
        estDdmValide.trigger(() -> Transformations.map(ddmSaisie, ddm -> {
            try {
                String[] tokens = ddm.split("/");
                int jour = Integer.parseInt(tokens[0]);
                int mois = Integer.parseInt(tokens[1]);
                int annee = Integer.parseInt(tokens[2]);

                if (jour < 1 || jour > 31) {
                    return false;
                }
                if (mois < 1 || mois > 12) {
                    return false;
                }
                if (annee < 0 || annee > 99) {
                    return false;
                }
                return true;
            } catch(NumberFormatException e) {
                return false;
            }
        }));
    }
}
