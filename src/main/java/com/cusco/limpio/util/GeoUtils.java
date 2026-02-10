package com.cusco.limpio.util;

public final class GeoUtils {

	private static final double EARTH_RADIUS_METERS = 6_371_000d;

	private GeoUtils() {}

	public static double haversineDistanceMeters(double lat1, double lon1, double lat2, double lon2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double rLat1 = Math.toRadians(lat1);
		double rLat2 = Math.toRadians(lat2);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(rLat1) * Math.cos(rLat2)
				* Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return EARTH_RADIUS_METERS * c;
	}

	public static boolean isValidLatitude(double lat) {
		return !Double.isNaN(lat) && lat >= -90.0 && lat <= 90.0;
	}

	public static boolean isValidLongitude(double lon) {
		return !Double.isNaN(lon) && lon >= -180.0 && lon <= 180.0;
	}

	public static boolean withinMeters(double lat1, double lon1, double lat2, double lon2, double meters) {
		return haversineDistanceMeters(lat1, lon1, lat2, lon2) <= meters;
	}
}

