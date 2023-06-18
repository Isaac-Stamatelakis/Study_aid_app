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
    ChatGroup chatGroup;
    public ChatClassSelectorFragment(ChatGroup chatGroup) {
        this.chatGroup = chatGroup;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState);
        addClassButton.setVisibility(View.INVISIBLE);
        calendarView.setVisibility(View.INVISIBLE);

        selectClass();

        return view;


    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        classList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SchoolClass schoolClass = classes.get(position);
                DocumentReference documentReference = db.collection("Classes").document(schoolClass.getDbID());
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            Log.d(TAG, documentSnapshot.getId());
                            ChatStudyMaterialSelectorFragment chatStudyMaterialSelectorFragment = new ChatStudyMaterialSelectorFragment(documentSnapshot.getId(), chatGroup);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, chatStudyMaterialSelectorFragment)
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            Log.e(TAG, "Failed to go to class");
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void selectClass() {

    }

}
