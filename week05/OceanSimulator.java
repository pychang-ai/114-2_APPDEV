
// 1. 父類別 Creature
class Creature {
    protected String name;
    protected String habitat;

    public Creature(String name, String habitat) {
        this.name = name;
        this.habitat = habitat;
    }

    public String move() {
        return name + " 在水中移動";
    }

    public String eat() {
        return name + " 正在覓食";
    }

    public String describe() {
        return "生物名稱：" + name + "，棲息地：" + habitat;
    }

    // final 方法：子類別不能覆寫
    public final String kingdom() {
        return "動物界";
    }

    // 方法多載 feed()
    public String feed() {
        return name + " 正在覓食";
    }

    public String feed(String food) {
        return name + " 正在吃 " + food;
    }

    public String feed(String food, int amount) {
        return name + " 吃了 " + amount + " 份 " + food;
    }
}

// 2. 子類別繼承與覆寫 (Overriding)
class Shark extends Creature {
    public Shark(String name, String habitat) {
        super(name, habitat);
    }
    @Override
    public String move() { return name + " 高速衝刺獵食"; }
    @Override
    public String eat() { return name + " 撕咬獵物"; }
}

class Turtle extends Creature {
    public Turtle(String name, String habitat) {
        super(name, habitat);
    }
    @Override
    public String move() { return name + " 緩慢劃動四肢"; }
    @Override
    public String eat() { return name + " 啃食海草"; }
}

class Dolphin extends Creature {
    public Dolphin(String name, String habitat) {
        super(name, habitat);
    }
    @Override
    public String move() { return name + " 躍出水面再潛入"; }
    @Override
    public String eat() { return name + " 合作圍捕魚群"; }
}

class Octopus extends Creature {
    public Octopus(String name, String habitat) {
        super(name, habitat);
    }
    @Override
    public String move() { return name + " 噴射水流推進"; }
    @Override
    public String eat() { return name + " 用觸手捕捉獵物"; }
}

// 3. 模擬器執行主程式
public class OceanSimulator {
    public static void main(String[] args) {
        // final 變數：數值不可修改
        final int OCEAN_DEPTH = 11034;
        System.out.println("=== 海洋生態模擬器 ===");
        System.out.println("海洋最深處約為：" + OCEAN_DEPTH + " 公尺\n");

        // 使用父類別陣列儲存子類別物件 (多型展示)
        Creature[] ecosystem = {
            new Shark("大白鯊", "深海"),
            new Turtle("綠蠵龜", "珊瑚礁"),
            new Dolphin("瓶鼻海豚", "近海"),
            new Octopus("大王烏賊", "深海泥沙區")
        };

        for (Creature c : ecosystem) {
            System.out.println(c.describe());
            System.out.println(" 分類：" + c.kingdom());
            System.out.println(" 移動：" + c.move());
            System.out.println(" 覓食：" + c.eat());
            
            // 測試多載的方法
            System.out.println(" [餵食測試 1]：" + c.feed());
            System.out.println(" [餵食測試 2]：" + c.feed("磷蝦"));
            System.out.println(" [餵食測試 3]：" + c.feed("小魚", 5));
            System.out.println("----------------------------------------");
        }
    }
}
