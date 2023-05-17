package com.example.myapplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.AggregateField;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Boolean testing;
    private TabLayout tabLayout;
    private com.example.myapplication.TabManager tabManager;
    FirebaseFirestore db;
    private String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        // Views
        tabLayout = findViewById(R.id.tab_manager);

        // Tab Manager
        tabLayout.selectTab(tabLayout.getTabAt(0));
        tabManager = new com.example.myapplication.TabManager(this);

        this.handleAndroidID();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (!isFinishing()) {
                    int position = tab.getPosition();
                    tabManager.switchFragment(position);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void handleAndroidID() {
        try {
            getApplication().getClassLoader().loadClass("com.robotium.solo.Solo");
            testing = true;
        } catch(Exception e) {
            testing = false;
        }
        final String user_id  = (!testing) ? (Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID)) : "android_id";
        Log.d("Secure_id", user_id);
        CollectionReference collectionReference = db.collection("Users");
        Query query = collectionReference.whereEqualTo("user_id",user_id);
        AggregateQuery aggregateQuery = query.count();
        aggregateQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    AggregateQuerySnapshot snapshot = task.getResult();

                    Log.d("User_id Count", Long.toString(snapshot.getCount()));
                    if (snapshot.getCount() < 1) {
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("Major", Collections.emptyList());
                        userInfo.put("Minor", Collections.emptyList());
                        userInfo.put("Education_level", null);
                        userInfo.put("user_id", user_id);
                        userInfo.put("School", null);
                        userInfo.put("Year", null);
                        CollectionReference classCollection = db.collection("Users");
                        String id = classCollection.document().getId();
                        classCollection.document(id).set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("MainActivity", "User successfully added");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                        builder
                                                .setTitle("Welcome to Study Better")
                                                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("MainActivity", "User not added ".concat(e.toString()));
                                    }
                                });

                    }

                } else {
                    Log.d("MainActivity","Task not successful");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        int count = this.getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            Log.d("MainActivity", "EXITING");
            super.onBackPressed();
        } else {
            Log.d("MainActivity", "GOING BACK");
            getSupportFragmentManager().popBackStack();
        }
    }

}