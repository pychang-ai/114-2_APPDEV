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

// TODO 1: Shark 繼承 MarineLife
class Shark extends MarineLife {
    public Shark(String name, String habitat) {
        super(name, habitat); // 呼叫父類別建構子
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

// TODO 2: Turtle 繼承 MarineLife
class Turtle extends MarineLife {
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

// TODO 3: Dolphin 繼承 MarineLife
class Dolphin extends MarineLife {
    public Dolphin(String name, String habitat) {
        super(name, habitat);
    }

    @Override
    public String move() {
        return name + " 躍出水面再潛入";
    }

    @Override
    public String eat() {
        return name + " 合作圍捕魚群";
    }
}

public class OceanSimulator {
    public static void main(String[] args) {
        System.out.println("=== 海洋模擬器 ===\n");

        // TODO 4: 建立 MarineLife 陣列，放入三個物件
        // 這裡展現了多型：父類別型別的陣列可以存放子類別的實體
        MarineLife[] ocean = {
            new Shark("大白鯊", "深海"),
            new Turtle("綠蠵龜", "珊瑚礁"),
            new Dolphin("瓶鼻海豚", "近海")
        };

        for (MarineLife creature : ocean) {
            System.out.println(creature.info());
            // 程式執行時會根據物件「真正的型別」來決定呼叫哪個版本的 move() 與 eat()
            System.out.println("  移動：" + creature.move());
            System.out.println("  覓食：" + creature.eat());
            System.out.println();
        }
    }
}