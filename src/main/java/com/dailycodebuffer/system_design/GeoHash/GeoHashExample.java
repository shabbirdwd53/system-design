package com.dailycodebuffer.system_design.GeoHash;

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;

public class GeoHashExample {

    public static void main(String[] args) {
        double latitude = 12.971599;   // Bengaluru
        double longitude = 77.594566;

        // Encode GeoHash
        String geohash = GeoHash.withCharacterPrecision(latitude, longitude, 8).toBase32();
        System.out.println("GeoHash: " + geohash);

        // Decode GeoHash
        GeoHash decodedHash = GeoHash.fromGeohashString(geohash);
        WGS84Point point = decodedHash.getOriginatingPoint();
        System.out.println("Decoded Latitude: " + point.getLatitude());
        System.out.println("Decoded Longitude: " + point.getLongitude());
    }
}

