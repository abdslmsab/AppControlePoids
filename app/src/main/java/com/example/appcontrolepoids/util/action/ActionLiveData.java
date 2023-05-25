package com.example.appcontrolepoids.util.action;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.appcontrolepoids.util.Async;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ActionLiveData<T> extends MediatorLiveData<T> {

    private LiveData<T> source;

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        if (hasObservers()) {
            throw new IllegalStateException("Seulement 1 observateur peut répondre à l'évènement");
        }

        super.observe(owner, data -> {
            //On ignore les données null
            if (data == null) return;

            observer.onChanged(data);

            //Directement après avoir notifié l'évènement, on reset les données
            //setValue(null);
            removeSource(source);
        });
    }

    /**
     * Exécute LiveData donnée en paramètre et relaie son résultat sur elle-même
     *
     * @param action fonction qui retourne une LiveData
     */
    public void trigger(@NonNull Action<T> action) {
        FutureTask<LiveData<T>> future = new FutureTask<>(action::action);
        Async.EXECUTOR_SERVICE.execute(future);

        try {
            source = future.get();
            addSource(source, this::setValue);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
