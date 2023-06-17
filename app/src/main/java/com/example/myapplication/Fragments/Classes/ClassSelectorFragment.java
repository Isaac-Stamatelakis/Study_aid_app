package com.example.myapplication.Fragments.Classes;

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

import com.example.myapplication.Fragments.Classes.SchoolClass.SchoolClass;
import com.example.myapplication.Fragments.Classes.SchoolClass.SchoolClassArrayAdapter;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ClassSelectorFragment extends Fragment {
    ListView classList;
    String user_id;
    ArrayList<SchoolClass> classes;
    FirebaseFirestore db;
    SchoolClassArrayAdapter classAdapter;
    private FloatingActionButton addClassButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.class_selector_fragment, container, false);
        classList = view.findViewById(R.id.class_fragment_class_list);

        user_id = (Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
        db = FirebaseFirestore.getInstance();

        classes = new ArrayList<SchoolClass>();
        classAdapter = new SchoolClassArrayAdapter(this.getActivity(),classes);
        classList.setAdapter(classAdapter);
        addClassButton = view.findViewById(R.id.CLASS_FRAGMENT_add_class_button);
        Log.d("User_id", user_id);
        Query userClassQuery = db.collection("Classes").whereEqualTo("user_id",user_id);
        userClassQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                classes.clear();
                for (QueryDocumentSnapshot doc : value) {
                    classes.add(new SchoolClass((String) doc.get("Number"), (String) doc.get("Section"), (String) doc.get("Subject")));
                }
                classAdapter.notifyDataSetChanged();

            }
        });
        /*
        Goes to selected class fragment
         */
        classList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SchoolClass schoolClass = classes.get(position);
                Query userClassesQuery = db.collection("Classes")
                        .whereEqualTo("Subject", schoolClass.getSubject())
                        .whereEqualTo("Number", schoolClass.getNumber())
                        .whereEqualTo("Section", schoolClass.getSection());
                userClassesQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Bundle bundle = new Bundle();
                                bundle.putString("ID", documentSnapshot.getId());
                                Log.d("ClassSelector", documentSnapshot.getId());
                                StudyMaterialSelectorFragment classFragment = new StudyMaterialSelectorFragment();
                                classFragment.setArguments(bundle);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.container, classFragment)
                                        .addToBackStack(null)
                                        .commit();

                            }
                        }
                    }
                });
            }
        });

        /*
        Class Deletion
         */
        classList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SchoolClass schoolClass = classes.get(position);
                Query userClassesQuery = db.collection("Classes")
                        .whereEqualTo("Subject", schoolClass.getSubject())
                        .whereEqualTo("Number", schoolClass.getNumber())
                        .whereEqualTo("Section", schoolClass.getSection());
                userClassesQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Do you want to delete ".concat(schoolClass.getFullClassName()).concat(" ?"))
                                        .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Confirm</font>"), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                db.collection("Classes").document(documentSnapshot.getId()).delete();
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })

                                        .show();
                            }
                        }
                    }
                });
                return true;
            }
        });

        return view;


    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        addClassButton.setOnClickListener(new View.OnClickListener() {
          @Override
        public void onClick(View view) {
              AddClassFragment addClassFragment = new AddClassFragment();
              FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
              fragmentManager.beginTransaction()
                      .replace(R.id.container, addClassFragment)
                      .addToBackStack(null)
                      .commit();
        }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
