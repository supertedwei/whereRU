package com.supergigi.whereru;

import android.accounts.Account;

/**
 * Created by tedwei on 3/2/17.
 */

public class Consts {

    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.supergigi.whereru.datasync.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.supergigi.whereru.datasync";
    // The account name
    public static final String ACCOUNT = "myaccount";

    public static final Account sAccount = new Account(Consts.ACCOUNT, Consts.ACCOUNT_TYPE);


}
