public class w031201{
    public static void main(String[]args){
        System.out.println("helloworld");
        printTriangle();
    }
    private static String str="";
    private static void printTriangle(){
        int i,j;
        for (i=1;i<=5;i++){
            for(j=1;j<=i;j++){
                str += "*";
                str += "\n";
            }
        }
        System.out.println(str);
    }
}