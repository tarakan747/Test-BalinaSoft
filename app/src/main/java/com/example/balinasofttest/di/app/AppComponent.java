package com.example.balinasofttest.di.app;

import com.example.balinasofttest.di.list.ListComponent;
import com.example.balinasofttest.di.list.ListModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    ListComponent plus(ListModule module);
}
