package week04.week04;

public class Geeks2 {

    static String name;
    static float price;

    static void set(String n, float p) {
        name = n;
        price = p;
    }

    static void get() {
        System.out.println("Software name is: " + name);
        System.out.println("Software price is: " + price);
    }

    public static void main(String[] args) {
        Geeks2.set("Visual Studio", 0.0f);
        Geeks2.get();
    }
}
