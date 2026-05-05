class MarineLife {
    String name;
    MarineLife(String name) { this.name = name; }

    String move() {
        return name + " 在水中移動";
    }
}

class Fish extends MarineLife {
    Fish(String name) { super(name); }

    @Override
    String move() {
        return name + " 擺動魚鰭快速游動";
    }
}

class Whale extends MarineLife {
    Whale(String name) { super(name); }

    @Override
    String move() {
        return name + " 擺動巨大尾鰭前進";
    }
}

class Jellyfish extends MarineLife {
    Jellyfish(String name) { super(name); }

    @Override
    String move() {
        return name + " 收縮傘狀身體漂浮";
    }
}

public class MarineDemo {
    public static void main(String[] args) {
        MarineLife[] ocean = {
    new Fish("小丑魚"),
    new Whale("藍鯨"),
    new Jellyfish("水母")
};

    for (MarineLife m : ocean) {
        System.out.println(m.move());
        }
    }
}