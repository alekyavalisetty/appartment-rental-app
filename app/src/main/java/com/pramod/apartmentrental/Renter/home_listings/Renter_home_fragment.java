package com.pramod.apartmentrental.Renter.home_listings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pramod.apartmentrental.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Renter_home_fragment extends Fragment {

    public Renter_home_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_renter_home_fragment, container, false);
    }
}
