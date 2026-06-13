abstract class GeeksHelp {
    abstract void check(String name);
}

class Test {
    static void hello() {
        System.out.println("Hello from Test");
    }
}

public class Geeks extends GeeksHelp {

    void hello() {
        System.out.println("This is a user-defined method.");
    }

    @Override
    void check(String name) {
        System.out.println("Name: " + name);
    }

    public static void main(String[] args) {
        Geeks obj = new Geeks();

        obj.hello();              // 呼叫自己的方法
        obj.check("Silas");       // 呼叫 abstract 實作
        Test.hello();             // 呼叫 static 方法
    }
}