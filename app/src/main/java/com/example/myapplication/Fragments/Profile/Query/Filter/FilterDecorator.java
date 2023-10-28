package com.example.myapplication.Fragments.Profile.Query.Filter;

import com.google.firebase.firestore.QueryDocumentSnapshot;

public class FilterDecorator extends QueryFilter {
    protected QueryFilter queryFilter;
    public FilterDecorator(QueryFilter queryFilter) {
        this.queryFilter = queryFilter;
    }

    @Override
    public boolean filter(QueryDocumentSnapshot queryDocumentSnapshot) {
        return queryFilter.filter(queryDocumentSnapshot);
    }
}
