package com.example.appcontrolepoids.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.appcontrolepoids.util.CombinedThreeLiveData;
import com.example.appcontrolepoids.util.CombinedTwoLiveData;

public class ResultatArticleViewModel extends ViewModel {

    private final MutableLiveData<float[]> pesees = new MutableLiveData<>();
    public LiveData<Float> moyenne = Transformations.map(pesees, _pesees -> {
        float somme = 0;

        for (float p : _pesees) {
            somme += p;
        }

        return somme / _pesees.length;
    });

    public LiveData<Float> variance = new CombinedTwoLiveData<>(pesees, moyenne, (_pesees, _moyenne) -> {
        float sommeEcartsCarres = 0;

        for (float p : _pesees) {
            float ecart = p - _moyenne;
            sommeEcartsCarres += ecart * ecart;
        }

        return sommeEcartsCarres / _pesees.length;
    });


    public LiveData<Float> ecartType = Transformations.map(variance, _variance -> (float) Math.sqrt(_variance));

    public MutableLiveData<Integer> poidsBrut = new MutableLiveData<>();

    public MutableLiveData<Float> coefficient = new MutableLiveData<>();

    public MutableLiveData<Float> formule = new CombinedThreeLiveData<>(poidsBrut, coefficient, ecartType,
            (_poidsBrut, _coefficient, _ecartType) -> _poidsBrut + (_coefficient * _ecartType)
    );

    //TODO : vérifier la formule
    public LiveData<Boolean> lotValide = new CombinedTwoLiveData<>(moyenne, formule, (_moyenne, _formule) -> _moyenne >= _formule);

    public void init(float[] pesees, int poidsBrut, float coefficient) {
        this.pesees.postValue(pesees);
        this.poidsBrut.postValue(poidsBrut);
        this.coefficient.postValue(coefficient);
    }
}
