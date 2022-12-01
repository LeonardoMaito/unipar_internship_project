package com.leonardomaito.autocommobile.models;

import com.google.firebase.firestore.PropertyName;

public class ClientDocument {

    public Client client;

    public ClientDocument(Client client) {
        this.client = client;
    }

    public ClientDocument() {
    }

    @PropertyName("client")
    public Client getClient() {
        return client;
    }

    @PropertyName("client")
    public void setClient(Client client) {
        this.client = client;
    }
}
