package week04;

public class Geeks {
    // An example method
    public void printMessage() {
        System.out.println("Hello, Geeks!");
    }
    void hello() {
        System.out.println("This is a user-defined method.");
    }
    
    public static void main(String[] args) {
        
        // Create an instance of the class
        // containing the method
        Geeks obj = new Geeks();
        
        // Calling the method
        obj.printMessage(); 
        obj.hello();
    }
}