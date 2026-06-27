package week04.week04;

public class Dog {

    String name;
    String breed;
    int age;
    String color;

    public Dog(String name, String breed, int age, String color) {
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.color = color;
    }

    public String getName() { return name; }
    public String getBreed() { return breed; }
    public int getAge() { return age; }
    public String getColor() { return color; }

    @Override
    public String toString() {
        return "Name is: " + name
             + "\nBreed age and color are: "
             + breed + " " + age + " " + color;
    }

    public static void main(String[] args) {
        Dog tuffy = new Dog("tuffy", "papillon", 5, "white");
        System.out.println(tuffy);
    }
}
