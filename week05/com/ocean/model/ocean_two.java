package week05.com.ocean.model;

public class ocean_two {
    public static void main(String[] args) {
        // 物件宣告與指令必須放在 main 方法內
        Fish nemo = new Fish("小丑魚", "珊瑚礁", "橘白相間");
        Whale blue = new Whale("藍鯨", "太平洋", 30.0);

        System.out.println(nemo.fishInfo());   
        System.out.println(nemo.move());       
        System.out.println(blue.whaleInfo());  
        System.out.println(blue.move());       
    }
}
// 父類別：所有海洋生物的共同特性
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
// 子類別：魚
class Fish extends MarineLife {
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
class Whale extends MarineLife {
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