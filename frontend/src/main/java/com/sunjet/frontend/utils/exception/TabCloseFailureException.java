package com.sunjet.frontend.utils.exception;

@SuppressWarnings("serial")
public class TabCloseFailureException extends RuntimeException {
    public TabCloseFailureException(String errorMsg) {
        super(errorMsg);
    }
}