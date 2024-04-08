package com.example.greengrove.Model;

import com.google.gson.annotations.SerializedName;

public class Cart {
    @SerializedName("id_user")
    private Users user;
    @SerializedName("id_fruit")
    private Fruit fruit;
    private int quantity;

    public Cart(Users users,Fruit fruit, int quantity) {
        this.user = users;
        this.fruit = fruit;
        this.quantity = quantity;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Fruit getFruit() {
        return fruit;
    }

    public void setFruit(Fruit fruit) {
        this.fruit = fruit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "fruit=" + fruit +
                ", quantity=" + quantity +
                '}';
    }
}
