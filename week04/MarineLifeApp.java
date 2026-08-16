package week04;

// 1. 父類別：定義共同屬性與方法
class MarineLife {
    protected String name;
    protected String habitat;

    public MarineLife(String name, String habitat) {
        this.name = name;
        this.habitat = habitat;
    }

    public String info() {
        return name + "，棲息地：" + habitat;
    }

    public String move() {
        return name + " 在水中移動";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }
}

// 2. 子類別 Fish
class Fish extends MarineLife {
    private String scaleColor;

    public Fish(String name, String habitat, String scaleColor) {
        super(name, habitat); // 呼叫父類別建構子
        this.scaleColor = scaleColor;
    }

    @Override
    public String move() { // 覆寫父類別方法
        return name + " 擺動魚鰭游泳";
    }

    public String fishInfo() {
        return info() + " 鱗片：" + scaleColor;
    }
}

// 3. 子類別 Whale
class Whale extends MarineLife {
    private final double length;

    public Whale(String name, String habitat, double length) {
        super(name, habitat);
        this.length = length;
    }

    @Override
    public String move() { // 覆寫父類別方法
        return name + " 擺動尾鰭前進";
    }

    public String whaleInfo() {
        return info() + " 體長：" + length + " 公尺";
    }
}

// 4. 主程式類別 (檔名必須為 MarineLife.java)
public class MarineLifeApp {
    public static void main(String[] args) {
        System.out.println("=== 海洋生物觀察日誌 ===\n");

        // --- 多型 (Polymorphism) 的應用 ---
        // 使用父類別型態的陣列來儲存不同的子類別物件
        MarineLife[] creatures = {
            new Fish("小丑魚", "珊瑚礁", "橘白相間"),
            new Whale("藍鯨", "太平洋", 30.0)
        };

        System.out.println(">> 透過多型呼叫 move():");
        for (MarineLife c : creatures) {
            // 這裡會自動根據物件的「實際類型」執行對應的 move()
            System.out.println(c.move()); 
        }

        System.out.println("\n----------------------------\n");

        // --- 個別物件詳細資訊 ---
        Fish nemo = new Fish("小丑魚", "珊瑚礁", "橘白相間");
        Whale blue = new Whale("藍鯨", "太平洋", 30.0);

        System.out.println(nemo.fishInfo());
        System.out.println("移動方式：" + nemo.move());

        System.out.println();

        System.out.println(blue.whaleInfo());
        System.out.println("移動方式：" + blue.move());
    }
}