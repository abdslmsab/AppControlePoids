package com.example.appcontrolepoids.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.appcontrolepoids.util.func.TriFunction;

public class CombinedThreeLiveData<L1, L2, L3, R> extends MediatorLiveData<R> {

    private final TriFunction<L1, L2, L3, R> combine;
    private L1 data1;
    private L2 data2;

    private L3 data3;

    public CombinedThreeLiveData(LiveData<L1> source1, LiveData<L2> source2, LiveData<L3> source3, TriFunction<L1, L2, L3, R> combine) {
        this.combine = combine;

        super.addSource(source1, it -> {
            data1 = it;
            update();
        });

        super.addSource(source2, it -> {
            data2 = it;
            update();
        });

        super.addSource(source3, it -> {
            data3 = it;
            update();
        });
    }

    private void update() {
        if (data1 != null && data2 != null && data3 != null) {
            setValue(combine.apply(data1, data2, data3));
        }
    }

    @Override
    public <S1> void addSource(@NonNull LiveData<S1> source, @NonNull Observer<? super S1> onChanged) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S1> void removeSource(@NonNull LiveData<S1> toRemote) {
        throw new UnsupportedOperationException();
    }
}