public class Q3_Turtle {
    String species;
    int age;

    // 建構子
    public Q3_Turtle(String species, int age) {
        this.species = species;
        this.age = age;
    }

    // 方法：顯示細節 (中文版)
    public void showDetails() {
        System.out.println("品種：" + species + "，年紀：" + age + " 歲");
    }

    // 方法：顯示細節 (英文版)
    public void showDetailEng() {
        System.out.println("This turtle's species is: " + species + ", Age: " + age);
    }

    public static void main(String[] args) {
        // 建立物件
        Q3_Turtle myTurtle = new Q3_Turtle("綠蠵龜", 50);

        // 執行結果
        System.out.println("--- 烏龜資訊 ---");
        myTurtle.showDetails();
        
        // 如果想跑英文版也可以呼叫這個
        // myTurtle.showDetailEng();
    }
}