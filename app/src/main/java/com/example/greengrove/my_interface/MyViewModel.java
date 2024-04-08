package com.example.greengrove.my_interface;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.greengrove.Model.Category;

import java.util.List;

public class MyViewModel extends ViewModel {
    private MutableLiveData<List<Category>> categories = new MutableLiveData<>();

    public void setCategories(List<Category> newCategories) {
        categories.setValue(newCategories);
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }
}
