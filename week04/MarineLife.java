// 父類別
class BaseMarineLife { // 為了避免與 public class 同名衝突，這裡改名為 BaseMarineLife
    protected String name;
    protected String habitat;

    public BaseMarineLife(String name, String habitat) {
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

// TODO 1: Fish 繼承 BaseMarineLife
class Fish extends BaseMarineLife {
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

// TODO 2: Whale 繼承 BaseMarineLife
class Whale extends BaseMarineLife {
    private double length;

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

public class MarineLife {
    public static void main(String[] args) {
        // TODO 3: 建立物件並印出資訊
        Fish clownFish = new Fish("小丑魚", "珊瑚礁", "橘白相間");
        System.out.println(clownFish.fishInfo());
        System.out.println(clownFish.move());

        Whale blueWhale = new Whale("藍鯨", "太平洋", 30.0);
        System.out.println(blueWhale.whaleInfo());
        System.out.println(blueWhale.move());
    }
}
