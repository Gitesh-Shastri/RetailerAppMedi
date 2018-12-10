package com.medicento.retailerappmedi.data;

public class Response {
    private String name;
    private int order,delivery,bounce,payment,returns,behaviour;

    public Response(String name, int order, int delivery, int bounce, int payment, int returns, int behaviour) {
        this.name = name;
        this.order = order;
        this.delivery = delivery;
        this.bounce = bounce;
        this.payment = payment;
        this.returns = returns;
        this.behaviour = behaviour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public int getBounce() {
        return bounce;
    }

    public void setBounce(int bounce) {
        this.bounce = bounce;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getReturns() {
        return returns;
    }

    public void setReturns(int returns) {
        this.returns = returns;
    }

    public int getBehaviour() {
        return behaviour;
    }

    public void setBehaviour(int behaviour) {
        this.behaviour = behaviour;
    }
}
