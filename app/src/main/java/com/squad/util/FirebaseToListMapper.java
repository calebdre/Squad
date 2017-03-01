package com.squad.util;


import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

public class FirebaseToListMapper<T>  {

    Func1<DataSnapshot, T> listTransform;

    public FirebaseToListMapper(Func1<DataSnapshot, T> listTransform) {
        this.listTransform = listTransform;
    }

    public Func1<DataSnapshot, List<T>> map() {
        return dataSnapshot -> {
            List<T> list = new ArrayList<>();
            for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                list.add(listTransform.call(snapshot));
            }

            return list;
        };
    }
}
