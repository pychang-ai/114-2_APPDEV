

class Creature {
    String name;
    String habitat;

    public Creature(String name, String habitat) {
        this.name = name;
        this.habitat = habitat;
    }

    public String move() {
        return name + " 正在移動";
    }

    public String eat() {
        return name + " 正在進食";
    }

    public String describe() {
        return name + "（" + habitat + "）";
    }

    // final 方法：子類別不能覆寫
    public final String kingdom() {
        return "動物界";
    }

    // --- 方法多載 (Overloading) ---
    // 版本 1：無參數
    public String feed() {
        return name + " 正在覓食";
    }

    // 版本 2：指定食物
    public String feed(String food) {
        return name + " 正在吃 " + food;
    }

    // 版本 3：指定食物和數量
    public String feed(String food, int amount) {
        return name + " 吃了 " + amount + " 份 " + food;
    }
}

// 子類別 1：鯊魚
class Shark extends Creature {
    public Shark(String name, String habitat) {
        super(name, habitat);
    }
    @Override
    public String move() { return name + " 高速衝刺"; }
    @Override
    public String eat() { return name + " 撕咬獵物"; }
}

// 子類別 2：海龜
class Turtle extends Creature {
    public Turtle(String name, String habitat) {
        super(name, habitat);
    }
    @Override
    public String move() { return name + " 緩慢划動四肢"; }
    @Override
    public String eat() { return name + " 啃食海草"; }
}

// 子類別 3：海豚
class Dolphin extends Creature {
    public Dolphin(String name, String habitat) {
        super(name, habitat);
    }
    @Override
    public String move() { return name + " 躍出水面"; }
    @Override
    public String eat() { return name + " 合作圍捕魚群"; }
}

// 子類別 4：章魚
class Octopus extends Creature {
    public Octopus(String name, String habitat) {
        super(name, habitat);
    }
    @Override
    public String move() { return name + " 噴射水流推進"; }
    @Override
    public String eat() { return name + " 用觸手捕捉獵物"; }
}

// 主程式
public class MarineEcosystem {
    public static void main(String[] args) {
        // final 變數
        final int OCEAN_DEPTH = 11034;
        System.out.println("海洋最深處：" + OCEAN_DEPTH + " 公尺\n");

        // 多型展示：用父類別陣列裝子類別物件
        Creature[] ecosystem = {
            new Shark("大白鯊", "深海"),
            new Turtle("綠蠵龜", "珊瑚礁"),
            new Dolphin("瓶鼻海豚", "近海"),
            new Octopus("藍環章魚", "岩礁")
        };

        // 使用 for-each 迴圈展示不同行為
        for (Creature c : ecosystem) {
            System.out.println(c.describe());
            System.out.println("  分類：" + c.kingdom());
            System.out.println("  移動：" + c.move());
            System.out.println("  覓食：" + c.eat());
            System.out.println("  餵食：" + c.feed());
            System.out.println("  餵食：" + c.feed("小魚"));
            System.out.println("  餵食：" + c.feed("小魚", 3));
            System.out.println();
        }
    }
}