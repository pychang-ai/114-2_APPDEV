// 父類別：Creature
class Creature {
    protected String name;
    protected String habitat;

    // 建構子
    public Creature(String name, String habitat) {
        this.name = name;
        this.habitat = habitat;
    }

    // 一般移動
    public String move() {
        return name + " 正在移動";
    }

    // 一般覓食
    public String eat() {
        return name + " 正在覓食";
    }

    // 描述名稱與棲息地
    public String describe() {
        return name + "（" + habitat + "）";
    }

    // final 方法（不可被子類別覆寫）
    public final String kingdom() {
        return "動物界";
    }

    // ===== 方法多載 feed() =====

    // 版本1：無參數
    public String feed() {
        return name + " 正在覓食";
    }

    // 版本2：指定食物
    public String feed(String food) {
        return name + " 正在吃 " + food;
    }

    // 版本3：指定食物與數量
    public String feed(String food, int amount) {
        return name + " 吃了 " + amount + " 份 " + food;
    }
}

// ===== 子類別1：Shark =====
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

// ===== 子類別2：Turtle =====
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

// ===== 子類別3：Dolphin =====
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

// ===== 子類別4：Octopus =====
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
        return name + " 利用觸手捕捉獵物";
    }
}

// ===== 主程式 =====
public class Main {
    public static void main(String[] args) {

        // final 變數（不可更改）
        final int OCEAN_DEPTH = 11034;
        System.out.println("海洋最深處：" + OCEAN_DEPTH + " 公尺\n");

        // 多型：使用父類別陣列
        Creature[] ecosystem = {
            new Shark("大白鯊", "深海"),
            new Turtle("綠蠵龜", "珊瑚礁"),
            new Dolphin("瓶鼻海豚", "近海"),
            new Octopus("章魚", "海底岩區")
        };

        // 使用 for-each 展示多型行為
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