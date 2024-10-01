package com.poten.dive_in.common.exception;

public class S3UploadException extends RuntimeException{
    public S3UploadException(String message, Throwable cause){
        super(message, cause);
    }
}
