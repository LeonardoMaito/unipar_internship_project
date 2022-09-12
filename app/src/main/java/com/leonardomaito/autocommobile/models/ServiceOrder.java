package com.leonardomaito.autocommobile.models;

public class ServiceOrder {

    public Client client;
    public Vehicle vehicle;
    private String service;
    private String observation;
    private String paymentForm;
    private String date;
    private double totalValue;
    private int id;

    public ServiceOrder() {
    }

    private ServiceOrder(ServiceOrderBuilder serviceOrderBuilder){
        this.client = serviceOrderBuilder.client;
        this.vehicle = serviceOrderBuilder.vehicle;
        this.service = serviceOrderBuilder.service;
        this.paymentForm = serviceOrderBuilder.paymentForm;
        this.observation = serviceOrderBuilder.observation;
        this.totalValue = serviceOrderBuilder.value;
        this.date = serviceOrderBuilder.date;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

   public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getPaymentForm() {
        return paymentForm;
    }

    public void setPaymentForm(String paymentForm) {
        this.paymentForm = paymentForm;
    }

    public static class ServiceOrderBuilder {

        private Vehicle vehicle;
        private Client client;
        private final String service;
        private final String paymentForm;
        private final int id;
        private final double value;
        private final String date;
        private String observation;

        public ServiceOrderBuilder(Client client, Vehicle vehicle,
                                   String service, String paymentForm,
                                   int id, double value, String date) {
            this.client = client;
            this.vehicle = vehicle;
            this.service = service;
            this.paymentForm = paymentForm;
            this.id = id;
            this.value = value;
            this.date = date;
        }


        public ServiceOrder.ServiceOrderBuilder observation(String observation) {
            this.observation = observation;
            return this;
        }

        public ServiceOrder build() {
            ServiceOrder serviceOrder = new ServiceOrder(this);
            return serviceOrder;

        }
    }
}
