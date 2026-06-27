public class Q2_Fish {
    // 1. 定義屬性 (Fields)
    String name;
    double weight;

    // 2. 定義展示資訊的方法
    public void displayInfo() {
        // 修正點：確保變數拼寫正確 (weight)
        System.out.println("這隻魚的名字是：" + name + "，重量是：" + weight + " 公斤");
    }

    // 3. 程式進入點
    public static void main(String[] args) {
        // 建立物件
        Q2_Fish myFish = new Q2_Fish();
        
        // 設定屬性值
        myFish.name = "黑鮪魚";
        myFish.weight = 250.5;

        // 呼叫方法
        myFish.displayInfo();
    }
}