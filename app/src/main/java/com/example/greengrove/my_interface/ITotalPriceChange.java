package com.example.greengrove.my_interface;

public interface ITotalPriceChange {
    void onTotalPriceChange( double totalPrice);
    void onDelteCartItem(String id_fruit);
    void onSaveQuantity(String id_fruit,int quantity);
}
