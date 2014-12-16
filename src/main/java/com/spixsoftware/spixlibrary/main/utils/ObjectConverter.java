package com.spixsoftware.spixlibrary.main.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

public class ObjectConverter {

	public static final boolean byteToBoolean(byte value) {
		return value != 0;
	}

	public static final byte booleanToByte(boolean value) {
		return (byte) (value ? 1 : 0);
	}

	public static boolean longToBoolean(long value) {
		if (value > 0) {
			return true;
		}
		return false;
	}

	public static boolean intToBoolean(int value) {
		if (value > 0) {
			return true;
		}
		return false;
	}

	public static long booleanToLong(boolean value) {
		if (value) {
			return 1;
		}
		return 0;
	}

	public static int booleanToInt(boolean value) {
		if (value) {
			return 1;
		}
		return 0;
	}

	public static String booleanToString(boolean value) {
		if (value) {
			return "1";
		} else {
			return "0";
		}
	}

	public static long nanosecondsToMiliseconds(long nanosecondsToConvert) {
		return nanosecondsToConvert / 1000000;
	}

	public static String inputStreamToString(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	public static BigDecimal stringToBigDecimal(String value) {
		if (value == null || value.isEmpty()) {
			return null;
		} else {
			return new BigDecimal(value);
		}
	}

	public static boolean stringToBoolean(String value) {
		if (value == null || value.isEmpty()) {
			return false;
		}
		if (value.equals("1")) {
			return true;
		}
		return false;
	}

	public static String intColorToHex(int intColor) {
		return String.format("#%06X", 0xFFFFFF & intColor);
	}

    public static double metersToKilometers(double metters){
        return metters/1000;
    }
}
