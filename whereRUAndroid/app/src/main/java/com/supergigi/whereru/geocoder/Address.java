package com.supergigi.whereru.geocoder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tedwei on 3/2/17.
 */

public class Address implements Parcelable {

    private String mFormattedAddress;

    /*
     * Address components
     */

    private String mStreetAddress;

    private String mRoute;

    private String mIntersection;

    private String mPolitical;

    private String mCountry;

    private String mAdministrativeAreaLevel1;

    private String mAdministrativeAreaLevel2;

    private String mAdministrativeAreaLevel3;

    private String mAdministrativeAreaLevel4;

    private String mAdministrativeAreaLevel5;

    private String mColloquialArea;

    private String mLocality;

    private String mWard;

    private String mSubLocality;

    private String mSubLocalityLevel1;

    private String mSubLocalityLevel2;

    private String mSubLocalityLevel3;

    private String mSubLocalityLevel4;

    private String mSubLocalityLevel5;

    private String mNeighborhood;

    private String mPremise;

    private String mSubPremise;

    private String mPostalCode;

    private String mNaturalFeature;

    private String mAirport;

    private String mPark;

    private String mPointOfInterest;

    /*
     * Note: This list is not exhaustive, and is subject to change by Google Geocoding API
     */

    private String mFloor;

    private String mEstablishment;

    private String mParking;

    private String mPostBox;

    private String mPostTown;

    private String mRoom;

    private String mStreetNumber;

    private String mBusStation;

    private String mTrainStation;

    private String mTransitStation;

    /*
     * Geometry
     */

    private Location mLocation;

    private String mLocationType;

    private Viewport mViewport;

    private Bounds mBounds;

    public Address() {

    }

    private Address(final Parcel p) {
        mFormattedAddress = p.readString();

        mStreetAddress = p.readString();
        mRoute = p.readString();
        mIntersection = p.readString();
        mPolitical = p.readString();
        mCountry = p.readString();
        mAdministrativeAreaLevel1 = p.readString();
        mAdministrativeAreaLevel2 = p.readString();
        mAdministrativeAreaLevel3 = p.readString();
        mAdministrativeAreaLevel4 = p.readString();
        mAdministrativeAreaLevel5 = p.readString();
        mColloquialArea = p.readString();
        mLocality = p.readString();
        mWard = p.readString();
        mSubLocality = p.readString();
        mSubLocalityLevel1 = p.readString();
        mSubLocalityLevel2 = p.readString();
        mSubLocalityLevel3 = p.readString();
        mSubLocalityLevel4 = p.readString();
        mSubLocalityLevel5 = p.readString();
        mNeighborhood = p.readString();
        mPremise = p.readString();
        mSubPremise = p.readString();
        mPostalCode = p.readString();
        mNaturalFeature = p.readString();
        mAirport = p.readString();
        mPark = p.readString();
        mPointOfInterest = p.readString();

        mFloor = p.readString();
        mEstablishment = p.readString();
        mParking = p.readString();
        mPostBox = p.readString();
        mPostTown = p.readString();
        mRoom = p.readString();
        mStreetNumber = p.readString();
        mBusStation = p.readString();
        mTrainStation = p.readString();
        mTransitStation = p.readString();

        mLocationType = p.readString();
        mLocation = p.readParcelable(Location.class.getClassLoader());
        mViewport = p.readParcelable(Viewport.class.getClassLoader());
        mBounds = p.readParcelable(Bounds.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel p, int flags) {
        p.writeString(mFormattedAddress);
        p.writeString(mStreetAddress);
        p.writeString(mRoute);
        p.writeString(mIntersection);
        p.writeString(mPolitical);
        p.writeString(mCountry);
        p.writeString(mAdministrativeAreaLevel1);
        p.writeString(mAdministrativeAreaLevel2);
        p.writeString(mAdministrativeAreaLevel3);
        p.writeString(mAdministrativeAreaLevel4);
        p.writeString(mAdministrativeAreaLevel5);
        p.writeString(mColloquialArea);
        p.writeString(mLocality);
        p.writeString(mWard);
        p.writeString(mSubLocality);
        p.writeString(mSubLocalityLevel1);
        p.writeString(mSubLocalityLevel2);
        p.writeString(mSubLocalityLevel3);
        p.writeString(mSubLocalityLevel4);
        p.writeString(mSubLocalityLevel5);
        p.writeString(mNeighborhood);
        p.writeString(mPremise);
        p.writeString(mSubPremise);
        p.writeString(mPostalCode);
        p.writeString(mNaturalFeature);
        p.writeString(mAirport);
        p.writeString(mPark);
        p.writeString(mPointOfInterest);

        p.writeString(mFloor);
        p.writeString(mEstablishment);
        p.writeString(mParking);
        p.writeString(mPostBox);
        p.writeString(mPostTown);
        p.writeString(mRoom);
        p.writeString(mStreetNumber);
        p.writeString(mBusStation);
        p.writeString(mTrainStation);
        p.writeString(mTransitStation);

        p.writeString(mLocationType);
        p.writeParcelable(mLocation, 0);
        p.writeParcelable(mViewport, 0);
        p.writeParcelable(mBounds, 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * A string containing the human-readable address of this location. Often this address is
     * equivalent to the "postal address," which sometimes differs from country to country. (Note
     * that some countries, such as the United Kingdom, do not allow distribution of true postal
     * addresses due to licensing restrictions.) This address is generally composed of one or more
     * address components. For example, the address "111 8th Avenue, New York, NY" contains
     * separate
     * address components for "111" (the street number, "8th Avenue" (the route), "New York" (the
     * city) and "NY" (the US state).
     */
    public String getFormattedAddress() {
        return mFormattedAddress;
    }

    public void setFormattedAddress(final String formattedAddress) {
        mFormattedAddress = formattedAddress;
    }

    /**
     * Precise street address
     *
     * @return precise street address
     */
    public String getStreetAddress() {
        return mStreetAddress;
    }

    public void setStreetAddress(final String streetAddress) {
        mStreetAddress = streetAddress;
    }

    /**
     * Named route (such as "US 101").
     *
     * @return a named route (such as "US 101").
     */
    public String getRoute() {
        return mRoute;
    }

    public void setRoute(final String route) {
        mRoute = route;
    }

    /**
     * Indicates a major intersection, usually of two major roads.
     *
     * @return a major intersection, usually of two major roads.
     */
    public String getIntersection() {
        return mIntersection;
    }

    public void setIntersection(final String intersection) {
        mIntersection = intersection;
    }

    /**
     * A political entity. Usually, this type indicates a polygon of some civil administration.
     *
     * @return a political entity.
     */
    public String getPolitical() {
        return mPolitical;
    }

    public void setPolitical(final String political) {
        mPolitical = political;
    }

    /**
     * The national political entity, and is typically the highest order type returned by the
     * Geocoder.
     *
     * @return The national political entity
     */
    public String getCountry() {
        return mCountry;
    }

    public void setCountry(final String country) {
        mCountry = country;
    }

    /**
     * A first-order civil entity below the country level. Within the United States, these
     * administrative levels are states. Not all nations exhibit these administrative levels.
     *
     * @return A first-order civil entity below the country level.
     */
    public String getAdministrativeAreaLevel1() {
        return mAdministrativeAreaLevel1;
    }

    public void setAdministrativeAreaLevel1(final String administrativeAreaLevel1) {
        mAdministrativeAreaLevel1 = administrativeAreaLevel1;
    }

    /**
     * A second-order civil entity below the country level. Within the United States, these
     * administrative levels are counties. Not all nations exhibit these administrative levels.
     *
     * @return A second-order civil entity below the country level.
     */
    public String getAdministrativeAreaLevel2() {
        return mAdministrativeAreaLevel2;
    }

    public void setAdministrativeAreaLevel2(final String administrativeAreaLevel2) {
        mAdministrativeAreaLevel2 = administrativeAreaLevel2;
    }

    /**
     * A third-order civil entity below the country level. This type indicates a minor civil
     * division. Not all nations exhibit these administrative levels.
     *
     * @return A third-order civil entity below the country level.
     */
    public String getAdministrativeAreaLevel3() {
        return mAdministrativeAreaLevel3;
    }

    public void setAdministrativeAreaLevel3(final String administrativeAreaLevel3) {
        mAdministrativeAreaLevel3 = administrativeAreaLevel3;
    }

    /**
     * A fourth-order civil entity below the country level. This type indicates a minor civil
     * division. Not all nations exhibit these administrative levels.
     *
     * @return A fourth-order civil entity below the country level.
     */
    public String getAdministrativeAreaLevel4() {
        return mAdministrativeAreaLevel4;
    }

    public void setAdministrativeAreaLevel4(final String administrativeAreaLevel4) {
        mAdministrativeAreaLevel4 = administrativeAreaLevel4;
    }

    /**
     * A fifth-order civil entity below the country level. This type indicates a minor civil
     * division. Not all nations exhibit these administrative levels.
     *
     * @return A fifth-order civil entity below the country level.
     */
    public String getAdministrativeAreaLevel5() {
        return mAdministrativeAreaLevel5;
    }

    public void setAdministrativeAreaLevel5(final String administrativeAreaLevel5) {
        mAdministrativeAreaLevel5 = administrativeAreaLevel5;
    }

    /**
     * A commonly-used alternative name for the entity.
     *
     * @return commonly-used alternative name for the entity.
     */
    public String getColloquialArea() {
        return mColloquialArea;
    }

    public void setColloquialArea(final String colloquialArea) {
        mColloquialArea = colloquialArea;
    }

    /**
     * An incorporated city or town political entity.
     *
     * @return incorporated city or town political entity.
     */
    public String getLocality() {
        return mLocality;
    }

    public void setLocality(final String locality) {
        mLocality = locality;
    }

    /**
     * A specific type of Japanese locality, to facilitate distinction between multiple locality
     * components within a Japanese address.
     *
     * @return Ward
     */
    public String getWard() {
        return mWard;
    }

    public void setWard(final String ward) {
        mWard = ward;
    }

    /**
     * A a first-order civil entity below a locality.
     * For some locations may receive one of the additional types: {@link #getSubLocalityLevel1()},
     * {@link #getSubLocalityLevel2()}, {@link #getSubLocalityLevel3()}, {@link
     * #getSubLocalityLevel4()}, {@link #getSubLocalityLevel5()} Each sublocality level is a civil
     * entity. Larger numbers indicate a smaller geographic area.
     *
     * @return a first-order civil entity below a locality.
     */
    public String getSubLocality() {
        return mSubLocality;
    }

    public void setSubLocality(final String subLocality) {
        mSubLocality = subLocality;
    }

    /**
     * @return Sublocality level 1
     * @see #getSubLocality()
     */
    public String getSubLocalityLevel1() {
        return mSubLocalityLevel1;
    }

    public void setSubLocalityLevel1(final String subLocalityLevel1) {
        mSubLocalityLevel1 = subLocalityLevel1;
    }

    /**
     * @return Sublocality level 2
     * @see #getSubLocality()
     */
    public String getSubLocalityLevel2() {
        return mSubLocalityLevel2;
    }

    public void setSubLocalityLevel2(final String subLocalityLevel2) {
        mSubLocalityLevel2 = subLocalityLevel2;
    }

    /**
     * @return Sublocality level 3
     * @see #getSubLocality()
     */
    public String getSubLocalityLevel3() {
        return mSubLocalityLevel3;
    }

    public void setSubLocalityLevel3(final String subLocalityLevel3) {
        mSubLocalityLevel3 = subLocalityLevel3;
    }

    /**
     * @return Sublocality level 4
     * @see #getSubLocality()
     */
    public String getSubLocalityLevel4() {
        return mSubLocalityLevel4;
    }

    public void setSubLocalityLevel4(final String subLocalityLevel4) {
        mSubLocalityLevel4 = subLocalityLevel4;
    }

    /**
     * @return Sublocality level 5
     * @see #getSubLocality()
     */
    public String getSubLocalityLevel5() {
        return mSubLocalityLevel5;
    }

    public void setSubLocalityLevel5(final String subLocalityLevel5) {
        mSubLocalityLevel5 = subLocalityLevel5;
    }

    /**
     * A named neighborhood
     *
     * @return named neighbourhood
     */
    public String getNeighborhood() {
        return mNeighborhood;
    }

    public void setNeighborhood(final String neighborhood) {
        mNeighborhood = neighborhood;
    }

    /**
     * A named location, usually a building or collection of buildings with a common name
     *
     * @return A named location
     */
    public String getPremise() {
        return mPremise;
    }

    public void setPremise(final String premise) {
        mPremise = premise;
    }

    /**
     * A first-order entity below a named location, usually a singular building within a collection
     * of buildings with a common name
     *
     * @return A first-order entity below a named location
     */
    public String getSubPremise() {
        return mSubPremise;
    }

    public void setSubPremise(final String subPremise) {
        mSubPremise = subPremise;
    }

    /**
     * A postal code as used to address postal mail within the country.
     *
     * @return a postal code as used to address postal mail within the country.
     */
    public String getPostalCode() {
        return mPostalCode;
    }

    public void setPostalCode(final String postalCode) {
        mPostalCode = postalCode;
    }

    /**
     * A prominent natural feature.
     *
     * @return a prominent natural feature.
     */
    public String getNaturalFeature() {
        return mNaturalFeature;
    }

    public void setNaturalFeature(final String naturalFeature) {
        mNaturalFeature = naturalFeature;
    }

    /**
     * An airport
     *
     * @return an airport.
     */
    public String getAirport() {
        return mAirport;
    }

    public void setAirport(final String airport) {
        mAirport = airport;
    }

    /**
     * A named park.
     *
     * @return a named park.
     */
    public String getPark() {
        return mPark;
    }

    public void setPark(final String park) {
        mPark = park;
    }

    /**
     * A named point of interest. Typically, these "POI"s are prominent local entities that don't
     * easily fit in another category, such as "Empire State Building" or "Statue of Liberty."
     *
     * @return a named point of interest.
     */
    public String getPointOfInterest() {
        return mPointOfInterest;
    }

    public void setPointOfInterest(final String pointOfInterest) {
        mPointOfInterest = pointOfInterest;
    }

    /**
     * The floor of a building address.
     *
     * @return the floor of a building address.
     */
    public String getFloor() {
        return mFloor;
    }

    public void setFloor(final String floor) {
        mFloor = floor;
    }

    /**
     * A place that has not yet been categorized.
     *
     * @return a place that has not yet been categorized.
     */
    public String getEstablishment() {
        return mEstablishment;
    }

    public void setEstablishment(final String establishment) {
        mEstablishment = establishment;
    }

    /**
     * A parking lot or parking structure.
     *
     * @return a parking lot or parking structure.
     */
    public String getParking() {
        return mParking;
    }

    public void setParking(final String parking) {
        mParking = parking;
    }

    /**
     * A specific postal box.
     *
     * @return a specific postal box.
     */
    public String getPostBox() {
        return mPostBox;
    }

    public void setPostBox(final String postBox) {
        mPostBox = postBox;
    }

    /**
     * A grouping of geographic areas, such as locality and sublocality, used for mailing addresses
     * in some countries.
     *
     * @return Post town
     */
    public String getPostTown() {
        return mPostTown;
    }

    public void setPostTown(final String postTown) {
        mPostTown = postTown;
    }

    /**
     * The room of a building address.
     *
     * @return the room of a building address.
     */
    public String getRoom() {
        return mRoom;
    }

    public void setRoom(final String room) {
        mRoom = room;
    }

    /**
     * The precise street number.
     *
     * @return the precise street number.
     */
    public String getStreetNumber() {
        return mStreetNumber;
    }

    public void setStreetNumber(final String streetNumber) {
        mStreetNumber = streetNumber;
    }

    /**
     * The location of a bus stop
     *
     * @return location of a bus stop
     */
    public String getBusStation() {
        return mBusStation;
    }

    public void setBusStation(final String busStation) {
        mBusStation = busStation;
    }

    /**
     * The location of a train station
     *
     * @return location of a train station
     */
    public String getTrainStation() {
        return mTrainStation;
    }

    public void setTrainStation(final String trainStation) {
        mTrainStation = trainStation;
    }

    /**
     * Location of transition station
     *
     * @return transition station
     */
    public String getTransitStation() {
        return mTransitStation;
    }

    public void setTransitStation(final String transitStation) {
        mTransitStation = transitStation;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(final Location location) {
        mLocation = location;
    }

    /**
     * Contains the recommended viewport for displaying the returned result, specified as two
     * latitude,longitude values defining the southwest and northeast corner of the viewport
     * bounding box. Generally the viewport is used to frame a result when displaying it to a user.
     *
     * @return Viewport
     */
    public Viewport getViewport() {
        return mViewport;
    }

    public void setViewport(final Viewport viewport) {
        mViewport = viewport;
    }

    /**
     * Stores the bounding box which can fully contain the returned result.
     * Note that these bounds may not match the recommended viewport. (For example, San Francisco
     * includes the Farallon islands, which are technically part of the city, but probably should
     * not be returned in the viewport.) Optionally returned.
     *
     * @return Bounds
     */
    public Bounds getBounds() {
        return mBounds;
    }

    public void setBounds(final Bounds bounds) {
        mBounds = bounds;
    }

    /**
     * Additional data about the specified location. The following values are currently supported:
     *
     * "ROOFTOP" indicates that the returned result is a precise geocode for which we have location
     * information accurate down to street address precision.
     *
     * "RANGE_INTERPOLATED" indicates that the returned result reflects an approximation (usually
     * on
     * a road) interpolated between two precise points (such as intersections). Interpolated
     * results
     * are generally returned when rooftop geocodes are unavailable for a street address.
     *
     * "GEOMETRIC_CENTER" indicates that the returned result is the geometric center of a result
     * such as a polyline (for example, a street) or polygon (region).
     *
     * "APPROXIMATE" indicates that the returned result is approximate.
     *
     * @return The location type
     */
    public String getLocationType() {
        return mLocationType;
    }

    public void setLocationType(final String locationType) {
        mLocationType = locationType;
    }

    @Override
    public String toString() {
        return "Address{" +
                "mFormattedAddress='" + mFormattedAddress + '\'' +
                ", mStreetAddress='" + mStreetAddress + '\'' +
                ", mRoute='" + mRoute + '\'' +
                ", mIntersection='" + mIntersection + '\'' +
                ", mPolitical='" + mPolitical + '\'' +
                ", mCountry='" + mCountry + '\'' +
                ", mAdministrativeAreaLevel1='" + mAdministrativeAreaLevel1 + '\'' +
                ", mAdministrativeAreaLevel2='" + mAdministrativeAreaLevel2 + '\'' +
                ", mAdministrativeAreaLevel3='" + mAdministrativeAreaLevel3 + '\'' +
                ", mAdministrativeAreaLevel4='" + mAdministrativeAreaLevel4 + '\'' +
                ", mAdministrativeAreaLevel5='" + mAdministrativeAreaLevel5 + '\'' +
                ", mColloquialArea='" + mColloquialArea + '\'' +
                ", mLocality='" + mLocality + '\'' +
                ", mWard='" + mWard + '\'' +
                ", mSubLocality='" + mSubLocality + '\'' +
                ", mSubLocalityLevel1='" + mSubLocalityLevel1 + '\'' +
                ", mSubLocalityLevel2='" + mSubLocalityLevel2 + '\'' +
                ", mSubLocalityLevel3='" + mSubLocalityLevel3 + '\'' +
                ", mSubLocalityLevel4='" + mSubLocalityLevel4 + '\'' +
                ", mSubLocalityLevel5='" + mSubLocalityLevel5 + '\'' +
                ", mNeighborhood='" + mNeighborhood + '\'' +
                ", mPremise='" + mPremise + '\'' +
                ", mSubPremise='" + mSubPremise + '\'' +
                ", mPostalCode='" + mPostalCode + '\'' +
                ", mNaturalFeature='" + mNaturalFeature + '\'' +
                ", mAirport='" + mAirport + '\'' +
                ", mPark='" + mPark + '\'' +
                ", mPointOfInterest='" + mPointOfInterest + '\'' +
                ", mFloor='" + mFloor + '\'' +
                ", mEstablishment='" + mEstablishment + '\'' +
                ", mParking='" + mParking + '\'' +
                ", mPostBox='" + mPostBox + '\'' +
                ", mPostTown='" + mPostTown + '\'' +
                ", mRoom='" + mRoom + '\'' +
                ", mStreetNumber='" + mStreetNumber + '\'' +
                ", mBusStation='" + mBusStation + '\'' +
                ", mTrainStation='" + mTrainStation + '\'' +
                ", mTransitStation='" + mTransitStation + '\'' +
                ", mLocation=" + mLocation +
                ", mLocationType='" + mLocationType + '\'' +
                ", mViewport=" + mViewport +
                ", mBounds=" + mBounds +
                '}';
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {

        @Override
        public Address createFromParcel(final Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(final int size) {
            return new Address[size];
        }
    };

    public static final class Location implements Parcelable {

        public final double latitude;

        public final double longitude;

        Location(final double latitude, final double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        Location(final Parcel p) {
            latitude = p.readDouble();
            longitude = p.readDouble();
        }

        @Override
        public void writeToParcel(final Parcel p, final int flags) {
            p.writeDouble(latitude);
            p.writeDouble(longitude);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public String toString() {
            return "Location{" +
                    "latitude=" + latitude +
                    ", longitude=" + longitude +
                    '}';
        }

        public static final Creator<Location> CREATOR = new Creator<Location>() {

            @Override
            public Location createFromParcel(final Parcel source) {
                return new Location(source);
            }

            @Override
            public Location[] newArray(final int size) {
                return new Location[size];
            }
        };
    }

    public static final class Viewport implements Parcelable {

        public final Location southwest;

        public final Location northeast;

        Viewport(final Location southwest, final Location northeast) {
            this.southwest = southwest;
            this.northeast = northeast;
        }

        Viewport(final Parcel p) {
            southwest = p.readParcelable(Location.class.getClassLoader());
            northeast = p.readParcelable(Location.class.getClassLoader());
        }

        @Override
        public void writeToParcel(final Parcel p, final int flags) {
            p.writeParcelable(southwest, 0);
            p.writeParcelable(northeast, 0);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public String toString() {
            return "Viewport{" +
                    "southwest=" + southwest +
                    ", northeast=" + northeast +
                    '}';
        }

        public static final Creator<Viewport> CREATOR = new Creator<Viewport>() {

            @Override
            public Viewport createFromParcel(final Parcel source) {
                return new Viewport(source);
            }

            @Override
            public Viewport[] newArray(final int size) {
                return new Viewport[size];
            }
        };
    }

    public static final class Bounds implements Parcelable {

        public final Location southwest;

        public final Location northeast;

        Bounds(final Location southwest, final Location northeast) {
            this.southwest = southwest;
            this.northeast = northeast;
        }

        Bounds(final Parcel p) {
            southwest = p.readParcelable(Location.class.getClassLoader());
            northeast = p.readParcelable(Location.class.getClassLoader());
        }

        @Override
        public void writeToParcel(final Parcel p, final int flags) {
            p.writeParcelable(southwest, 0);
            p.writeParcelable(northeast, 0);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public String toString() {
            return "Bounds{" +
                    "southwest=" + southwest +
                    ", northeast=" + northeast +
                    '}';
        }

        public static final Creator<Bounds> CREATOR = new Creator<Bounds>() {

            @Override
            public Bounds createFromParcel(final Parcel source) {
                return new Bounds(source);
            }

            @Override
            public Bounds[] newArray(final int size) {
                return new Bounds[size];
            }
        };
    }
}
