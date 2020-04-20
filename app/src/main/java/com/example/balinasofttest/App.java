package com.example.balinasofttest;
import android.app.Application;
import com.example.balinasofttest.di.app.AppComponent;
import com.example.balinasofttest.di.app.AppModule;
import com.example.balinasofttest.di.app.DaggerAppComponent;
import com.example.balinasofttest.di.list.ListComponent;
import com.example.balinasofttest.di.list.ListModule;


public class App extends Application {
    private static App app;
    private AppComponent appComponent;
    private ListComponent listComponent;

    public App() {
        app = this;
    }

    public static App get() {
        return app;
    }

    @Override
    public void onCreate() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        super.onCreate();

    }

    public ListComponent plus(ListModule module) {
        if (listComponent == null) {
            listComponent = appComponent.plus(module);
        }
        return listComponent;
    }

    public void clearListComponent() {
        listComponent = null;
    }
}
