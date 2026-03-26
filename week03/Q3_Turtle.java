public class Q3_Turtle{
    String species;
    int age;

    public Q3_Turtle(String species, int age){
        this.species = species;
        this.age = age;
    }

    public void showDetail(){
         System.out.println("品種：" + species + "，年紀："+ age);
    }

    public static void main(String [] args){
      Q3_Turtle myTurtle = new Q3_Turtle("綠蠵龜", 50);
      myTurtle.showDetail();

    }

}