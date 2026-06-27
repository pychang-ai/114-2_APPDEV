package week04;

// 父類別
class MarineLifeParent {
    // 加上 final 表示初始化後不可更改
    protected final String name;
    protected final String habitat;

    public MarineLifeParent(String name, String habitat) {
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

class Fish extends MarineLifeParent {
    private final String scaleColor; // 修正警告：加上 final

    public Fish(String name, String habitat, String scaleColor) {
        super(name, habitat); 
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

class Whale extends MarineLifeParent {
    private final double length; // 修正警告：加上 final

    public Whale(String name, String habitat, double length) {
        super(name, habitat);
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

// 檔名必須為 MarineLife.java
public class MarineLife {
    public static void main(String[] args) {
        System.out.println("=== 海洋生物觀察日誌 ===\n");

        Fish clownFish = new Fish("小丑魚", "珊瑚礁", "橘白相間");
        System.out.println(clownFish.fishInfo());
        System.out.println("移動方式：" + clownFish.move());
        
        System.out.println(); // 換行美化

        Whale blueWhale = new Whale("藍鯨", "太平洋", 30.0);
        System.out.println(blueWhale.whaleInfo());
        System.out.println("移動方式：" + blueWhale.move());
    }
}