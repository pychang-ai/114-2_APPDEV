package com.ocean;
public class MarineDemo {
    class MarineLife {
    String name;
    MarineLife(String name) { this.name = name; }

    // 一般移動
    String move() {
        return name + " 在水中移動";
    }
    // 移動指定距離（多載：參數數量不同）
    String move(int meters) {
        return name + " 移動了 " + meters + " 公尺";
    }
    // 移動到指定深度（多載：參數型別不同）
    String move(double depth) {
        return name + " 下潛到 " + depth + " 公尺深";
    }
}






}
