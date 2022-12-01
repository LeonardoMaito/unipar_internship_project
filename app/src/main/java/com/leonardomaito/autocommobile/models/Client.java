package com.leonardomaito.autocommobile.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Client implements Parcelable {

    private String name;
    private String address;
    private String telephone;
    private String cpf;


    public Client() {
    }

    private Client(ClientBuilder clientBuilder) {
        this.name = clientBuilder.name;
        this.cpf = clientBuilder.cpf;
        this.address = clientBuilder.address;
        this.telephone = clientBuilder.telephone;

    }

    protected Client(Parcel in) {
        name = in.readString();
        cpf = in.readString();
        address = in.readString();
        telephone = in.readString();

    }

    public static final Creator<Client> CREATOR = new Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel in) {
            return new Client(in);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(cpf);
        parcel.writeString(address);
        parcel.writeString(telephone);

    }

    @Override
    public String toString() {
        return "Cliente:" +
                "Nome= " + name +
                "\n Endereco= " + address +
                "\n Telefone= " + telephone +
                "\n CPF= " + cpf;
    }

    public static class ClientBuilder{

        private final String name;
        private String address;
        private String telephone;
        private final String cpf;

        public ClientBuilder(String name,String cpf){
            this.name = name;
            this.cpf = cpf;
        }

        public ClientBuilder address(String address){
            this.address = address;
            return this;
        }

        public ClientBuilder telephone(String telephone){
            this.telephone = telephone;
            return this;
        }

        public Client build(){
            Client client = new Client(this);
            return client;
        }
    }
}
