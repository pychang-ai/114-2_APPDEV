public class Q2_fish{
    String name;
    double weight;

    public void displayinf(){
         System.out.println("This fish's name is:" + name + "Weight: "+ wieght +"KG");
    }

    public static void main(String [] args){
        Q2_fish myfish = new Q2_fish();
        myfish.name = "Black fish";
        myfish.weight =250.5;

        myfish.displayinf();

    }

}