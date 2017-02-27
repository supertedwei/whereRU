package com.supergigi.whereru.firebase;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by tedwei on 12/2/16.
 */

abstract public class FirebaseViewHolder extends RecyclerView.ViewHolder {

    protected DatabaseReference databaseReference;

    public FirebaseViewHolder(View itemView) {
        super(itemView);
    }

    public void setDatabaseReference(DatabaseReference ref) {
        this.databaseReference = ref;
    }

    public String getFirebaseUrl() {
        return this.databaseReference.toString();
    }
}
