class Creature {
    protected String name;
    protected String habitat;

    // 版本 1：無參數
    String feed(){
        return name + " 正在覓食";
    }
    // 回傳：name + " 正在覓食"

    // 版本 2：指定食物
    String feed(String food){
        return name + " 正在吃 " + food;
    }
    // 回傳：name + " 正在吃 " + food

    // 版本 3：指定食物和數量
    String feed(String food, int amount){
        return name + " 吃了 " + amount + " 份 " + food;
    }

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
        return name + "（" + habitat + "）";
    }

    public final String kingdom() {
            return "動物界";
        }
}

class Seahorse extends Creature {
    Seahorse(String name, String habitat) { super(name, habitat); }

    public @Override
    String move() {
        return name + " 擺動背鰭直立游動";
    }
    public @Override
    String eat(){
        return name + " 吸食浮游生物";
    }
}

class Crab extends Creature {
    Crab(String name, String habitat) { super(name, habitat); }

    public @Override
    String move() {
        return name + " 橫向移動";
    }
    public @Override
    String eat(){
        return name + " 用螯夾取食物";
    }
}

class Whale extends Creature {
    Whale(String name, String habitat) { super(name, habitat); }

    public @Override
    String move() {
        return name + " 在深海中游泳";
    }
    public @Override
    String eat(){
        return name + " 張口吞食大量水和小魚";
    }
}

class Octopus extends Creature {
    Octopus(String name, String habitat) { super(name, habitat); }

    public @Override
    String move() {
        return name + " 噴射水流推進";
    }
    public @Override
    String eat(){
        return name + " 用觸手捕捉獵物";
    }
}

public class MarineEcosystem {
    public static void main(String[] args) {
        final int OCEAN_DEPTH = 11034;
        System.out.println("海洋最深處：" + OCEAN_DEPTH + " 公尺\n");

        Creature[] ecosystem = {
            // 放入 4 個子類別的物件
            new Seahorse("海馬", "珊瑚礁"),
            new Crab("螃蟹", "沙灘"),
            new Whale("鯨魚", "深海"),
            new Octopus("章魚", "岩洞")
        };

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