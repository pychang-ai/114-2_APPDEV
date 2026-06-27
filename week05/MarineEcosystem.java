/**
 * 1. 父類別 Creature
 * 包含基本屬性、多載方法 (feed) 與 final 方法 (kingdom)
 */
class Creature {
    protected String name;
    protected String habitat;

    // 建構子接收 name 和 habitat
    public Creature(String name, String habitat) {
        this.name = name;
        this.habitat = habitat;
    }

    // 方法 move()：回傳一般移動描述
    public String move() {
        return name + " 正在水中移動";
    }

    // 方法 eat()：回傳一般覓食描述
    public String eat() {
        return name + " 正在尋找食物";
    }

    // 方法 describe()：回傳完整的描述資訊
    public String describe() {
        return name + "（" + habitat + "）";
    }

    // 4. final 方法：回傳「動物界」，子類別不能覆寫
    public final String kingdom() {
        return "動物界";
    }

    // 3. 方法多載 (Overloading) feed() - 實作 3 個版本
    
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

/**
 * 2. 子類別實作 (正確覆寫 move() 與 eat())
 */

// 子類別 1: Shark
class Shark extends Creature {
    public Shark(String name, String habitat) {
        super(name, habitat);
    }

    @Override
    public String move() { return name + " 高速衝刺獵食"; }
    
    @Override
    public String eat() { return name + " 撕咬獵物"; }
}

// 子類別 2: Turtle
class Turtle extends Creature {
    public Turtle(String name, String habitat) {
        super(name, habitat);
    }

    @Override
    public String move() { return name + " 緩慢划動四肢"; }

    @Override
    public String eat() { return name + " 啃食海草"; }
}

// 子類別 3: Dolphin
class Dolphin extends Creature {
    public Dolphin(String name, String habitat) {
        super(name, habitat);
    }

    @Override
    public String move() { return name + " 躍出水面"; }

    @Override
    public String eat() { return name + " 合作圍捕魚群"; }
}

// 子類別 4: Octopus
class Octopus extends Creature {
    public Octopus(String name, String habitat) {
        super(name, habitat);
    }

    @Override
    public String move() { return name + " 噴射水流推進"; }

    @Override
    public String eat() { return name + " 用觸手捕捉獵物"; }
}

/**
 * 5. Main 類別：展示多型與 final 變數
 */
public class MarineEcosystem {
    public static void main(String[] args) {
        // 4. final 變數使用
        final int OCEAN_DEPTH = 11034;
        System.out.println("海洋最深處：" + OCEAN_DEPTH + " 公尺\n");

        // 5. 展示多型：用 Creature[] 陣列放入 4 種生物
        Creature[] ecosystem = {
            new Shark("大白鯊", "深海"),
            new Turtle("綠蠵龜", "珊瑚礁"),
            new Dolphin("瓶鼻海豚", "近海"),
            new Octopus("大王紅章魚", "底棲帶")
        };

        // 使用 for-each 迴圈展示不同行為
        for (Creature c : ecosystem) {
            System.out.println(c.describe());
            System.out.println("  分類：" + c.kingdom()); // 呼叫父類別 final 方法
            System.out.println("  移動：" + c.move());    // 展現 Overriding 多型
            System.out.println("  覓食：" + c.eat());     // 展現 Overriding 多型
            
            // 展示 Overloading (多載) 的三種形式
            System.out.println("  餵食：" + c.feed());
            System.out.println("  餵食：" + c.feed("小魚"));
            System.out.println("  餵食：" + c.feed("小魚", 3));
            System.out.println(" ");
        }
    }
}