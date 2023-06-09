package com.example.myapplication;

import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragments.Classes.ClassSelectorFragment;
import com.example.myapplication.Fragments.Profile.ProfileFragment;
import com.example.myapplication.Fragments.Profile.User;
import com.example.myapplication.Fragments.SocialFragment;

public class TabManager {
    private FragmentManager fragmentManager;
    private  Fragment currentFragment;
    private String transactionTAG;
    private User user;
    public TabManager(@NonNull FragmentActivity fragmentActivity) {
        fragmentManager = fragmentActivity.getSupportFragmentManager();
        user = new User((Settings.Secure.getString(fragmentActivity.getContentResolver(), Settings.Secure.ANDROID_ID)));
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
                currentFragment = new ClassSelectorFragment();
                transactionTAG = "Classes";
                break;
            case 2:
                currentFragment = new ProfileFragment(user);
                transactionTAG = "Profile";
                break;
        }
        replaceFragment(currentFragment, transactionTAG);
    }
}
