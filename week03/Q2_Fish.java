public class Q2_Fish{

    String name;
    double weight;

    public void displayinf(){
        System.out.println("This fish's is:" + name + " Weight:"+weight+"KG");
    }

    public static void main(String [] args){
        Q2_Fish myfish = new Q2_Fish();
        myfish.name = "Black fish";
        myfish.weight = 250.5;

        myfish.displayinf();
    }

}