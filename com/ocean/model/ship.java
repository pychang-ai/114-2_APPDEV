package com.ocean.model;

public class ship {
    public String name; // 加上 public，Main 才能讀取它

    // 必須手動增加這個「建構子」
    public ship(String name) {
        this.name = name;
    }
}