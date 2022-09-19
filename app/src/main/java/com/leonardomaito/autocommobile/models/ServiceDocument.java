package com.leonardomaito.autocommobile.models;

import android.app.Service;

import com.google.firebase.firestore.PropertyName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceDocument {

    public List<ServiceOrder> serviceOrderArray;

    //public ServiceOrder serviceOrder;

    public ServiceDocument() {
    }

   /* @PropertyName("serviceOrder")
    public ServiceOrder getServiceOrder() {
        return serviceOrder;
    }*/

    @PropertyName("serviceOrder")
    public List<ServiceOrder> serviceOrderArray() {
        return serviceOrderArray;
    }
}
