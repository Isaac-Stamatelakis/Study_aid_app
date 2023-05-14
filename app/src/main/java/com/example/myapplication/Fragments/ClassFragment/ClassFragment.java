package com.example.myapplication.Fragments.ClassFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class ClassFragment extends Fragment {
    ListView classList;
    ArrayList<SchoolClass> classes;
    ClassCustomerList classAdapter;
    private FloatingActionButton addClassButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.class_fragment, container, false);
        classList = view.findViewById(R.id.class_fragment_class_list);
        classes = new ArrayList<SchoolClass>();
        classes.add(new SchoolClass("301",null,"CMPUT"));
        classes.add(new SchoolClass("466",null,"CMPUT"));
        classAdapter = new ClassCustomerList(this.getActivity(),classes);
        classList.setAdapter(classAdapter);
        addClassButton = view.findViewById(R.id.CLASS_FRAGMENT_add_class_button);

        return view;


    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        addClassButton.setOnClickListener(new View.OnClickListener() {
          @Override
        public void onClick(View view) {
              AddClassFragment addClassFragment = new AddClassFragment();
              Bundle bundle = new Bundle();
              bundle.putString("user_id",null);
              addClassFragment.setArguments(bundle);
              FragmentManager fragmentManager = getParentFragmentManager();
              FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
              fragmentTransaction.replace(R.id.container, addClassFragment);
              fragmentTransaction.commit();
        }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
