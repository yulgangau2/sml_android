package com.eatyhero.customer.base;

import com.eatyhero.customer.model.SearchOptionList;

import java.util.ArrayList;

class SearchOptionSingleton {
    private static final SearchOptionSingleton ourInstance = new SearchOptionSingleton();

    static SearchOptionSingleton getInstance() {
        return ourInstance;
    }

    private SearchOptionSingleton() {
    }

    public ArrayList<SearchOptionList> searchOptionList = new ArrayList<>();
    void init() {
        searchOptionList = new ArrayList<>();
    }
}
