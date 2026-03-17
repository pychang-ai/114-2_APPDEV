public class Q3_Turtle {
String species;
int age;
// 1. 撰寫建構子 (Constructor)，接收 species 與 age 作為參數並初始化屬性
public Q3_Turtle(String species, int age) {
this.species = species; // 利用 this. 區分屬性與參數
this.age = age;
}
public void showDetails() {
System.out.println("品種：" + species + "，年紀：" + age + "歲");
}
public static void main(String[] args) {
// 2. 利用建構子，直接在建立物件時傳入初始值
Q3_Turtle myTurtle = new Q3_Turtle("綠蠵龜", 50);
// 呼叫方法印出資訊
myTurtle.showDetails();
}
}