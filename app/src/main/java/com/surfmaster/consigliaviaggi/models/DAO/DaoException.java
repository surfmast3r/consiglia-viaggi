package com.surfmaster.consigliaviaggi.models.DAO;

public class DaoException extends Exception {

    public final static int FAIL_TO_INSERT = 1;
    public final static int UPDATE_FAILED = 2;
    public final static int ERROR = 3;
    public final static int FORBIDDEN_ACCESS = 4;
    public static final int NOT_FOUND = 5 ;

    private String errorMessage;
    private int errorCode;

    public DaoException(int errorCode,String message) {
        this.errorCode = errorCode;
        this.errorMessage=message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
