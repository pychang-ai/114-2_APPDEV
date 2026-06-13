public class Geeks2 {
    static String name;
    static String price;

    static void set(String n, String p) {
        name = n;
        price = p;
    }
    static void get()
    {
        System.out.println("Software name is:" + name);
        System.out.println("Software price is:" + price);
    }

    public static void main(String[] args) {
        dog tuffy = new dog("tuffy", "papillon", 5, "white");
        System.out.println(tuffy);
    }
}
