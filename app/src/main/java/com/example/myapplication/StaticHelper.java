package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class StaticHelper {
    public static void switchFragment(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
