package com.example.myapplication.Fragments.Social.Message;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.CustomDocumentRetrieval;
import com.example.myapplication.CustomQuery;
import com.example.myapplication.Fragments.Classes.ClassSelectorFragment.StudyMaterialAddClassSelectorFragment;
import com.example.myapplication.Fragments.Classes.StudyMaterial.FlashCard;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Note;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Quiz;
import com.example.myapplication.Fragments.Classes.StudyMaterial.StudyMaterial;
import com.example.myapplication.R;
import com.example.myapplication.StaticHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;

public abstract class MessageViewSetter {
    protected View view;
    public MessageViewSetter(View view) {
        this.view = view;
    }

    public abstract void set();

}
