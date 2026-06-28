package week05;

public class ShipCalculator {

    // TODO 1: 計算兩個港口之間的運費（整數公里）
    public int fee(int km) {
        return km * 50;
    }

    // TODO 2: 計算運費（浮點數公里，精確距離）
    public double fee(double km) {
        return km * 50.0;
    }

    // TODO 3: 計算運費（公里 + 貨物重量）
    public double fee(int km, double weight) {
        return km * 50 + weight * 10;
    }

    // TODO 4: 計算航行時間
    public double travelTime(int km) {
        return km / 30.0;
    }
    public double travelTime(int km, int speed) {
        return km / (double) speed;
    }

    public static void main(String[] args) {
        ShipCalculator calc = new ShipCalculator();

        System.out.println("=== 船舶運費計算器 ===");
        System.out.println("100 公里運費：" + calc.fee(100));
        System.out.println("100.5 公里運費：" + calc.fee(100.5));
        System.out.println("100 公里 + 500kg 運費：" + calc.fee(100, 500.0));
        System.out.println("100 公里航行時間：" +
            String.format("%.1f", calc.travelTime(100)) + " 小時");
        System.out.println("100 公里（時速 20）：" +
            String.format("%.1f", calc.travelTime(100, 20)) + " 小時");
    }
}