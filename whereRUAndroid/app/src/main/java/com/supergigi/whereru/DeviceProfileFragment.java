package com.supergigi.whereru;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.supergigi.whereru.firebase.FbDeviceProfile;
import com.supergigi.whereru.firebase.FirebaseUtil;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceProfileFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private EditText nameView;


    public DeviceProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DeviceProfileFragment newInstance(/*String param1, String param2*/) {
        DeviceProfileFragment fragment = new DeviceProfileFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device_profile, container, false);
        this.nameView = (EditText) rootView.findViewById(R.id.name);
        rootView.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameView.getEditableText().toString();
                FirebaseUtil.updateDeviceProfile(name);
            }
        });

        FirebaseUtil.getDeviceProfile().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FbDeviceProfile fbDeviceProfile = dataSnapshot.getValue(FbDeviceProfile.class);
                if (fbDeviceProfile == null) {
                    fbDeviceProfile = new FbDeviceProfile();
                    FirebaseUtil.getDeviceProfile().setValue(fbDeviceProfile);
                }
                nameView.setText(fbDeviceProfile.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

}
