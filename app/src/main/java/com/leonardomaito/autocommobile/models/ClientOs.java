package com.leonardomaito.autocommobile.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ClientOs implements Parcelable {

    private String name;
    private String address;
    private String telephone;
    private String cpf;
    private String id;

    public ClientOs() {
    }

    public ClientOs(String name, String address, String telephone, String cpf, String id) {
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.cpf = cpf;
        this.id = id;
    }

    protected ClientOs(Parcel in) {
        name = in.readString();
        address = in.readString();
        telephone = in.readString();
        cpf = in.readString();
        id = in.readString();
    }

    public static final Creator<ClientOs> CREATOR = new Creator<ClientOs>() {
        @Override
        public ClientOs createFromParcel(Parcel in) {
            return new ClientOs(in);
        }

        @Override
        public ClientOs[] newArray(int size) {
            return new ClientOs[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getCpf() {
        return cpf;
    }

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(telephone);
        parcel.writeString(cpf);
        parcel.writeString(id);
    }
}
