package com.koreatech.naeilro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends Fragment {
    @BindView(R.id.category_tourspot)
    LinearLayout categoryTourspotLayout;
    @BindView(R.id.category_facility)
    LinearLayout categoryFacilityLayout;
    @BindView(R.id.category_festival)
    LinearLayout categoryFestivalLayout;
    @BindView(R.id.category_house)
    LinearLayout categoryHouseLayout;
    @BindView(R.id.category_reports)
    LinearLayout categoryReportsLayout;
    @BindView(R.id.category_restraunt)
    LinearLayout categoryRestrauntLayout;
    @BindView(R.id.category_train)
    LinearLayout categoryTrainLayout;
    @BindView(R.id.category_weather)
    LinearLayout categoryWeatherLayout;

    View root;
    NavController navController;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        return root;
    }
    @OnClick(R.id.category_tourspot)
    void goToTourSpotFragment(){
        navController.navigate(R.id.action_navigation_home_to_navigation_tourspot);
    }
    @OnClick(R.id.category_train)
    void goToTrainInfoFragment(){
        navController.navigate(R.id.action_navigation_home_to_navigation_train);
    }
    @OnClick(R.id.category_restraunt)
    void goToRestrauntFragment(){
        navController.navigate(R.id.action_navigation_home_to_navigation_restraunt);
    }

    @OnClick(R.id.category_house)
    void goToHouseFragment(){
        navController.navigate(R.id.action_navigation_home_to_navigation_house);
    }
    @OnClick(R.id.category_reports)
    void goToReportsFragment(){
        navController.navigate(R.id.action_navigation_home_to_navigation_reports);
    }
    @OnClick(R.id.category_facility)
    void goToFacilityFragment(){
        navController.navigate(R.id.action_navigation_home_to_navigation_facility);
    }
    @OnClick(R.id.category_festival)
    void goToFestivalFragment(){
        navController.navigate(R.id.action_navigation_home_to_navigation_festival);
    }
    @OnClick(R.id.category_weather)
    void goToWeatherFragment(){
        navController.navigate(R.id.action_navigation_home_to_navigation_weather);
    }
}
