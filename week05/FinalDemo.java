package week05;

// TODO 2: Ship 類別
class Ship {
    protected String name;

    public Ship(String name) {
        this.name = name;
    }

    // final 方法：子類別不能修改這個功能的邏輯
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

    @Override
    public String sail() {
        return name + " 正在拖網捕魚";
    }

    /* // 嘗試覆寫 type() 會發生編譯錯誤，因為父類別標記為 final
    @Override
    public String type() {
        return "漁船";
    }
    */
}

public class FinalDemo {

    // TODO 1: 宣告 final 變數 (常數通常建議全大寫)
    static final int MAX_DEPTH = 11034; 

    public static void main(String[] args) {
        System.out.println("馬里亞納海溝最深：" + MAX_DEPTH + " 公尺");

        // 如果取消下面這行的註解，會報錯：Cannot assign a value to final variable MAX_DEPTH
        // MAX_DEPTH = 12000; 

        Ship s = new Ship("遠洋號");
        FishingBoat f = new FishingBoat("海豐號");

        System.out.println(s.type() + "：" + s.sail());
        System.out.println(f.type() + "：" + f.sail());

        // 多型：變數雖然是 Ship，但執行的是 FishingBoat 的 sail()
        Ship s2 = new FishingBoat("福星號");
        System.out.println(s2.type() + "：" + s2.sail());
    }
}
