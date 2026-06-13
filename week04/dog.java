public class dog {
    String name;
    String breed;
    int age;
    String color;


    public dog(String name, String breed, int age, String color) 
   {
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.color = color;
    }
    public String getName() { return name; }
    public String getBreed() { return name; }
    public String getAge() { return name; }
    public String getColor() { return name; }

      @Override
    public String toString() {
        return "Name is: " + name
             + "\nBreed age and color are: "
             + breed + " " + age + " " + color;
    }
    public static void main(String[] args)
    {
        dog tuffy = new dog("tuffy","papillon", 3, "white");
        System.out.println(tuffy);
    }
}    