package br.com.bsfraga.fwBaseWebApi.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * This class contains the methods that make the
 * encode and decode process of a String.
 *
 * @author bsfraga
 */
public class URLEncoding {

    /**
     * This method recieves as parameter a URL String which will
     * be encoded to URL UTF-8 pattern.
     * @param value String
     * @return encoded URL String
     */
    public static String encodeString(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException exception) {
            throw new RuntimeException(exception.getCause());
        }
    }

    /**
     * This method recieves as parameter a URL String which will be decoded to String UTF-8 pattern.
     * @param value UTF-8 URL Encoded String
     * @return decoded URL String or String
     */
    public static String decodeString (String value){
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        }catch (UnsupportedEncodingException exception){
            throw new RuntimeException(exception.getCause());
        }
    }

}
