package com.supergigi.whereru.geocoder;

import android.support.annotation.Nullable;

/**
 * Created by tedwei on 3/2/17.
 */

public enum Status {

    /**
     * Indicates that no errors occurred; the address was successfully parsed and at least one
     * geocode was returned.
     */
    OK,

    /**
     * Indicates that the geocode was successful but returned no results. This may occur if the
     * geocoder was passed a non-existent address
     */
    ZERO_RESULTS,

    /**
     * Indicates that you are over your quota.
     */
    OVER_QUERY_LIMIT,

    /**
     * Indicates that your request was denied
     */
    REQUEST_DENIED,

    /**
     * Generally indicates that the query (address, components or latlng) is missing.
     */
    INVALID_REQUEST,

    /**
     * Indicates that the request could not be processed due to a server error. The request may
     * succeed if you try again.
     */
    UNKNOWN_ERROR;

    static Status fromString(@Nullable final String status) {
        if (status == null || status.isEmpty()) {
            return UNKNOWN_ERROR;
        }
        try {
            return valueOf(status);
        } catch (IllegalArgumentException e) {
            return UNKNOWN_ERROR;
        }
    }
}
