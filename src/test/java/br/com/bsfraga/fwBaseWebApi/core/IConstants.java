package br.com.bsfraga.fwBaseWebApi.core;

import io.restassured.http.ContentType;

/**
 * This Interface is where you define the basic informations about the domain such as
 * URL, API BASE PATH, PORT and more.
 * @author bsfraga
 *
 */
public interface IConstants {

	/**
	 * This attribute sets the URL base from the API.
	 * For example: https://swapi.co/
	 */
	public static String API_BASE_URL = "";
	
	/**
	 * This attribute sets the port related to the request type.
	 * For example: port 443 for HTTPS or port 80 for HTTP.
	 */
	public static Integer API_PORT = 443; //
	
	/**
	 * This attribute sets the api base path.
	 * <pre>
	 * For example:
	 * 
	 * {{URL_BASE}}/api/
	 * {{URL_BASE}}/api/v1
	 * {{URL_BASE}}/v1/
	 * {{URL_BASE}}/people/
	 * {{URL_BASE}}/address/
	 *</pre>
	 */
	public static String API_BASE_PATH = ""; 
	
	/**
	 * This attribute sets the content type from the requests.
	 */
	public static ContentType API_CONTENT_TYPE = ContentType.JSON;
	
	/**
	 * This attribute sets the maximum time out for each request.
	 */
	public static Long MAX_TIME_OUT = 5000L;
	
}