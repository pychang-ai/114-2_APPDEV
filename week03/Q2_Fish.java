public class Q2_Fish {
// 1. 宣告兩個屬性
String name;
double weight;
// 2. 撰寫 displayInfo 方法
public void displayInfo() {
System.out.println("這隻魚的名字是：" + name + "，重量是：" + weight + "公斤");
}
public static void main(String[] args) {
// 3. 實例化 Q2_Fish 物件
Q2_Fish myFish = new Q2_Fish();
// 4. 將 name 設為 "黑鮪魚"，weight 設為 250.5
myFish.name = "黑鮪魚";
myFish.weight = 250.5;
// 5. 呼叫 displayInfo() 方法
myFish.displayInfo();
}
}
