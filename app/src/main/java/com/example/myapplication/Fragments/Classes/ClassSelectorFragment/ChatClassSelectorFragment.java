package com.example.myapplication.Fragments.Classes.ClassSelectorFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.Classes.AddClassFragment;
import com.example.myapplication.Fragments.Classes.SchoolClass.SchoolClass;
import com.example.myapplication.Fragments.Classes.SchoolClass.SchoolClassArrayAdapter;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Selector.ChatStudyMaterialSelectorFragment;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Selector.ClassStudyMaterialSelectorFragment;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroup;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ChatClassSelectorFragment extends AbstractClassSelectorFragment {
    public ChatClassSelectorFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState);
        selectClass();

        return view;


    }

    @Override
    protected void classSelected(SchoolClass schoolClass) {
        super.classSelected(schoolClass);
        DocumentReference documentReference = db.collection("Classes").document(schoolClass.getDbID());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    documentLoaded(documentSnapshot);
                } else {
                    Log.e(TAG, "Failed to go to class");
                }
            }
        });
    }
    protected void documentLoaded(DocumentSnapshot documentSnapshot) {

    }
    @Override
    protected void setViews(View view) {
        classList = view.findViewById(R.id.class_fragment_class_list);
    }

    @Override
    protected void setViewListeners(View view) {
        super.setViewListeners(view);
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.chat_class_selector_fragment, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void selectClass() {

    }

}
