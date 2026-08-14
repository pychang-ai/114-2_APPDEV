/**
 * W05 課後作業：海洋生態系統模擬器
 * 學生：furouwang (c112181150)
 */

// 1. 父類別 Creature
class Creature {
    protected String name;
    protected String habitat;

    public Creature(String name, String habitat) {
        this.name = name;
        this.habitat = habitat;
    }

    // 基本移動描述
    public String move() {
        return name + " 正在水中移動";
    }

    // 基本覓食描述
    public String eat() {
        return name + " 正在尋找食物";
    }

    // 描述生物基本資訊
    public String describe() {
        return name + "（" + habitat + "）";
    }

    // 4. final 方法：子類別不可覆寫
    public final String kingdom() {
        return "動物界";
    }

    // 3. 方法多載 feed() - 版本 1：無參數
    public String feed() {
        return name + " 正在覓食";
    }

    // 3. 方法多載 feed() - 版本 2：指定食物
    public String feed(String food) {
        return name + " 正在吃 " + food;
    }

    // 3. 方法多載 feed() - 版本 3：指定食物和數量
    public String feed(String food, int amount) {
        return name + " 吃了 " + amount + " 份 " + food;
    }
}

// 2. 子類別實作 (至少 4 個)

class Shark extends Creature {
    public Shark(String name, String habitat) {
        super(name, habitat);
    }

    @Override
    public String move() {
        return name + " 高速衝刺獵食";
    }

    @Override
    public String eat() {
        return name + " 撕咬獵物";
    }
}

class Turtle extends Creature {
    public Turtle(String name, String habitat) {
        super(name, habitat);
    }

    @Override
    public String move() {
        return name + " 緩慢划動四肢";
    }

    @Override
    public String eat() {
        return name + " 啃食海草";
    }
}

class Dolphin extends Creature {
    public Dolphin(String name, String habitat) {
        super(name, habitat);
    }

    @Override
    public String move() {
        return name + " 躍出水面";
    }

    @Override
    public String eat() {
        return name + " 合作圍捕魚群";
    }
}

class Octopus extends Creature {
    public Octopus(String name, String habitat) {
        super(name, habitat);
    }

    @Override
    public String move() {
        return name + " 噴射水流推進";
    }

    @Override
    public String eat() {
        return name + " 用觸手捕捉獵物";
    }
}

// 5. 主程式展示多型
public class MarineEcosystem {
    public static void main(String[] args) {
        // 4. final 變數
        final int OCEAN_DEPTH = 11034;
        System.out.println("海洋最深處：" + OCEAN_DEPTH + " 公尺\n");

        // 5. 使用 Creature 陣列放入不同子類別物件 (展現多型)
        Creature[] ecosystem = {
            new Shark("大白鯊", "深海"),
            new Turtle("綠蠵龜", "珊瑚礁"),
            new Dolphin("瓶鼻海豚", "近海"),
            new Octopus("大王烏賊", "深海底")
        };

        // 使用 for-each 迴圈呼叫方法
        for (Creature c : ecosystem) {
            System.out.println(c.describe());
            System.out.println("  分類：" + c.kingdom()); // 呼叫 final 方法
            System.out.println("  移動：" + c.move());    // 展現 @Override
            System.out.println("  覓食：" + c.eat());     // 展現 @Override
            
            // 展示方法多載 (Overloading)
            System.out.println("  餵食：" + c.feed());
            System.out.println("  餵食：" + c.feed("小魚"));
            System.out.println("  餵食：" + c.feed("小魚", 3));
            System.out.println();
        }
    }
}

