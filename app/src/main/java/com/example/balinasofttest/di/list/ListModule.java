package com.example.balinasofttest.di.list;



import com.example.balinasofttest.data.list.Repository;
import com.example.balinasofttest.data.list.RepositoryImpl;
import com.example.balinasofttest.data.provider.web.Api;

import dagger.Module;
import dagger.Provides;

@Module
public class ListModule {

    @Provides
    @ListScope
    Repository provideRepo(Api api) {
        return new RepositoryImpl(api);
    }

}
