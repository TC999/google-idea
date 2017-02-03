package edu.oregonstate.actions;

/**
 * @author Nicholas Nelson <nelsonni@oregonstate.edu> Created on on 5/25/16.
 */
public class PermissionGuardException extends Exception {

    public PermissionGuardException(String message) {
        super(message);
    }

    public PermissionGuardException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionGuardException(Throwable cause) {
        super(cause);
    }

    public PermissionGuardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}