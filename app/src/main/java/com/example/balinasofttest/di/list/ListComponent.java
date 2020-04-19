package com.example.balinasofttest.di.list;

import com.example.balinasofttest.ui.presenter.ActivityPresenter;

import dagger.Subcomponent;

@Subcomponent(modules = {ListModule.class})
@ListScope
public interface ListComponent {
    void inject(ActivityPresenter presenter);
}
