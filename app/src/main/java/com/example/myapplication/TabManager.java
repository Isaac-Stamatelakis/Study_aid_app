package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragments.ClassFragment.ClassFragment;
import com.example.myapplication.Fragments.ProfileFragment;
import com.example.myapplication.Fragments.SocialFragment;

public class TabManager {
    private FragmentManager fragmentManager;
    private  Fragment currentFragment;
    private String transactionTAG;
    public TabManager(@NonNull FragmentActivity fragmentActivity) {
        fragmentManager = fragmentActivity.getSupportFragmentManager();
    }

    public void replaceFragment(Fragment fragment, String transactionTAG){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
    public void switchFragment(int position) {
        switch (position) {
            case 0:
                currentFragment = new SocialFragment();
                transactionTAG = "Social";
                break;
            case 1:
                currentFragment = new ClassFragment();
                transactionTAG = "Classes";
                break;
            case 2:
                currentFragment = new ProfileFragment();
                transactionTAG = "Profile";
                break;
        }
        replaceFragment(currentFragment, transactionTAG);
    }
}
