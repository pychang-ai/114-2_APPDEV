package week05;

// 父類別
class MarineLife {
    protected String name;
    protected String habitat;

    public MarineLife(String name, String habitat) {
        this.name = name;
        this.habitat = habitat;
    }

    public String move() {
        return name + " 在水中移動";
    }

    public String eat() {
        return name + " 正在覓食";
    }

    public String info() {
        return name + "（" + habitat + "）";
    }
}

// Shark 類別
class Shark extends MarineLife {    // TODO 1: Shark 繼承 MarineLife
    public Shark(String name, String habitat) {
        super(name, habitat);
    }

    @Override
    public String move() {
        return name + " 高速衝刺獵食";  // - 覆寫 move() → "[name] 高速衝刺獵食"
    }

    @Override
    public String eat() {
        return name + " 撕咬獵物";  // - 覆寫 eat() → "[name] 撕咬獵物"
    }
}

// Turtle 類別
class Turtle extends MarineLife {   // TODO 2: Turtle 繼承 MarineLife
    public Turtle(String name, String habitat) {
        super(name, habitat);
    }

    @Override
    public String move() {
        return name + " 緩慢划動四肢";  // - 覆寫 move() → "[name] 緩慢划動四肢"
    }

    @Override
    public String eat() {
        return name + " 啃食海草";  // - 覆寫 eat() → "[name] 啃食海草"
    }
}

// Dolphin 類別
class Dolphin extends MarineLife {  // TODO 3: Dolphin 繼承 MarineLife
    public Dolphin(String name, String habitat) {
        super(name, habitat);
    }

    @Override
    public String move() {
        return name + " 躍出水面再潛入";    // - 覆寫 move() → "[name] 躍出水面再潛入"
    }

    @Override
    public String eat() {
        return name + " 合作圍捕魚群";  // - 覆寫 eat() → "[name] 合作圍捕魚群"
    }
}

// 主程式
public class OceanSimulator {
    public static void main(String[] args) {
        System.out.println("=== 海洋模擬器 ===\n");

        MarineLife[] ocean = {  // TODO 4: 建立 MarineLife 陣列，放入三個物件
            new Shark("大白鯊", "深海"),
            new Turtle("綠蠵龜", "珊瑚礁"),
            new Dolphin("瓶鼻海豚", "近海")
        };

        for (MarineLife creature : ocean) {
            System.out.println(creature.info());
            System.out.println("  移動：" + creature.move());
            System.out.println("  覓食：" + creature.eat());
            System.out.println();
        }
    }
}