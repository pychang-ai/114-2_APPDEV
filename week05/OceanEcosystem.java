// 1. 父類別 Creature
class Creature {
    protected String name;
    protected String habitat;

    public Creature(String name, String habitat) {
        this.name = name;
        this.habitat = habitat;
    }

    // final 方法：動物界分類不可更改
    public final String kingdom() {
        return "動物界";
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

    // 3. 方法多載 (Overloading) feed()
    public String feed() {
        return name + " 正在尋找獵物...";
    }

    public String feed(String food) {
        return name + " 正在享用 " + food;
    }

    public String feed(String food, int amount) {
        return name + " 捕獲並吃掉了 " + amount + " 隻 " + food;
    }
}

// 2. 四個主要子類別 (參考真實習性)

class Octopus extends Creature {
    public Octopus(String name, String habitat) { super(name, habitat); }
    @Override
    public String move() { return name + " 噴射水流並在岩縫間爬行"; }
    @Override
    public String eat() { return name + " 注入毒液麻痺獵物並用齒舌鑽開外殼"; }
}

class Seahorse extends Creature {
    public Seahorse(String name, String habitat) { super(name, habitat); }
    @Override
    public String move() { return name + " 快速扇動背鰭以垂直姿態游動"; }
    @Override
    public String eat() { return name + " 隱藏在海草中，瞬間吸入經過的生物"; }
}

class Crab extends Creature {
    public Crab(String name, String habitat) { super(name, habitat); }
    @Override
    public String move() { return name + " 利用八隻步足靈活地橫向移動"; }
    @Override
    public String eat() { return name + " 用強壯的螯夾碎獵物甲殼並撕碎肉塊"; }
}

class Dolphin extends Creature {
    public Dolphin(String name, String habitat) { super(name, habitat); }
    @Override
    public String move() { return name + " 規律地躍出水面換氣並高速游動"; }
    @Override
    public String eat() { return name + " 使用回聲定位並與同伴合作圍捕"; }
}

// 3. 額外 3 種海洋生物

class Stingray extends Creature {
    public Stingray(String name, String habitat) { super(name, habitat); }
    @Override
    public String move() { return name + " 像寬大的翅膀一樣拍動胸鰭滑行"; }
    @Override
    public String eat() { return name + " 覆蓋在沙地獵物上方，用強力的牙齒壓碎貝類"; }
}

class Jellyfish extends Creature {
    public Jellyfish(String name, String habitat) { super(name, habitat); }
    @Override
    public String move() { return name + " 透過肌肉收縮傘部，產生脈動波推進"; }
    @Override
    public String eat() { return name + " 伸展佈滿刺細胞的觸手，麻痺並包裹獵物"; }
}

class SpermWhale extends Creature {
    public SpermWhale(String name, String habitat) { super(name, habitat); }
    @Override
    public String move() { return name + " 屏息下潛至 2000 公尺的漆黑深海"; }
    @Override
    public String eat() { return name + " 在深海與大王烏賊展開激烈的生死搏鬥"; }
}

public class OceanEcosystem {
    public static void main(String[] args) {
        // 4. final 變數
        final int OCEAN_DEPTH = 11034;
        System.out.println("=== 海洋生態系統模擬器 ===");
        System.out.println("當前區域：馬里亞納海溝附近（最深 " + OCEAN_DEPTH + " 公尺）\n");

        // 5. 展示多型
        Creature[] ecosystem = {
            new Octopus("藍環章魚", "潮間帶區"),
            new Seahorse("海馬", "海草床"),
            new Crab("椰子蟹", "海岸礁石"),
            new Dolphin("瓶鼻海豚", "熱帶海域"),
            new Stingray("魟魚", "大陸棚沙底"),
            new Jellyfish("海月水母", "表層洋流"),
            new SpermWhale("抹香鯨", "深海峽谷")
        };

        // 為每種生物準備真實的食物
        String[] realFoods = {"螃蟹", "橈足類", "腐肉", "魷魚", "蛤蜊", "浮游幼蟲", "大王烏賊"};
        int[] amounts = {2, 100, 1, 15, 8, 500, 1};

        for (int i = 0; i < ecosystem.length; i++) {
            Creature c = ecosystem[i];
            System.out.println(c.describe());
            System.out.println("  [分類] " + c.kingdom());
            System.out.println("  [移動] " + c.move());
            System.out.println("  [習性] " + c.eat());
            
            // 展示方法多載與真實食物
            System.out.println("  [覓食中] " + c.feed());
            System.out.println("  [進食中] " + c.feed(realFoods[i]));
            System.out.println("  [飽食後] " + c.feed(realFoods[i], amounts[i]));
            System.out.println();
        }
    }
}