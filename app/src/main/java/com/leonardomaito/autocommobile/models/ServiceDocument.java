package com.leonardomaito.autocommobile.models;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class ServiceDocument {

    public List<ServiceOrder> serviceOrder;

    public ServiceDocument() {
    }

    public ServiceDocument(List<ServiceOrder> serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    @PropertyName("serviceOrder")
    public List<ServiceOrder> getServiceOrder() {
        return serviceOrder;
    }

    @PropertyName("serviceOrder")
    public void setServiceOrder(List<ServiceOrder> serviceOrder) {
        this.serviceOrder = serviceOrder;
    }



}

