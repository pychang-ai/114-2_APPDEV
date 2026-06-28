package week03;

public class Triangle {
    public static void main(String[] args) {
        printTriangle();
    }
    private static String str="";
    public static void printTriangle() {
        int i,j;
        for(i=1 ;i<=5 ;i++) {
            for(j=1 ;j<=i ;j++) {
                str += "* ";
            }
            str += "\n";
        }
        System.out.print(str);
    }
}
