package com.example.myapplication.Fragments.Profile.Query.Filter;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Locale;

public class NameFilter extends FilterDecorator{
    protected String search;
    public NameFilter(QueryFilter queryFilter, String search) {
        super(queryFilter);
        this.search = search;
    }

    @Override
    public boolean filter(QueryDocumentSnapshot queryDocumentSnapshot) {
        String name = (String) queryDocumentSnapshot.get("Name");
        if (search == null || search.isEmpty()) {
            return true;
        }
        return name != null && name.toLowerCase(Locale.CANADA).contains(search) && super.filter(queryDocumentSnapshot);
    }
}
