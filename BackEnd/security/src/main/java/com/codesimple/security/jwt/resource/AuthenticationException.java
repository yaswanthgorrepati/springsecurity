package com.codesimple.security.jwt.resource;
public class AuthenticationException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2754435247936519318L;

	public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}

