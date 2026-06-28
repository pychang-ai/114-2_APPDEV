package week03;
public class Q2_Fish {
    String name;
    double weight;
    public void displayInfo() {
        System.out.println("\n這隻魚的名字是："+name
                        +"，重量是："+weight+"公斤");
    }
    public static void main(String[] args) {
        Q2_Fish myFish = new Q2_Fish();
        myFish.name = "黑鮪魚";
        myFish.weight = 250.5;
        myFish.displayInfo();
    }
}