public class Q2_Fish{
    String name;
    double weight;

    public void displayinf(){
         System.out.println("這隻魚的名字是：" + name + "，重量是："+ weight +"公斤");
    }

    public static void main(String [] args){
        Q2_Fish myfish = new Q2_Fish();
        myfish.name = "黑鮪魚";
        myfish.weight =250.5;

        myfish.displayinf();

    }

}