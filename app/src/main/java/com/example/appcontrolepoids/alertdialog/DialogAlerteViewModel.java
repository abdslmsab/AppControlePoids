package com.example.appcontrolepoids.alertdialog;

import android.text.Editable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.appcontrolepoids.util.action.ActionLiveData;

public class DialogAlerteViewModel extends ViewModel {
    private final MutableLiveData<OptionsAlerte> options = new MutableLiveData<>();
    private final MutableLiveData<Boolean> fermer = new MutableLiveData<>();

    public LiveData<OptionsAlerte> getOptions() {
        return options;
    }

    public void setOptions(OptionsAlerte newOptions) {
        options.setValue(newOptions);
    }

    public void afficherDialog() {
        fermer.setValue(false);
    }

    public void fermerAlerte() {
        fermer.setValue(true);
    }

    public LiveData<Boolean> estFerme() {
        return fermer;
    }

    //Code saisi par l'utilisateur (MutableLiveData : valeur qui peut changer)
    public final MutableLiveData<String> codeSaisi = new MutableLiveData<>("");

    //Permet de réagir à des évènements
    public final ActionLiveData<Boolean> codeValide = new ActionLiveData<>();

    /**
     * Méthode appelée lorsque le code saisi dans l'EditText change
     *
     * @param texte le texte saisi
     */
    public void changementCode(Editable texte) {
        codeSaisi.postValue(texte.toString());
    }

    //On vérifie si le code entré est bien égal à 1234
    public void verifierCodeValide() {
        codeValide.trigger(() -> Transformations.map(codeSaisi, code -> code.equals("1234")));
    }

    public void reinitialiserCodeSaisi(){
        codeSaisi.setValue("");
    }
}
