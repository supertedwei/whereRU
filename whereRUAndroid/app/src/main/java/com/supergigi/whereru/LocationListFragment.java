package com.supergigi.whereru;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.Query;
import com.supergigi.whereru.firebase.BaseFirebaseRecyclerAdapter;
import com.supergigi.whereru.firebase.FbLocation;
import com.supergigi.whereru.firebase.FirebaseUtil;
import com.supergigi.whereru.firebase.FirebaseViewHolder;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationListFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private ProgressBar spinnerView;


    public LocationListFragment() {
        // Required empty public constructor
    }

    public static LocationListFragment newInstance(/*String param1, String param2*/) {
        LocationListFragment fragment = new LocationListFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location_list, container, false);

        spinnerView = (ProgressBar) rootView.findViewById(R.id.spinner);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        ItemRecyclerViewAdapter adapter = new ItemRecyclerViewAdapter(
                FbLocation.class,
                R.layout.fragment_location_list_content,
                ViewHolder.class,
                FirebaseUtil.getDeviceLocationLog());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).build());
        addFirebaseRecyclerAdapter(adapter);
//
//        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addSubFragment(CreateTemplateFragment.newInstance());
//            }
//        });

        return rootView;
    }

    public class ItemRecyclerViewAdapter extends BaseFirebaseRecyclerAdapter<FbLocation, ViewHolder> {
        /**
         * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
         * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
         *                        instance of the corresponding view with the data from an instance of modelClass.
         * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
         * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
         *                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
         */
        public ItemRecyclerViewAdapter(Class<FbLocation> modelClass, int modelLayout, Class<ViewHolder> viewHolderClass, Query ref) {
            super(modelClass, modelLayout, viewHolderClass, ref);
        }

        @Override
        protected void populateViewHolder(ViewHolder viewHolder, FbLocation model, int position) {
            super.populateViewHolder(viewHolder, model, position);
            viewHolder.setData(model, LocationListFragment.this);
            spinnerView.setVisibility(View.GONE);
        }
    }

    public static class ViewHolder extends FirebaseViewHolder {
        public final View mView;
        public final TextView addressView;
        public FbLocation item;
        LocationListFragment parent;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            addressView = (TextView) view.findViewById(R.id.address);

//            mView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    parent.addSubFragment(ViewTemplateFragment.newInstance(getFirebaseUrl()));
//                }
//            });
        }

        public void setData(FbLocation data, LocationListFragment parent) {
            this.parent = parent;
            item = data;
            addressView.setText(item.getAddress() + "\n" + item.getDateTimestamp().toString());
//            titleView.setText(item.getName());
        }
    }

}
