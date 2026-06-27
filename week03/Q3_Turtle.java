public class Q3_Turtle {
    // 1. 定義屬性
    String species;
    int age;

    // 2. 建構子 (Constructor)
    public Q3_Turtle(String species, int age) {
        this.species = species;
        this.age = age;
    }

    // 3. 定義展示資訊的方法
    public void showDetails() {
        System.out.println("這隻海龜的品種是：" + species + "，年紀：" + age + " 歲");
    }

    // 4. 程式執行進入點
    public static void main(String[] args) {
        // 使用建構子建立物件並給予初始值
        Q3_Turtle myTurtle = new Q3_Turtle("綠蠵龜 (Green Turtle)", 50);
        
        // 呼叫方法顯示結果
        myTurtle.showDetails();
    }
}