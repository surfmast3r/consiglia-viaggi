package com.surfmaster.consigliaviaggi.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.surfmaster.consigliaviaggi.R;
import com.surfmaster.consigliaviaggi.controllers.ViewAccommodationsController;
import com.surfmaster.consigliaviaggi.ui.accommodation_list.FiltersFragment;

public class MainFragment extends Fragment {

    private MainViewModel mainViewModel;
    private ViewAccommodationsController viewAccommodationsController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        viewAccommodationsController=new ViewAccommodationsController();

        mainViewModel =
                ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_main, container, false);


        final Button citySelectButton = root.findViewById(R.id.text_home);

        mainViewModel.setCity(viewAccommodationsController.getSelectedCity(requireContext()));
        mainViewModel.getCity().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                citySelectButton.setText(s);
            }
        });

        citySelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DialogFragment newFragment = SelectCityFragment.newInstance();
                newFragment.show(ft, "dialog");
            }
        });

        return root;
    }
}