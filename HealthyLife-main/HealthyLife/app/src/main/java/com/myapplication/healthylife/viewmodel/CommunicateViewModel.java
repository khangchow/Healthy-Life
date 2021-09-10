package com.myapplication.healthylife.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommunicateViewModel extends ViewModel {
    private MutableLiveData<Integer> _pos = new MutableLiveData();
    public LiveData<Integer> pos = _pos;

    public void selectTab(int pos) {
        _pos.postValue(pos);
    }
}
