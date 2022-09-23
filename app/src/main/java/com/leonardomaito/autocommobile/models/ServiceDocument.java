package com.leonardomaito.autocommobile.models;

import com.google.firebase.firestore.PropertyName;

import java.util.List;
import java.util.Map;

public class ServiceDocument {

    public ServiceOrder serviceOrder;

    public ServiceDocument() {
    }

    public ServiceDocument(ServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    @PropertyName("serviceOrder")
    public  ServiceOrder  getServiceOrder() {
        return serviceOrder;
    }

    @PropertyName("serviceOrder")
    public void setServiceOrder(ServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }



}

