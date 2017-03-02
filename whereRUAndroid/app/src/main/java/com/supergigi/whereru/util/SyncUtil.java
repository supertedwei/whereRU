package com.supergigi.whereru.util;

import android.content.ContentResolver;
import android.os.Bundle;

import com.supergigi.whereru.Consts;

/**
 * Created by tedwei on 3/2/17.
 */

public class SyncUtil {

    public static final void requestSync() {
        Bundle settingsBundle = new Bundle();
        ContentResolver.requestSync(Consts.sAccount, Consts.AUTHORITY, settingsBundle);
    }
}
