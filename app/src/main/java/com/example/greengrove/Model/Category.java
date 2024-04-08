package com.example.greengrove.Model;

import com.example.greengrove.R;

import java.util.List;

public class Category {
    private String nameCategory;
    private List<Fruit> fruits;

    public Category(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public Category(String nameCategory, List<Fruit> fruits) {
        this.nameCategory = nameCategory;
        this.fruits = fruits;
    }
    public String getNameCategory() {
        return nameCategory;
    }
    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public List<Fruit> getFruits() {
        return fruits;
    }

    public void setFruits(List<Fruit> fruits) {
        this.fruits = fruits;
    }
    public int getIconResourceId(String categoryName) {
        // Ánh xạ tên loại sản phẩm sang ID tài nguyên của icon tương ứng
        switch (categoryName) {
            case "Fruits":
                return R.drawable.fruit_icon;
            case "Vegetables":
                return R.drawable.vegetable_icon;
            case "Meats":
                return R.drawable.meat_icon;
            case "Dairys":
                return R.drawable.dairy_icon;
            default:
                return -1;
        }
    }
}
