package com.dailycodebuffer.system_design.GeoHash;

public class GeoHashDecoder {
    private static final String BASE32 = "0123456789bcdefghjkmnpqrstuvwxyz";

    public static double[] decode(String geohash) {
        boolean isEven = true;
        double[] latRange = {-90.0, 90.0};
        double[] lonRange = {-180.0, 180.0};

        for (char c : geohash.toCharArray()) {
            int val = BASE32.indexOf(c);
            for (int i = 4; i >= 0; i--) {
                int bit = (val >> i) & 1;
                if (isEven) {
                    refineRange(lonRange, bit);
                } else {
                    refineRange(latRange, bit);
                }
                isEven = !isEven;
            }
        }
        return new double[]{(latRange[0] + latRange[1]) / 2, (lonRange[0] + lonRange[1]) / 2};
    }

    private static void refineRange(double[] range, int bit) {
        double mid = (range[0] + range[1]) / 2;
        if (bit == 1) {
            range[0] = mid;
        } else {
            range[1] = mid;
        }
    }

    public static void main(String[] args) {
        String geohash = "tdr1v9q";
        double[] coordinates = decode(geohash);
        System.out.println("Decoded Latitude: " + coordinates[0]);
        System.out.println("Decoded Longitude: " + coordinates[1]);
    }
}
