package util;

import java.nio.ByteBuffer;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Parser {
	public static String encode(JSONObject json) {
		String ret = JSONValue.toJSONString(json);
		ret += '\n';
		return ret;
	}

	/*
	 * decode function converts String to JSONObject. assuming str is the message
	 * received from server and was built with Parser.code()
	 */
	public static JSONObject decode(Object str) {
		if (!(str instanceof String)) {
			// TODO maybe throw exception instead of returning null
			return null;
		}

		String msg = (String) str;

		StringBuilder string = new StringBuilder(msg);
		string.deleteCharAt(msg.length() - 1);
		JSONObject jsonObj = (JSONObject) JSONValue.parse(string.toString());
		return jsonObj;
	}

	// Note: Java doesn't support unsigned values, so for each unsigned format
	// is passed the next big signed primitive instead.
	public static byte[] getByteArrayByFormatType(String formatName, String data) throws ClassNotFoundException {

		switch (formatName) {
		case "Double precision 64-bit":
            return ByteBuffer.allocate(8).putDouble(Double.parseDouble(data)).array();
		case "Floating point 32-bit":
            return ByteBuffer.allocate(4).putFloat(Float.parseFloat(data)).array();
		case "Unsigned Int 32-bit":
			// Returning 64-bit signed value instead of unsigned 32-bit 
            return ByteBuffer.allocate(8).putLong(Long.parseLong(data)).array();
		case "Unsigned Int 16-bit":
			// Returning 32-bit signed value instead of unsigned 16-bit 
            return ByteBuffer.allocate(4).putInt(Integer.parseInt(data)).array();
		case "Unsigned Int 8-bit":
			// Returning 16-bit signed value instead of unsigned 8-bit 
            return ByteBuffer.allocate(2).putShort(Short.parseShort(data)).array();
		case "Signed Int 8-bit":
            return new byte[]{Byte.parseByte(data)};
		case "ASCII string":
            return data.getBytes();
		default:
			throw new ClassNotFoundException(
					"This format type " + formatName + " is not recognized as a Capture API format.");
		}
	}
	
	// Convert a hexadecimal string to a byte value
	public static byte hexStringToByte(String hexString) {
		// Check if the string starts with "0x" and remove it
		if (hexString.startsWith("0x")) {
			hexString = hexString.substring(2);
		}

		// Parse the hexadecimal string to a byte
		return (byte) Integer.parseInt(hexString, 16);
	}

	// Method to split a hexadecimal string into high and low parts
	public static String[] splitHex(String originalHex) {
		// Remove "0x" prefix
		String hexWithoutPrefix = originalHex.substring(2);

		// Determine the length of the original hexadecimal number
		int length = hexWithoutPrefix.length();

		// Calculate the index to split the string
		int splitIndex = length / 2;

		// Separate high and low parts
		String highPart = hexWithoutPrefix.substring(0, splitIndex);
		String lowPart = hexWithoutPrefix.substring(splitIndex);

		// Return the results as an array
		return new String[] { highPart, lowPart };
	}
}
