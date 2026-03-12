public class Q3_Turtle {
String species;
int age;
public Q3_Turtle(String species, int age) {
this.species = species; // 利用 this. 區分屬性與參數
this.age = age;
}
public void showDetails() {
System.out.println("品種：" + species + "，年紀：" + age + "歲");
}
public static void main(String[] args) {
  
Q3_Turtle myTurtle = new Q3_Turtle("綠蠵龜", 50);
 
myTurtle.showDetails();
}
}
