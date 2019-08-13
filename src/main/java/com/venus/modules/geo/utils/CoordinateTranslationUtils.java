package com.venus.modules.geo.utils;

import java.lang.reflect.Array;

public class CoordinateTranslationUtils {
    private final static double X_PI = 3.14159265358979324 * 3000.0 / 180.0;
    private final static double PI = 3.1415926535897932384626;
    private final static double A = 6378245.0;
    private final static double EE = 0.00669342162296594323;

    /**
     * BD09转化为GCJ02
     *
     * @param lngLat: 数组
     * @return 返回数组
     */
    public static double[] bd09ToGCJ02(double[] lngLat) {
        double[] result = new double[2];
        double x = lngLat[0] - 0.0065;
        double y = lngLat[1] - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
        double gcj02Lng = z * Math.cos(theta);
        double gcj02Lat = z * Math.sin(theta);
        result[0] = gcj02Lng;
        result[1] = gcj02Lat;
        return result;
    }

    /**
     * GCJ02转化为BD09
     * @param lngLat: 数组
     * @return 返回数组
     */
    public static double[] gcj02ToBD09(double[] lngLat) {
        double[] result = new double[2];
        double z = Math.sqrt(lngLat[0] * lngLat[0] + lngLat[1] * lngLat[1]) + 0.00002 * Math.sin(lngLat[1] * X_PI);
        double theta = Math.atan2(lngLat[1], lngLat[0]) + 0.000003 * Math.cos(lngLat[0] * X_PI);
        double bdLng = z * Math.cos(theta) + 0.0065;
        double bdLat = z * Math.sin(theta) + 0.006;
        result[0] = bdLng;
        result[1] = bdLat;
        return result;
    }

}
