package com.supergigi.whereru.geocoder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by tedwei on 3/2/17.
 */

public class GeocoderException extends Exception {

    private Status mStatus;
    private String mErrorMessage;

    @NonNull
    public static GeocoderException forStatus(@NonNull final Status status) {
        final GeocoderException e = new GeocoderException();
        e.setStatus(status);
        return e;
    }

    @NonNull
    public static GeocoderException forQueryOverLimit() {
        return forStatus(Status.OVER_QUERY_LIMIT);
    }

    public GeocoderException() {
    }

    public GeocoderException(final Throwable cause) {
        super(cause);
    }

    @Nullable
    public Status getStatus() {
        return mStatus;
    }

    void setStatus(final Status status) {
        mStatus = status;
    }

    @Nullable
    public String getErrorMessage() {
        return mErrorMessage;
    }

    void setErrorMessage(final String errorMessage) {
        mErrorMessage = errorMessage;
    }

    public boolean isCausedByNetworkError() {
        return getCause() instanceof IOException;
    }

    @Override
    public String toString() {
        if (mErrorMessage != null && !mErrorMessage.isEmpty()) {
            return mErrorMessage;
        }
        if (mStatus != null) {
            return mStatus.name();
        }
        final Throwable cause = getCause();
        if (cause != null) {
            return cause.toString();
        }
        return super.toString();
    }
}
