public class Q3_Turtle{
    String species;
    int age;

    public Q3_Turtle(String species, int age){
        this.species = species;
        this.age = age;
    }

    public void showDetails(){
        System.out.println("This turtle's species is :" + species + " age:" + age);
    }
    public static void main(String [] args){
        Q3_Turtle myTurtle = new Q3_Turtle("Green Turtle",50);
        myTurtle.showDetails();
    }
}