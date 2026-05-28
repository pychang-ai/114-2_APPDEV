class MarineLife{
    protected String name;
    protected String habitat;

    public MarineLife(String name, String habitat) {
        this.name = name;
        this.habitat = habitat;
    } 
    public String kingdom() {
        return name + " 動物界";
    }
    
    public String move() {
        return name + " 在水中移動";
    }

    public String eat() {
        return name + " 正在覓食";
    }
    
    public String feed() {
        return name + " 正在覓食";
    }

    public String feed(String food) {
        return name + " 正在吃" +food;
    }

    public String feed(String food, int amount) {
        return name + " 吃了 " + amount + " 份 " + food;
    }
    
    public String describe() {
        return name + "（" + habitat + "）";
    }
}

class Shark extends MarineLife {
    public Shark(String name, String habitat) {
        super(name, habitat);
    }

    @Override
    public String move() {
        return name + " 高速衝刺";
    }

    @Override
    public String eat() {
        return name + " 撕咬獵物";
    }
    
    @Override
    public String feed() {
        return name + " 正在覓食";
    }

    @Override
    public String feed(String food) {
        return name + " 正在吃" +food;
    }

    @Override
    public String feed(String food, int amount) {
        return name + " 吃了 " + amount + " 份 " + food;
    }
}


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
    
    @Override
    public String feed() {
        return name + " 正在覓食";
    }

    @Override
    public String feed(String food) {
        return name + " 正在吃" +food;
    }

    @Override
    public String feed(String food, int amount) {
        return name + " 吃了 " + amount + " 份 " + food;
    }
}

class Dolphin extends MarineLife {
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
    
    @Override
    public String feed() {
        return name + " 正在覓食";
    }

    @Override
    public String feed(String food) {
        return name + " 正在吃" +food;
    }

    @Override
    public String feed(String food, int amount) {
        return name + " 吃了 " + amount + " 份 " + food;
    }
}

class Octopus extends MarineLife {
    public Octopus(String name, String habitat) {
        super(name, habitat);
    }

    @Override
    public String move() {
        return name + " 噴射水流推進";
    }

    @Override
    public String eat() {
        return name + " 用觸手捕捉獵物";
    }
    
    @Override
    public String feed() {
        return name + " 正在覓食";
    }

    @Override
    public String feed(String food) {
        return name + " 正在吃" +food;
    }

    @Override
    public String feed(String food, int amount) {
        return name + " 吃了 " + amount + " 份 " + food;
    }
}
public class MarineEcosystem {
    

     public static void main(String[] args) {
        final int OCEAN_DEPTH=11034;
        System.out.println("海洋最深處：" + OCEAN_DEPTH + " 公尺\n");

        
        MarineLife[] ocean = {
           
            new Shark("大白鯊", "深海"),
            new Turtle("綠蠵龜", "珊瑚礁"),
            new Dolphin("瓶鼻海豚", "近海"),
            new Octopus("藍圈章魚", "珊瑚礁")
        };

        for (MarineLife c : ocean) {
            System.out.println(c.describe());
            System.out.println("  分類：" + c.kingdom());
            System.out.println("  移動：" + c.move());
            System.out.println("  覓食：" + c.eat());
            System.out.println("  覓食：" + c.eat());
            System.out.println("  餵食：" + c.feed());
            System.out.println("  餵食：" + c.feed("小魚"));
            System.out.println("  餵食：" + c.feed("小魚", 3));
            System.out.println();
        }
    }
}
