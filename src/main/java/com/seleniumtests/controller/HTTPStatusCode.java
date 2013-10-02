package com.seleniumtests.controller;

/**
 * This enumeration will capture all the HTTP error status codes 4XX and 5XX For
 *
 */
public enum HTTPStatusCode {

	// //////////////////////4xx (Request
	// error)///////////////////////////////////////////////////////
	// These status codes indicate that there was likely an error in the request
	// which prevented the server from being able to process it.
	BAD_REQUEST(400, "Bad request",
			"The server didn't understand the syntax of the request."),
	// NOT_AUTHORIZED(401,"Not authorized",
	// "The request requires authentication. The server might return this response for a page behind a login."),
	// FORBIDDEN(403,
	// "Directory listing denied","The server is refusing the request."),
	PAGE_NOT_FOUND(404, "Page not found",
			"The server can't find the requested page."), PAGE_MOVED_OR_NOT_AVAILABLE(
			404, "This page may have moved or is no longer available",
			"The server can't find the requested page."), PROBLEM_LOADING_PAGE(
			404, "HTTP ERROR 404", "HTTP ERROR 404"),
	// METHOD_NOT_ALLOWED(405,"Method not allowed",
	// "The method specified in the request is not allowed."),
	// NOT_ACCEPTABLE(406, "Not acceptable",
	// "The requested page can't respond with the content characteristics requested."),
	// PROXY_AUTHENTICATION_REQUIRED(407, "Proxy authentication required",
	// "Tthe requestor has to authenticate using a proxy."),
	REQUEST_TIMEOUT(408, "Request timeout",
			"The server timed out waiting for the request."),
	// CONFLICT(409, "Conflict",
	// "The server encountered a conflict fulfilling the request."),
	// GONE(410, "Gone",
	// "The server returns this response when the requested resource has been permanently removed. It is similar to a 404 Not found."),
	// LENGTH_REQUIRED(411, "Length required",
	// "The server won't accept the request without a valid Content-Length header field."),
	// PRECONDITION_FAILED(412, "Precondition failed",
	// "The server doesn't meet one of the preconditions that the requestor put on the request."),
	// REQUEST_ENTITY_TOO_LARGE(413, "Request entity too large",
	// "The server can't process the request because it is too large for the server to handle."),
	// REQUEST_URI_IS_TOO_LONG(414, "Requested URI is too long",
	// "The requested URI typically, a URL is too long for the server to process."),
	// UNSUPPORTED_MEDIA_TYPE(415, "Unsupported media type",
	// "The request is in a format not support by the requested page."),
	// REQUESTED_RANGE_NOT_SUPPORTED(416, "Requested range not satisfiable",
	// "The server returns this status code if the request is for a range not available for the page."),
	// EXPECTATION_FAILED(417,
	// "Expectation failed","The server can't meet the requirements of the Expect request-header field."),

	// //////////////////////5xx (Server error)
	// ////////////////////////////////////////////////////////////
	// These status codes indicate that the server had an internal error when
	// trying to process the request.
	// These errors tend to be with the server itself, not with the request.
	INTERNAL_SERVER_ERROR(500, "Internal server error",
			"The server encountered an error and can't fulfill the request."),
	// NOT_IMPLEMENTED(501, "Not implemented",
	// "The server doesn't have the functionality to fulfill the request. For instance, the server might return this code when it doesn't recognize the request method."),
	BAD_GATEWAY(
			502,
			"Bad gateway",
			"The server was acting as a gateway or proxy and received an invalid response from the upstream server."), SERVICE_UNAVAILABLE(
			503,
			"Service unavailable",
			"The server is currently unavailable because it is overloaded or down for maintenance."), FEATURE_UNAVAILABLE(
			503,
			"Feature unavailable",
			"The server is currently unavailable because it is overloaded or down for maintenance."), GATEWAY_TIMEOUT(
			504,
			"Gateway timeout",
			"The server was acting as a gateway or proxy and didn't receive a timely request from the upstream server."), HTTP_VERSION_NOT_SUPPORTED(
			505, "HTTP version not supported",
			"The server doesn't support the HTTP protocol version used in the request."), PAGE_NOT_RESPONDING(
			500, "QA PNR", "Page not responding"), TECHNICAL_ERROR(500,
			"Technical Error", "Technical Error");

	public static HTTPStatusCode getByCode(int code) {
		for (HTTPStatusCode status : values()) {
			if (status.getCode() == code)
				return status;
		}
		throw new RuntimeException("HTTPStatusCode: " + code
				+ " not supported.");
	}

	// //////////////////////member variables, getters, setters and constructors
	// //////////////////////////
	private int code;
	private String title;

	private String description;

	private HTTPStatusCode(int code, String title, String description) {
		this.code = code;
		this.title = title;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public String getTitle() {
		return title;
	}

	public String toString() {
		return name() + " (" + getCode() + ") - " + getTitle() + " {"
				+ getDescription() + "}";
	}
}
