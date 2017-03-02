package com.supergigi.whereru.geocoder;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by tedwei on 3/2/17.
 */

public class Geocoder {

    private static final String PREFERENCES_GEOCODER = "com.doctoror.geocoder.preferences";

    private static final String KEY_ALLOW = "com.doctoror.geocoder.preferences.keys.allow";

    private static final String ENDPOINT_URL = "https://maps.googleapis.com/maps/api/geocode/json";

    @NonNull
    private final Context mContext;

    @NonNull
    private final Locale mLocale;

    @Nullable
    private final String mApiKey;

    private SharedPreferences mSharedPreferences;

    private long mAllowedDate;

    /**
     * Constructs a Geocoder whose responses will be localized for the given {@link Locale} with no
     * API key
     *
     * @param context the Context of the calling Activity
     * @param locale  The Locale to use
     */
    public Geocoder(@NonNull final Context context, @NonNull final Locale locale) {
        this(context, locale, null);
    }

    /**
     * Constructs a Geocoder that will use your API key and whose responses will be localized for
     * the given {@link Locale}
     *
     * @param context the Context of the calling Activity
     * @param locale  the Locale to use
     * @param apiKey  your application's API key. This key identifies your application for
     *                purposes of quota management. This must be a <b>Server key</b>, not Android
     *                key created in Google Developer Console
     */
    public Geocoder(@NonNull final Context context, @NonNull final Locale locale,
                    @Nullable final String apiKey) {
        //noinspection ConstantConditions
        if (context == null) {
            throw new NullPointerException("context == null");
        }
        //noinspection ConstantConditions
        if (locale == null) {
            throw new NullPointerException("locale == null");
        }
        mContext = context;
        mLocale = locale;
        mApiKey = apiKey;
    }

    @NonNull
    private Uri.Builder buildBaseRequestUri() {
        final Uri.Builder uriBuilder = Uri.parse(ENDPOINT_URL).buildUpon()
                .appendQueryParameter("language", mLocale.toString());
        if (mApiKey != null && !mApiKey.isEmpty()) {
            uriBuilder.appendQueryParameter("key", mApiKey);
        }
        return uriBuilder;
    }

    /**
     * Returns an array of Addresses that are known to describe the area
     * immediately surrounding the given latitude and longitude. The returned
     * addresses will be localized for the locale provided to this class's
     * constructor.
     *
     * <p>
     * The returned values may be obtained by means of a network lookup. The
     * results are a best guess and are not guaranteed to be meaningful or
     * correct. It may be useful to call this method from a thread separate from
     * your primary UI thread.
     *
     * @param latitude               the latitude a point for the search
     * @param longitude              the longitude a point for the search
     * @param maxResults             max number of addresses to return. Smaller numbers (1 to 5)
     *                               are recommended
     * @param parseAddressComponents If set to true, will parse "address_components". For more
     *                               details see documentation provided in the Geocoder class
     *                               javadoc
     * @return a list of Address objects. Returns empty list if no matches were found.
     * @throws IllegalArgumentException if latitude is less than -90 or greater than 90
     * @throws IllegalArgumentException if longitude is less than -180 or greater than 180
     * @throws GeocoderException        On Geocoder error, if the network is unavailable or any
     *                                  other I/O problem occurs
     */
    @NonNull
    public List<Address> getFromLocation(final double latitude, final double longitude,
                                         final int maxResults, final boolean parseAddressComponents)
            throws GeocoderException {
        if (latitude < -90.0 || latitude > 90.0) {
            throw new IllegalArgumentException("latitude == " + latitude);
        }
        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException("longitude == " + longitude);
        }

        if (isLimitExceeded()) {
            throw GeocoderException.forQueryOverLimit();
        }

        final Uri.Builder uriBuilder = buildBaseRequestUri()
                .appendQueryParameter("sensor", "true")
                .appendQueryParameter("latlng", latitude + "," + longitude);

        final byte[] data;
        try {
            data = download(uriBuilder.toString());
        } catch (IOException e) {
            throw new GeocoderException(e);
        }

        return Parser.parseJson(data, maxResults, parseAddressComponents);
    }

    /**
     * Returns an array of Addresses that are known to describe the named
     * location, which may be a place name such as "Dalvik,
     * Iceland", an address such as "1600 Amphitheatre Parkway, Mountain View,
     * CA", an airport code such as "SFO", etc.. The returned addresses will be
     * localized for the locale provided to this class's constructor.
     *
     * <p>
     * The query will block and returned values will be obtained by means of a
     * network lookup. The results are a best guess and are not guaranteed to be
     * meaningful or correct. It may be useful to call this method from a thread
     * separate from your primary UI thread.
     *
     * @param locationName           a user-supplied description of a location
     * @param maxResults             max number of results to return. Smaller numbers (1 to 5) are
     *                               recommended
     * @param parseAddressComponents If set to true, will parse "address_components". For more
     *                               details see documentation provided in the Geocoder class
     *                               javadoc
     * @return a list of Address objects. Returns empty list if no matches were found.
     * @throws IllegalArgumentException if locationName is null
     * @throws GeocoderException        if parse failed, Geocoder returned error, or if the network
     *                                  is unavailable or any other I/O problem occurs
     */
    @NonNull
    public List<Address> getFromLocationName(final String locationName, final int maxResults,
                                             final boolean parseAddressComponents)
            throws GeocoderException {
        if (locationName == null) {
            throw new IllegalArgumentException("locationName == null");
        }

        if (isLimitExceeded()) {
            throw GeocoderException.forQueryOverLimit();
        }

        final Uri.Builder uriBuilder = buildBaseRequestUri()
                .appendQueryParameter("sensor", "false")
                .appendQueryParameter("address", locationName);

        final String url = uriBuilder.toString();
        byte[] data;
        try {
            data = download(url);
        } catch (IOException e) {
            throw new GeocoderException(e);
        }

        try {
            return Parser.parseJson(data, maxResults, parseAddressComponents);
        } catch (GeocoderException e) {
            if (e.getStatus() == Status.OVER_QUERY_LIMIT) {
                // OVER_QUERY_LIMIT could be thrown if too many calls per second
                // If after two seconds, it is thrown again - then it means there are too much calls
                // per 24 hours
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    // Safely abort when interrupted
                    return new ArrayList<>();
                }

                try {
                    data = download(url);
                } catch (IOException ioe) {
                    throw new GeocoderException(ioe);
                }

                try {
                    return Parser.parseJson(data, maxResults, parseAddressComponents);
                } catch (GeocoderException e1) {
                    if (e1.getStatus() == Status.OVER_QUERY_LIMIT) {
                        // available in 24 hours
                        setAllowedDate(System.currentTimeMillis() + 86400000L);
                    }
                    throw e1;
                }
            } else {
                throw e;
            }
        }
    }

    /**
     * Downloads data to buffer
     *
     * @param url Data location
     * @return downloaded data or null if error occurred
     */
    @NonNull
    private static byte[] download(String url) throws IOException {
        InputStream is = null;
        ByteArrayOutputStream os = null;

        try {

            final URL u = new URL(url);
            final URLConnection connection = u.openConnection();
            connection.connect();

            is = connection.getInputStream();
            os = new ByteArrayOutputStream();

            final byte[] buffer = new byte[4096];
            int read;

            while (true) {
                read = is.read(buffer, 0, buffer.length);
                if (read == -1) {
                    break;
                }
                os.write(buffer, 0, read);
            }

            return os.toByteArray();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    /**
     * Returns true if limit is exceeded and next query is not allowed
     *
     * @return true if limit is exceeded and next query is not allowed; false
     * otherwise
     */
    private boolean isLimitExceeded() {
        return System.currentTimeMillis() <= getAllowedDate();
    }

    /**
     * Sets date after which next geocoding query is allowed
     *
     * @param date the date after which next geocoding query is allowed
     */
    private void setAllowedDate(final long date) {
        mAllowedDate = date;
        if (mSharedPreferences == null) {
            mSharedPreferences = mContext.getSharedPreferences(
                    PREFERENCES_GEOCODER, Context.MODE_PRIVATE);
        }
        final SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putLong(KEY_ALLOW, date);
        e.apply();
    }

    /**
     * Returns date after which the next geocoding query is allowed
     *
     * @return date after which the next geocoding query is allowed
     */
    private long getAllowedDate() {
        if (mSharedPreferences == null) {
            mSharedPreferences = mContext
                    .getSharedPreferences(PREFERENCES_GEOCODER, Context.MODE_PRIVATE);
            mAllowedDate = mSharedPreferences.getLong(KEY_ALLOW, 0);
        }
        return mAllowedDate;
    }
}
