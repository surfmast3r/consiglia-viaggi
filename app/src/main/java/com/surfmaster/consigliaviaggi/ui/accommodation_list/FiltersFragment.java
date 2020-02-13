package com.surfmaster.consigliaviaggi.ui.accommodation_list;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.surfmaster.consigliaviaggi.AccommodationRecyclerViewAdapter;
import com.surfmaster.consigliaviaggi.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class FiltersFragment extends DialogFragment{

    private AccommodationListViewModel accommodationListViewModel;


    public static FiltersFragment newInstance() {
        FiltersFragment f = new FiltersFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_NoActionBar);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accommodationListViewModel =
                ViewModelProviders.of(this).get(AccommodationListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_filters, container, false);

        View cancelButton= root.findViewById(R.id.fullscreen_dialog_close);
        setButtonListener(cancelButton);

        View applyButton= root.findViewById(R.id.fullscreen_dialog_action);
        setButtonListener(applyButton);

        final TextView textView = root.findViewById(R.id.filter_fragment_text);
        accommodationListViewModel.getFilterText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        return root;
    }

    private void setButtonListener(View view){
        int viewId=view.getId();
        switch (viewId){
            case R.id.fullscreen_dialog_action:
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
                break;
            case R.id.fullscreen_dialog_close:
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
        }



    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Animation_Design_BottomSheetDialog;
        return dialog;
    }


}
