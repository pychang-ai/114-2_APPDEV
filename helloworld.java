public class helloworld {
    public static void main(String [] args){
        printTriangle();
    }

    private static String str="";
    private static void printTriangle(){
        int i, j;
        for(i = 0; i <= 5; i++) {
            for ( j = 1; j <= i; j++){
                str += "*";
                str += "\n";
            }
        }
        System.out.println(str);
    }
}