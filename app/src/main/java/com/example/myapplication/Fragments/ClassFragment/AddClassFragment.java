package com.example.myapplication.Fragments.ClassFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddClassFragment extends Fragment {
    ListView classList;
    ArrayList<SchoolClass> classes;
    ClassCustomerList classAdapter;
    EditText searchBar;
    String user_id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_class_fragment, container, false);
        classList = view.findViewById(R.id.add_class_dialog_class_list);
        searchBar = view.findViewById(R.id.add_class_dialog_class_search);

        classes = new ArrayList<SchoolClass>();
        classes.add(new SchoolClass("301",null,"CMPUT"));
        classes.add(new SchoolClass("466",null,"CMPUT"));
        classAdapter = new ClassCustomerList(this.getActivity(),classes);
        classList.setAdapter(classAdapter);

        return view;


    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        classList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SchoolClass schoolClass = classes.get(position);
                StringBuilder dialogTitle = new StringBuilder().append("<font color='#758BFD'>Do you want to add ")
                        .append(schoolClass.getSubject())
                        .append(" ")
                        .append(schoolClass.getNumber());
                if (schoolClass.getSection() != null) {
                    dialogTitle.append(schoolClass.getSection());
                }
                dialogTitle.append(" to your classes?</font>");
                new AlertDialog.Builder(getActivity())
                        .setTitle(Html.fromHtml(dialogTitle.toString()))
                        .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Confirm</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(Html.fromHtml("<font color = '#AEB8FE'>Cancel</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /*
    Searchs for all classes which match the searchText and are at the
    user_id's university/school
     */
    public void getUserClasses(String searchText){

    }
}
