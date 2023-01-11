package com.myapplication.healthylife.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommunicateViewModel extends ViewModel {
    private MutableLiveData<Integer> _pos = new MutableLiveData();
    public LiveData<Integer> pos = _pos;

    private MutableLiveData<Boolean> _isUpdated = new MutableLiveData();
    public LiveData<Boolean> isUpdated = _isUpdated;

    private MutableLiveData<Boolean> _isLogout = new MutableLiveData();
    public LiveData<Boolean> isLogout = _isLogout;

    public void selectTab(int pos) {
        _pos.postValue(pos);
    }

    public void updatedBMI(boolean isUpdated) {
        Log.d("CHOTAOTEST", "updatedBMI: "+isUpdated);
        _isUpdated.postValue(isUpdated);
    }

    public void logout(boolean isLogout) {
        _isLogout.postValue(isLogout);
    }
}
