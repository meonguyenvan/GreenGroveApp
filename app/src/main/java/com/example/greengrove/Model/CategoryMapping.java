package com.example.greengrove.Model;

import android.content.SharedPreferences;

import com.example.greengrove.serviecs.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryMapping {
    private Map<String, String> idToNameMap = new HashMap<>();

    public void addCategories(ArrayList<Categor> categoryList) {
        for (Categor category : categoryList) {
            String id = category.getId();
            String name = category.getName();
            idToNameMap.put(id, name);
        }
    }
    public String getCategoryName(Categor categor) {
        return idToNameMap.get(categor.getId()); // Sử dụng id của category để lấy tên category từ map
    }
}
