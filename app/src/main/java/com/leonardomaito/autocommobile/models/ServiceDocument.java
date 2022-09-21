package com.leonardomaito.autocommobile.models;



import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class ServiceDocument {

    public List<ServiceOrder> serviceOrderArray;

    public ServiceDocument() {
    }


    public ServiceDocument(List<ServiceOrder> serviceOrderArray) {
        this.serviceOrderArray = serviceOrderArray;
    }

    @PropertyName("serviceOrder")
    public List<ServiceOrder> getServiceOrderArray() {
        return serviceOrderArray;
    }

    @PropertyName("serviceOrder")
    public void setServiceOrderArray(List<ServiceOrder> serviceOrderArray) {
        this.serviceOrderArray = serviceOrderArray;
    }

}

