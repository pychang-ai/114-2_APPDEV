package week03;
public class Q3_Turtle {
    String species;
    int age;
    public Q3_Turtle(String species ,int age) {
        this.species = species;
        this.age = age;
    }
    
    public void showDetails() {
        System.out.println("\n品種：" + species + "，年紀：" + age + "歲");
    }

    public static void main(String[] args) {
        Q3_Turtle turtle = new Q3_Turtle("綠蠵龜", 50);
        turtle.showDetails();
    }
}