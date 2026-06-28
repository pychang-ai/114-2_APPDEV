package week05;

// 父類別：所有海洋生物的共同特性
public class MarineEcosystem {
    protected String name;
    protected String habitat;

    public MarineEcosystem(String name, String habitat) {
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

// 子類別：魚
class Fish extends MarineEcosystem {
    private String scaleColor;

    public Fish(String name, String habitat, String scaleColor) {
        super(name, habitat);       // 呼叫父類別建構子
        this.scaleColor = scaleColor;
    }

    public String move() {          // 覆寫
        return name + " 擺動魚鰭游泳";
    }

    public String fishInfo() {      // 子類別特有方法
        return info() + " 鱗片：" + scaleColor;
    }
}

// 子類別：鯨魚
class Whale extends MarineEcosystem {
    private double length;

    public Whale(String name, String habitat, double length) {
        super(name, habitat);
        this.length = length;
    }

    public String move() {
        return name + " 擺動尾鰭前進";
    }

    public String whaleInfo() {
        return info() + " 體長：" + length + " 公尺";
    }
}

class Main {
    public static void main(String[] args) {
        Fish fish = new Fish("小丑魚", "珊瑚礁", "橘色");
        Whale whale = new Whale("藍鯨", "深海", 30);

        System.out.println(fish.fishInfo());
        System.out.println(fish.move());

        System.out.println(whale.whaleInfo());
        System.out.println(whale.move());
    }
}