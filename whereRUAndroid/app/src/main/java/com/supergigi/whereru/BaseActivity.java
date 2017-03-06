package com.supergigi.whereru;

import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tedwei on 3/2/17.
 */

abstract public class BaseActivity extends AppCompatActivity {

    private class ManagedFirebaseObserver {
        Query query;
        ChildEventListener childEventListener;
        ValueEventListener valueEventListener;
    }

    private List<ManagedFirebaseObserver> fbObserversList = new ArrayList<>();
    private List<FirebaseRecyclerAdapter> fbRecyclerAdapterList = new ArrayList<>();

    final protected void addValueEventListener(Query query, ValueEventListener listener) {
        ManagedFirebaseObserver observer = new ManagedFirebaseObserver();
        observer.query = query;
        observer.valueEventListener = listener;
        query.addValueEventListener(listener);
        fbObserversList.add(observer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (ManagedFirebaseObserver observer : fbObserversList) {
            if (observer.childEventListener != null) {
                observer.query.removeEventListener(observer.childEventListener);
            } else if (observer.valueEventListener != null) {
                observer.query.removeEventListener(observer.valueEventListener);
            }
        }
        fbObserversList.clear();

        for (FirebaseRecyclerAdapter adapter : fbRecyclerAdapterList) {
            adapter.cleanup();
        }
        fbRecyclerAdapterList.clear();
    }
}
