package com.askia.coremodel.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.askia.coremodel.datamodel.database.repository.DBRepository;

public class WelcomViewModel extends ViewModel {


    public WelcomViewModel() {
        DBRepository.SetShowWelcomPage(false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
