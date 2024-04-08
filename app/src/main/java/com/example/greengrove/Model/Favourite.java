package com.example.greengrove.Model;

import com.google.gson.annotations.SerializedName;

public class Favourite {
    @SerializedName("_id")
    private String id;
    @SerializedName("id_user")
    private String users;
    @SerializedName("id_fruit")
    private Fruit fruit;
    private String createdAt,updatedAt;

    public Favourite() {
    }

    public Favourite(String users, Fruit fruit) {
        this.users = users;
        this.fruit = fruit;
    }

    public Favourite(String id, String users, Fruit fruit, String createdAt, String updatedAt) {
        this.id = id;
        this.users = users;
        this.fruit = fruit;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public Fruit getFruit() {
        return fruit;
    }

    public void setFruit(Fruit fruit) {
        this.fruit = fruit;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


    @Override
    public String toString() {
        return "Favourite{" +
                "id='" + id + '\'' +
                ", users='" + users + '\'' +
                ", fruit=" + fruit +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
