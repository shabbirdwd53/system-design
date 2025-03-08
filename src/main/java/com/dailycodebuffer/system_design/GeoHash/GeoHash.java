package com.dailycodebuffer.system_design.GeoHash;

import java.util.*;

public class GeoHash {
    private static final String BASE32 = "0123456789bcdefghjkmnpqrstuvwxyz";
    private static final int[] BITS = {16, 8, 4, 2, 1};

    public static String encode(double latitude, double longitude, int precision) {
        boolean isEven = true;
        int bit = 0, ch = 0;
        StringBuilder geohash = new StringBuilder();

        double[] latRange = {-90.0, 90.0};
        double[] lonRange = {-180.0, 180.0};

        while (geohash.length() < precision) {
            double mid;
            if (isEven) {
                mid = (lonRange[0] + lonRange[1]) / 2;
                if (longitude >= mid) {
                    ch |= BITS[bit];
                    lonRange[0] = mid;
                } else {
                    lonRange[1] = mid;
                }
            } else {
                mid = (latRange[0] + latRange[1]) / 2;
                if (latitude >= mid) {
                    ch |= BITS[bit];
                    latRange[0] = mid;
                } else {
                    latRange[1] = mid;
                }
            }
            isEven = !isEven;
            if (bit < 4) {
                bit++;
            } else {
                geohash.append(BASE32.charAt(ch));
                bit = 0;
                ch = 0;
            }
        }
        return geohash.toString();
    }

    public static void main(String[] args) {
        double latitude = 12.971599;
        double longitude = 77.594566;
        int precision = 7;

        String geohash = encode(latitude, longitude, precision);
        System.out.println("GeoHash for Bengaluru: " + geohash);
    }
}
