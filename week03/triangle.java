public class triangle {
    public static void main(String [] args){
        System.out.println("helloworld");
        //printriangle();
        int total=0;
        total= sumN2N(1,10);
        System.out.println("total"+total);
        
        }

    static int sumN2N(int begin,int end){
        int i=0;
        int total=0;
        for(i=begin;i<=end;i++){
            total+=i;
        }
        return total;
    }
    private static String str="";
    private static void printriangle(){
        int i,j;
        for(i=1;i<=5;j++){
            for(j=1;j<=i;j++){
                str+="*";
                str+="\n";

            }
        }
    }
}