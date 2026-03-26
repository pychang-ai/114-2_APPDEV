public class w031202{
    public static void main(String[]args){
        System.out.println("helloworld");
        //printTriangle();
        int total=0;
        total = sumN2N(1,10);
        System.out.println( "total: " + total);
    }
    static int sumN2N(int begin,int end){
        int i=0;
        int total=0;
        for (i=begin; i<=end; i++){
            total+=1;
        }
        return total;
    }

}