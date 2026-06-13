public class Q2_Fish {
    // 屬性定義
    String name;
    double weight;

    // 方法：顯示資訊 (中文版)
    public void displayInfo() {
        System.out.println("這隻魚的名字是：" + name + "，重量是：" + weight + "公斤");
    }

    // 方法：顯示資訊 (英文版，修正拼字後)
    public void displayInfoEng() {
        System.out.println("This fish's name is: " + name + ", Weight: " + weight + " KG");
    }

    public static void main(String[] args) {
        // 建立物件
        Q2_Fish myFish = new Q2_Fish();
        
        // 設定屬性
        myFish.name = "黑鮪魚";
        myFish.weight = 250.5;

        // 呼叫方法
        System.out.println("--- 執行結果 ---");
        myFish.displayInfo();
        myFish.displayInfoEng();
    }
}