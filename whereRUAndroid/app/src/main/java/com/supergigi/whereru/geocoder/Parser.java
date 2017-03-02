package com.supergigi.whereru.geocoder;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tedwei on 3/2/17.
 */

public class Parser {

    /*
     * Status codes which we handle
     */


    private static final String ERROR_MESSAGE = "error_message";

    private static final String STATUS = "status";

    private static final String RESULTS = "results";

    private static final String GEOMETRY = "geometry";

    private static final String LOCATION = "location";

    private static final String LOCATION_TYPE = "location_type";

    private static final String VIEWPORT = "viewport";

    private static final String BOUNDS = "bounds";

    private static final String SOUTHWEST = "southwest";

    private static final String NORTHEAST = "northeast";

    private static final String LAT = "lat";

    private static final String LNG = "lng";

    private static final String FORMATTED_ADDRESS = "formatted_address";

    private static final String ADDRESS_COMPONENTS = "address_components";

    private static final String TYPES = "types";

    private static final String LONG_NAME = "long_name";

    private static final String SHORT_NAME = "short_name";

    private Parser() {
    }

    /**
     * Parses response into {@link List}
     *
     * @param jsonData
     * @param maxResults
     * @param parseAddressComponents
     * @return {@link Address} {@link List}
     * @throws GeocoderException if error occurs
     */
    @NonNull
    static List<Address> parseJson(final byte[] jsonData,
                                   final int maxResults,
                                   final boolean parseAddressComponents)
            throws GeocoderException {
        try {
            final String jsonString = new String(jsonData, Charset.forName("UTF-8"));
            final JSONObject jsonObject = new JSONObject(jsonString);

            if (!jsonObject.has(STATUS)) {
                throw new GeocoderException(new JSONException("No \"status\" field"));
            }

            final Status status = Status.fromString(jsonObject.getString(STATUS));
            switch (status) {
                case OK:
                    if (jsonObject.has(RESULTS)) {
                        return parseResults(maxResults, parseAddressComponents, jsonObject);
                    }
                    return new ArrayList<>();

                case ZERO_RESULTS:
                    return new ArrayList<>();

                default:
                    final GeocoderException e = GeocoderException.forStatus(status);
                    try {
                        if (jsonObject.has(ERROR_MESSAGE)) {
                            e.setErrorMessage(jsonObject.getString(ERROR_MESSAGE));
                        }
                    } catch (JSONException ignored) {
                    }
                    throw e;
            }
        } catch (JSONException e) {
            throw new GeocoderException(e);
        }
    }

    private static List<Address> parseResults(final int maxResults,
                                              final boolean parseAddressComponents,
                                              @NonNull final JSONObject o) throws JSONException {
        final JSONArray results = o.getJSONArray(RESULTS);
        final int count = results.length() >= maxResults ? maxResults : results.length();
        final ArrayList<Address> addressList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {

            final Address address = new Address();
            final JSONObject result = results.getJSONObject(i);

            if (result.has(FORMATTED_ADDRESS)) {
                address.setFormattedAddress(result.getString(FORMATTED_ADDRESS));
            }

            parseGeometry(result, address);

            if (parseAddressComponents) {
                parseAddressComponents(result, address);
            }

            addressList.add(address);
        }
        return addressList;
    }

    private static void parseGeometry(@NonNull final JSONObject result,
                                      @NonNull final Address current) throws JSONException {
        if (result.has(GEOMETRY)) {
            final JSONObject geometry = result.getJSONObject(GEOMETRY);
            if (geometry.has(LOCATION_TYPE)) {
                current.setLocationType(geometry.getString(LOCATION_TYPE));
            }

            if (geometry.has(LOCATION)) {
                final JSONObject location = geometry.getJSONObject(LOCATION);
                current.setLocation(new Address.Location(location.getDouble(LAT),
                        location.getDouble(LNG)));
            }

            if (geometry.has(VIEWPORT)) {
                final JSONObject viewport = geometry.getJSONObject(VIEWPORT);
                if (viewport.has(SOUTHWEST) && viewport.has(NORTHEAST)) {
                    final JSONObject southwest = viewport.getJSONObject(SOUTHWEST);
                    final Address.Location locationSouthwest = new Address.Location(
                            southwest.getDouble(LAT),
                            southwest.getDouble(LNG));

                    final JSONObject northeast = viewport.getJSONObject(NORTHEAST);
                    final Address.Location locationNortheast = new Address.Location(
                            northeast.getDouble(LAT),
                            northeast.getDouble(LNG));

                    current.setViewport(new Address.Viewport(locationSouthwest,
                            locationNortheast));
                }
            }

            if (geometry.has(BOUNDS)) {
                final JSONObject viewport = geometry.getJSONObject(BOUNDS);
                if (viewport.has(SOUTHWEST) && viewport.has(NORTHEAST)) {
                    final JSONObject southwest = viewport.getJSONObject(SOUTHWEST);
                    final Address.Location locationSouthwest = new Address.Location(
                            southwest.getDouble(LAT),
                            southwest.getDouble(LNG));

                    final JSONObject northeast = viewport.getJSONObject(NORTHEAST);
                    final Address.Location locationNortheast = new Address.Location(
                            northeast.getDouble(LAT),
                            northeast.getDouble(LNG));

                    current.setBounds(new Address.Bounds(locationSouthwest,
                            locationNortheast));
                }
            }
        }
    }

    private static void parseAddressComponents(@NonNull final JSONObject result,
                                               @NonNull final Address address) throws JSONException {
        if (result.has(ADDRESS_COMPONENTS)) {
            final JSONArray addressComponents = result
                    .getJSONArray(ADDRESS_COMPONENTS);
            for (int a = 0; a < addressComponents.length(); a++) {
                final JSONObject addressComponent = addressComponents.getJSONObject(a);
                if (!addressComponent.has(TYPES)) {
                    continue;
                }
                String value = null;
                if (addressComponent.has(LONG_NAME)) {
                    value = addressComponent.getString(LONG_NAME);
                } else if (addressComponent.has(SHORT_NAME)) {
                    value = addressComponent.getString(SHORT_NAME);
                }
                if (value == null || value.isEmpty()) {
                    continue;
                }
                final JSONArray types = addressComponent.getJSONArray(TYPES);
                for (int t = 0; t < types.length(); t++) {
                    final String type = types.getString(t);
                    switch (type) {
                        case "street_address":
                            address.setStreetAddress(value);
                            break;

                        case "route":
                            address.setRoute(value);
                            break;

                        case "intersection":
                            address.setIntersection(value);
                            break;

                        case "political":
                            address.setPolitical(value);
                            break;

                        case "country":
                            address.setCountry(value);
                            break;

                        case "administrative_area_level_1":
                            address.setAdministrativeAreaLevel1(value);
                            break;

                        case "administrative_area_level_2":
                            address.setAdministrativeAreaLevel2(value);
                            break;

                        case "administrative_area_level_3":
                            address.setAdministrativeAreaLevel3(value);
                            break;

                        case "administrative_area_level_4":
                            address.setAdministrativeAreaLevel4(value);
                            break;

                        case "administrative_area_level_5":
                            address.setAdministrativeAreaLevel5(value);
                            break;

                        case "colloquial_area":
                            address.setColloquialArea(value);
                            break;

                        case "locality":
                            address.setLocality(value);
                            break;

                        case "ward":
                            address.setWard(value);
                            break;

                        case "sublocality":
                            address.setSubLocality(value);
                            break;

                        case "sublocality_level_1":
                            address.setSubLocalityLevel1(value);
                            break;

                        case "sublocality_level_2":
                            address.setSubLocalityLevel2(value);
                            break;

                        case "sublocality_level_3":
                            address.setSubLocalityLevel3(value);
                            break;

                        case "sublocality_level_4":
                            address.setSubLocalityLevel4(value);
                            break;

                        case "sublocality_level_5":
                            address.setSubLocalityLevel5(value);
                            break;

                        case "neighborhood":
                            address.setNeighborhood(value);

                        case "premise":
                            address.setPremise(value);
                            break;

                        case "subpremise":
                            address.setSubPremise(value);
                            break;

                        case "postal_code":
                            address.setPostalCode(value);
                            break;

                        case "natural_feature":
                            address.setNaturalFeature(value);
                            break;

                        case "airport":
                            address.setAirport(value);
                            break;

                        case "park":
                            address.setPark(value);
                            break;

                        case "point_of_interest":
                            address.setPointOfInterest(value);
                            break;

                        case "floor":
                            address.setFloor(value);
                            break;

                        case "establishment":
                            address.setEstablishment(value);
                            break;

                        case "parking":
                            address.setParking(value);
                            break;

                        case "post_box":
                            address.setPostBox(value);
                            break;

                        case "postal_town":
                            address.setPostTown(value);
                            break;

                        case "room":
                            address.setRoom(value);
                            break;

                        case "street_number":
                            address.setStreetNumber(value);
                            break;

                        case "bus_station":
                            address.setBusStation(value);
                            break;

                        case "train_station":
                            address.setTrainStation(value);
                            break;

                        case "transit_station":
                            address.setTransitStation(value);
                            break;

                        default:
                            // Unhandled
                            break;
                    }
                }
            }
        }
    }
}
