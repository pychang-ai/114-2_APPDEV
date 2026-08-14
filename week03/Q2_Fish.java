public class Q2_Fish {
    // i. 宣告兩個屬性 (狀態)
    String name;
    double weight;

    // ii. 撰寫 displayInfo() 方法 (行為)
    public void displayInfo() {
        System.out.println("這隻魚的名字是：" + name + "，重量是：" + weight + "公斤");
    }

    // iii. 主程式入口
    public static void main(String[] args) {
        // 產生一個 Q2_Fish 的物件 (實例化)
        Q2_Fish myFish = new Q2_Fish();

        // 將這隻魚的屬性設值
        myFish.name = "黑鮪魚";
        myFish.weight = 250.5;

        // 呼叫這隻魚的方法
        myFish.displayInfo();
    }
}

}
