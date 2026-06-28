// 父類別
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
}

// TODO 1: Fish 繼承 MarineLife
class Fish extends MarineLife {
    private String scaleColor;

    public Fish(String name, String habitat, String scaleColor) {
        super(name, habitat); // 呼叫父類別建構子
        this.scaleColor = scaleColor;
    }

    @Override
    public String move() {
        return name + " 擺動魚鰭游泳";
    }

    public String fishInfo() {
        return info() + " 鱗片：" + scaleColor;
    }
}

// TODO 2: Whale 繼承 MarineLife
class Whale extends MarineLife {
    private double length;

    public Whale(String name, String habitat, double length) {
        super(name, habitat); // 呼叫父類別建構子
        this.length = length;
    }

    @Override
    public String move() {
        return name + " 擺動尾鰭前進";
    }

    public String whaleInfo() {
        return info() + " 體長：" + length + " 公尺";
    }
}

public class MarineLife {
    public static void main(String[] args) {
        // TODO 3: 建立 Fish 和 Whale 各一個
        Fish f = new Fish("小丑魚", "珊瑚礁", "橘白相間");
        Whale w = new Whale("藍鯨", "太平洋", 30.0);

        System.out.println(f.fishInfo());
        System.out.println(f.move());

        System.out.println(w.whaleInfo());
        System.out.println(w.move());
    }
}
