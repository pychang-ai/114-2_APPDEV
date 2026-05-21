
class MarineLife {

    protected String name;
    protected String habitat;

    public MarineLife(String name, String habitat) {
        this.name = name;
        this.habitat = habitat + "";
    }

    public String move() {
        return name + " 在水中移動";
    }

    public String eat() {
        return name + " 正在覓食";
    }

    public String info() {
        return name + "（" + habitat + "）";
    }
}

class Shark extends MarineLife {

    Shark(String name, String habitat) {
        super(name, habitat);
    }

    @Override
    public String move() {
        return name + "高速衝刺獵食";
    }

    @Override
    public String eat() {
        return name + "高速衝刺獵食";
    }
}

public class OceanSimulator {

    public static void main(String[] args) {
        MarineLife[] ocean = {new Shark("大白鯊", "深海")
        };

        for (MarineLife creature : ocean) {
            System.out.println(creature.info());
            System.out.println("  移動：" + creature.move());
            System.out.println("  覓食：" + creature.eat());
            System.out.println();
        }
    }
}
