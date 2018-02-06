package com.example.marta.rpicamera.service;

import com.example.marta.rpicamera.Data.Results;

/**
 * Created by Marta on 2018-01-30.
 */

public interface RPiServiceCallback {
    void serviceSuccess(Results results);
    void serviceFailure(Exception exception);
}
