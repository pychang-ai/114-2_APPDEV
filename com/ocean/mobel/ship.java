package com.ocean.mobel;

class Ship {
    private String name;
    private double fuelLevel;    // 油量 0-100
    private boolean engineOn;

    public Ship(String name) {
        this.name = name;
        this.fuelLevel = 100.0;
        this.engineOn = false;
    }

    public String getName() { return name; }
    public double getFuelLevel() { return fuelLevel; }
    public boolean isEngineOn() { return engineOn; }

    // 加油：有驗證邏輯
    public void refuel(double amount) {
        if (amount > 0) {
            fuelLevel = Math.min(fuelLevel + amount, 100.0);
            System.out.println(name + " 加油完成，油量：" + fuelLevel);
        }
    }

    // 啟動引擎：有前置條件
    public void startEngine() {
        if (fuelLevel > 10) {
            engineOn = true;
            System.out.println(name + " 引擎啟動");
        } else {
            System.out.println(name + " 油量不足，無法啟動");
        }
    }

    // 航行：會消耗油量
    public void sail(int km) {
        if (!engineOn) {
            System.out.println(name + " 引擎未啟動");
            return;
        }
        double fuelNeeded = km * 0.5;
        if (fuelLevel >= fuelNeeded) {
            fuelLevel -= fuelNeeded;
            System.out.println(name + " 航行 " + km + " 公里，剩餘油量：" + fuelLevel);
        } else {
            System.out.println(name + " 油量不足以航行 " + km + " 公里");
        }
    }
}
