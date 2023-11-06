package com.example.myapplication.Fragments.Classes.ClassSelectorFragment;

import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Fragments.Classes.SchoolClass.SchoolClass;
import com.example.myapplication.Fragments.Classes.SchoolClass.SchoolClassArrayAdapter;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public abstract class AbstractClassSelectorFragment extends Fragment {
    ListView classList;
    String user_id;
    String TAG = "Class Selector Fragment";
    ArrayList<SchoolClass> classes;
    FirebaseFirestore db;
    SchoolClassArrayAdapter classAdapter;
    FloatingActionButton addClassButton;
    CalendarView calendarView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflateView(inflater,container);

        setViews(view);
        setViewListeners(view);
        user_id = (Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
        db = FirebaseFirestore.getInstance();

        classes = new ArrayList<SchoolClass>();
        initAdapter();

        getClassFromDB();
        return view;


    }
    protected void setViews(View view) {
        classList = view.findViewById(R.id.class_fragment_class_list);
        calendarView = view.findViewById(R.id.class_calender);
        addClassButton = view.findViewById(R.id.CLASS_FRAGMENT_add_class_button);
    }
    protected void setViewListeners(View view) {
        classList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                classSelected(classAdapter.getItem(position));
            }
        });
    }
    protected void classSelected(SchoolClass schoolClass) {

    }
    protected void initAdapter() {
        classAdapter = new SchoolClassArrayAdapter(this.getActivity(),classes);
        classList.setAdapter(classAdapter);
    }
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.class_selector_fragment, container, false);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected void getClassFromDB() {
        Query userClassQuery = getClassQuery();
        userClassQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                classes.clear();
                for (QueryDocumentSnapshot doc : value) {
                    SchoolClass schoolClass = new SchoolClass((String) doc.get("Number"), (String) doc.get("Section"), (String) doc.get("Subject"));
                    schoolClass.setDbID(doc.getId());
                    schoolClass.setInstitution((String) doc.get("Institution"));
                    classes.add(schoolClass);
                }
                classAdapter.notifyDataSetChanged();

            }
        });
    }
    protected Query getClassQuery() {
        return db.collection("Classes").whereEqualTo("user_id",user_id);
    }


}
