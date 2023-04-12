package com.example.appcontrolepoids.viewmodel;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateViewModel extends ViewModel {
    private MutableLiveData<String> dateActuelle = new MutableLiveData<>();

    public LiveData<String> getDateActuelle() {
        return dateActuelle;
    }

    public void setDateActuelle(String date) {
        dateActuelle.setValue(date);
    }

    // Méthode de vérification de la date de durabilité minimale
    public boolean dateValide(String dateString) {
        if (TextUtils.isEmpty(dateString)) {
            return false;
        }

        //On divise la date en 3 parties dès qu'il y'a un "/"
        String[] partiesDate = dateString.split("/");
        if (partiesDate.length != 3) {
            return false;
        }

        try {
            int jour = Integer.parseInt(partiesDate[0]);
            int mois = Integer.parseInt(partiesDate[1]);
            int annee = Integer.parseInt(partiesDate[2]);

            // Vérification des limites pour jour, mois et année
            if (jour < 1 || jour > 31 || mois < 1 || mois > 12 || annee < 0 || annee > 99) {
                return false;
            }

            // Vérification si la date est valide
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
            dateFormatter.setLenient(false);
            dateFormatter.parse(dateString);
        } catch (NumberFormatException | ParseException e) {
            return false;
        }

        return true;
    }
}
