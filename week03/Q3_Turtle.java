public class Q3_Turtle {
    // i. 屬性已經宣告好
    String species;
    int age;

    // ii. 撰寫建構子 (Constructor)
    // 注意：建構子的名稱必須跟「類別名稱」完全一樣，且沒有回傳值型態（連 void 都不寫）
    public Q3_Turtle(String species, int age) {
        this.species = species; // 將傳入的參數設定給類別的屬性
        this.age = age;
    }

    // 已宣告好的 showDetails() 方法
    public void showDetails() {
        System.out.println("品種：" + species + "，年紀：" + age + "歲");
    }

    public static void main(String[] args) {
        // iii. 利用建構子直接建立物件
        // 在括號內直接傳入 "綠蠵龜" 和 50
        Q3_Turtle myTurtle = new Q3_Turtle("綠蠵龜", 50);

        // iv. 呼叫方法印出資訊
        myTurtle.showDetails();
    }
}
