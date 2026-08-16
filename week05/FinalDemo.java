package week05;

class Ship {
    protected String name;

    // TODO 2: Ship 類別建構子
    public Ship(String name) {
        this.name = name;
    }

    // final 方法：子類別不能修改這個行為
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

    /* // 嘗試覆寫 final 方法會導致編譯錯誤：
    // Cannot override the final method from Ship
    @Override
    public String type() {
        return "漁船";
    }
    */
}

public class FinalDemo {

    // TODO 1: 宣告 final 變數 (常數通常使用大寫)
    static final int MAX_DEPTH = 11034; 

    public static void main(String[] args) {
        // MAX_DEPTH = 12000; // 編譯錯誤：The final field FinalDemo.MAX_DEPTH cannot be assigned
        
        System.out.println("馬里亞納海溝最深：" + MAX_DEPTH + " 公尺");

        Ship s = new Ship("遠洋號");
        FishingBoat f = new FishingBoat("海豐號");

        System.out.println(s.type() + "：" + s.sail());
        System.out.println(f.type() + "：" + f.sail());

        // 多型：宣告為 Ship，但實際指向 FishingBoat 物件
        Ship s2 = new FishingBoat("福星號");
        System.out.println(s2.type() + "：" + s2.sail());
    }
}