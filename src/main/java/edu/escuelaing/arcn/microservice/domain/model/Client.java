package edu.escuelaing.arcn.microservice.domain.model;


public class Client {
    private String id;
    private String name;
    private String email;
    private String address;
    private PaymentMethod paymentDetails;

    public Client(String name, String email, String address, PaymentMethod paymentDetails){
        this.name=name;
        this.email=email;
        this.address=address;
        this.paymentDetails=paymentDetails;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PaymentMethod getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentMethod paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", paymentDetails='" + paymentDetails + '\'' +
                '}';
    }
}
