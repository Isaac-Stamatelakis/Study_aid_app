package com.example.myapplication.Fragments.Profile;

import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EditProfileFragment extends Fragment {

    FirebaseFirestore db;
    String user_id;
    Spinner educationSpinner;
    EditText nameText;
    EditText schoolText;
    EditText educationLevelText;
    EditText facultyText;
    EditText majorText;
    EditText minorText;
    EditText yearText;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_profile_fragment, container, false);

        db = FirebaseFirestore.getInstance();
        user_id = (Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));

        //Views
        nameText = view.findViewById(R.id.edit_profile_name);
        schoolText = view.findViewById(R.id.edit_profile_school);
        educationLevelText = view.findViewById(R.id.edit_profile_education_level);
        facultyText = view.findViewById(R.id.edit_profile_faculty);
        majorText = view.findViewById(R.id.edit_profile_major);
        minorText = view.findViewById(R.id.edit_profile_minor);
        yearText = view.findViewById(R.id.edit_profile_year);

        //Spinner
        String[] educationTypes = new String[]{"University", "Technical School","Grade School"};
        educationSpinner = view.findViewById(R.id.edit_profile_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.black_text_spinner, educationTypes);
        educationSpinner.setAdapter(spinnerAdapter);

        educationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (educationTypes[position]) {
                    case "University":
                        multiSetVisibility(new EditText[]{nameText, schoolText, facultyText, educationLevelText, majorText, minorText, yearText});
                        break;
                    case "Technical School":
                        multiSetVisibility(new EditText[]{nameText, schoolText, facultyText, yearText});
                        break;
                    case "Grade School":
                        multiSetVisibility(new EditText[]{nameText, schoolText, yearText});
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    public void multiSetVisibility(EditText[] visibleEditText) {
        EditText[] allEditText = new EditText[]{nameText, schoolText, facultyText, educationLevelText, majorText, minorText, yearText};
        for (EditText editText: allEditText) {
            editText.setVisibility(View.GONE);
        }
        for (EditText editText: visibleEditText) {
            editText.setVisibility(View.VISIBLE);
        }
    }



}