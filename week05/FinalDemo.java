// TODO 2: Ship 類別
class Ship {
    protected String name;

    public Ship(String name) {
        this.name = name;
    }

    // final 方法：子類別「不能」改寫這個方法
    public final String type() {
        return "船舶";
    }

    public String sail() {
        return name + " 正在航行";
    }
}

// TODO 3: FishingBoat 繼承 Ship
class FishingBoat extends Ship {
    public FishingBoat(String name) {
        super(name);
    }

    // 覆寫一般方法
    @Override
    public String sail() {
        return name + " 正在拖網捕魚";
    }

    /* // 嘗試覆寫 type() 會發生編譯錯誤：
    // Cannot override the final method from Ship
    @Override
    public final String type() {
        return "漁船";
    }
    */
}

public class FinalDemo {

    // TODO 1: 宣告 final 變數 (常量)
    static final int MAX_DEPTH = 11034; 

    public static void main(String[] args) {
        System.out.println("馬里亞納海溝最深：" + MAX_DEPTH + " 公尺");

        // 如果取消註解下面這行，會報錯：The final local variable MAX_DEPTH cannot be assigned
        // MAX_DEPTH = 12000; 

        Ship s = new Ship("遠洋號");
        FishingBoat f = new FishingBoat("海豐號");

        System.out.println(s.type() + "：" + s.sail());
        System.out.println(f.type() + "：" + f.sail());

        // 多型：父類別變數指向子類別物件
        Ship s2 = new FishingBoat("福星號");
        // type() 依然是父類別定義的「船舶」，但 sail() 會執行子類別的版本
        System.out.println(s2.type() + "：" + s2.sail());
    }
}