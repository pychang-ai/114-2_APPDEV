// 父類別
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

// TODO 1: Fish 繼承 MarineLife
// - 新增屬性 scaleColor（鱗片顏色）
// - 建構子接收 name, habitat, scaleColor，用 super 呼叫父類別建構子
// - 覆寫 move()，回傳「[name] 擺動魚鰭游泳」
// - 新增方法 fishInfo()，回傳「[info()] 鱗片：[scaleColor]」


// TODO 2: Whale 繼承 MarineLife
// - 新增屬性 length（體長，單位公尺）
// - 建構子接收 name, habitat, length，用 super 呼叫父類別建構子
// - 覆寫 move()，回傳「[name] 擺動尾鰭前進」
// - 新增方法 whaleInfo()，回傳「[info()] 體長：[length] 公尺」


public class MarineLife {
    // 注意：檔名是 MarineLife.java，所以這個 class 要加 public
    // 上面的 MarineLife class 不加 public

    public static void main(String[] args) {
        // TODO 3: 建立 Fish 和 Whale 各一個
        // 印出 fishInfo() / whaleInfo() 和 move()

        // 預期輸出：
        // 小丑魚，棲息地：珊瑚礁 鱗片：橘白相間
        // 小丑魚 擺動魚鰭游泳
        // 藍鯨，棲息地：太平洋 體長：30.0 公尺
        // 藍鯨 擺動尾鰭前進
    }
}
