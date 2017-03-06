package com.supergigi.whereru;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tedwei on 11/25/16.
 */

abstract public class BaseFragment extends Fragment {

    private class ManagedFirebaseObserver {
        Query query;
        ChildEventListener childEventListener;
        ValueEventListener valueEventListener;
    }

    private List<ManagedFirebaseObserver> fbObserversList = new ArrayList<>();
    private List<FirebaseRecyclerAdapter> fbRecyclerAdapterList = new ArrayList<>();

    final protected void finishFragment() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.onBackPressed();
        }
    }

    final protected void addSubFragment(Fragment fragment) {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
//            activity.addSubFragment(fragment);
        }
    }

    final protected void addChildEventListener(Query query, ChildEventListener listener) {
        ManagedFirebaseObserver observer = new ManagedFirebaseObserver();
        observer.query = query;
        observer.childEventListener = listener;
        query.addChildEventListener(listener);
        fbObserversList.add(observer);
    }

    final protected void addValueEventListener(Query query, ValueEventListener listener) {
        ManagedFirebaseObserver observer = new ManagedFirebaseObserver();
        observer.query = query;
        observer.valueEventListener = listener;
        query.addValueEventListener(listener);
        fbObserversList.add(observer);
    }

    final protected void addFirebaseRecyclerAdapter(FirebaseRecyclerAdapter adapter) {
        fbRecyclerAdapterList.add(adapter);
    }


    final protected void addListenerForSingleValueEvent(Query query, ValueEventListener listener) {
        query.addListenerForSingleValueEvent(listener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

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

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = activity.getCurrentFocus();
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
