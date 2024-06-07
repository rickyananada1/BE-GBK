package com.dev.gbk.exception;

import java.util.Map;

public class NotAuthorizedException extends RuntimeException{
    public NotAuthorizedException(){
        super("401 : UNAUTHORIZED");
    }
}
