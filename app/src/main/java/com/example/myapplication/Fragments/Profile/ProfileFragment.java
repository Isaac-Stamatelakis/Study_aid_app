package com.example.myapplication.Fragments.Profile;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.Profile.FriendRequestFragment.FriendRequestFragment;
import com.example.myapplication.Fragments.Profile.Query.Filter.FriendRequestFilter;
import com.example.myapplication.R;
import com.example.myapplication.StaticHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    FirebaseFirestore db;
    String user_id;
    TextView nameText;
    TextView schoolText;
    TextView majorText;
    TextView minorText;
    TextView educationLevelText;
    TextView facultyText;
    TextView yearText;
    ImageView editProfileButton;
    ImageView addFriend;
    User user;
    public ProfileFragment(User user) {
        this.user = user;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        db = FirebaseFirestore.getInstance();
        user_id = (Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));

        //Views
        educationLevelText = view.findViewById(R.id.profile_education_level_text); educationLevelText.setVisibility(View.GONE);
        nameText = view.findViewById(R.id.profile_username_text); nameText.setVisibility(View.INVISIBLE);
        schoolText = view.findViewById(R.id.profile_school_text); schoolText.setVisibility(View.GONE);
        majorText = view.findViewById(R.id.profile_major_text); majorText.setVisibility(View.GONE);
        minorText = view.findViewById(R.id.profile_minor_text); minorText.setVisibility(View.GONE);
        facultyText = view.findViewById(R.id.profile_faculty_text); facultyText.setVisibility(View.GONE);
        yearText = view.findViewById(R.id.profile_year_text); yearText.setVisibility(View.GONE);
        editProfileButton = view.findViewById(R.id.profile_edit_button);
        addFriend = view.findViewById(R.id.profile_fragment_add_friend);

        // Set Text
        if (user.getName() == null || user.getName().length() == 0) {
            nameText.setText("Username"); nameText.setVisibility(View.VISIBLE);
        } else {
            nameText.setText(user.getName()); nameText.setVisibility(View.VISIBLE);
        }
        putStringIntoTextView(educationLevelText, user.getEducationLevel(), null);
        putStringIntoTextView(facultyText, user.getFaculty(), null);
        putStringIntoTextView(yearText, user.getYear(), null);
        putArrayIntoTextView(schoolText, user.getSchools());
        putArrayIntoTextView(majorText, user.getMajors());
        putArrayIntoTextView(minorText, user.getMinors());

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new EditProfileFragment(user))
                        .addToBackStack(null)
                        .commit();
            }
        });
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendRequestFragment friendRequestFragment = new FriendRequestFragment(user);
                StaticHelper.switchFragment(getActivity().getSupportFragmentManager(),friendRequestFragment,null);

            }
        });

        return view;



    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void putArrayIntoTextView(TextView textView, ArrayList<String> strings) {

        if (strings.size() == 0) {
            return;
        }
        if (strings.size() == 1 && strings.get(0).length() == 0) {
            return;
        }
        textView.setVisibility(View.VISIBLE);
        StringBuilder stringBuilder = new StringBuilder()
                .append(textView.getText().toString());
        if (strings.size() > 1) {
            stringBuilder.append("s");
        }
        stringBuilder.append(": ");
        for (int i = 0; i < strings.size(); i ++) {
            stringBuilder.append(strings.get(i));
            if (i != strings.size()-1) {
                stringBuilder.append(", ");
            }
        }
        textView.setText(stringBuilder.toString());

    }

    public void putStringIntoTextView(TextView textView, String string, String nullText) {
        if ((String) string == null || string.length() ==0){
            if (nullText == null) {
                return;
            }
            string = nullText;
        }
        textView.setText(textView.getText().toString().concat(": ").concat(string));
        textView.setVisibility(View.VISIBLE);
    }

}