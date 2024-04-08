package com.example.greengrove.my_interface;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.greengrove.Model.Category;
import com.example.greengrove.Model.Fruit;

import java.util.List;

public class ViewHolderFruit extends ViewModel {
    private MutableLiveData<List<Fruit>> fruit = new MutableLiveData<>();

    public void setFruits(List<Fruit> newFruit) {
        fruit.setValue(newFruit);
    }

    public LiveData<List<Fruit>> getFruits() {
        return fruit;
    }
}
