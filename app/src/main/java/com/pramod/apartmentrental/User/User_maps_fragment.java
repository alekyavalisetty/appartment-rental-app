package com.pramod.apartmentrental.User;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pramod.apartmentrental.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class User_maps_fragment extends Fragment {

    public User_maps_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_maps_fragment, container, false);
    }
}
