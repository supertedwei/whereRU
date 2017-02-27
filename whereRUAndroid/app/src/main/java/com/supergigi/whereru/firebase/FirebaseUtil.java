package com.supergigi.whereru.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by tedwei on 27/02/2017.
 */

public class FirebaseUtil {

    private static final String TAG = FirebaseUtil.class.getSimpleName();

    private static final String FIREBASE_USERS = "users";
    private static final String FIREBASE_DATA_LOCATION = "data/location";
//    private static final String FIREBASE_AUTH_CONFIRM_EMAIL_SEND = "auth/confirm_email_sent";
//    public static final String FIREBASE_DATA_ITEM_MASTER = "data/item/master";
//    public static final String FIREBASE_DATA_ITEM_DETAIL = "data/item/detail";
//    public static final String FIREBASE_TEMPLATE_DETAIL = "data/template/detail";
//    public static final String FIREBASE_TEMPLATE_DEFAULT_CREATED = "data/template/default/created";

    public static final DatabaseReference getUserReference() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        return rootRef.child(FIREBASE_USERS).child(user.getUid());
    }

    public static final DatabaseReference getDataLocation() {
        return getUserReference().child(FIREBASE_DATA_LOCATION);
    }
//
//    public static final DatabaseReference getConfirmEmailSentRef() {
//        return getUserReference().child(FIREBASE_AUTH_CONFIRM_EMAIL_SEND);
//    }
//
//    public static final DatabaseReference getDataItemMaster() {
//        return getUserReference().child(FIREBASE_DATA_ITEM_MASTER);
//    }
//
//    public static final DatabaseReference getDataItemDetail() {
//        return getUserReference().child(FIREBASE_DATA_ITEM_DETAIL);
//    }
//
//    public static final DatabaseReference getTemplateDetail() {
//        return getUserReference().child(FIREBASE_TEMPLATE_DETAIL);
//    }
//
//    public static final DatabaseReference getTemplateDefaultCreated() {
//        return getUserReference().child(FIREBASE_TEMPLATE_DEFAULT_CREATED);
//    }
}
