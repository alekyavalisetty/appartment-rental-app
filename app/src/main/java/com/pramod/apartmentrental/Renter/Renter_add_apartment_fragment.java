package com.pramod.apartmentrental.Renter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pramod.apartmentrental.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Renter_add_apartment_fragment extends Fragment {

    public Renter_add_apartment_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_renter_add_apartment, container, false);
    }
}
