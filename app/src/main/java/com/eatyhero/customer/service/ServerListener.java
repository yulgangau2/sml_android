package com.eatyhero.customer.service;

/**
 * Created by venky on 13-08-2015.
 */
public interface ServerListener {

    public void onSuccess(Object result, RequestID requestID);

    public void onFailure(String error, RequestID requestID);

}
