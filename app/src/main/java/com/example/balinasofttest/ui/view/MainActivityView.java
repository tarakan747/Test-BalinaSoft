package com.example.balinasofttest.ui.view;


import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.balinasofttest.data.dto.PhotoTypeDtoOut;
import com.example.balinasofttest.ui.view.adapter.MyAdapter;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface MainActivityView extends MvpView , MyAdapter.OnRowClickListener {

    void showProgress();

    void hideProgress();

    @StateStrategyType(SingleStateStrategy.class)
    void showList(List<PhotoTypeDtoOut> list);

    @StateStrategyType(SingleStateStrategy.class)
    void updateAdapter(List<PhotoTypeDtoOut> list);

    @StateStrategyType(SingleStateStrategy.class)
    void showCamera(int position, PhotoTypeDtoOut p);
}
