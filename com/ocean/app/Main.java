package com.ocean.app;

public class Main {
    public static void main(String[] args) {
        Ship ship = new Ship();
        ship.refuel(50);
        ship.startEngine();
        ship.sail(100);
    }

}
