package week03;
public class ex_total {
    public static void main(String[] args) {
        int total = 0;
        int a=1 , b=10;
        total = sumN1N(a, b);
        System.out.print(a + "~" + b + " total=" + total);
    }
    static int sumN1N(int begin, int end) {
        int i=0;
        int total=0;
        for(i=begin ; i<=end ; i++) {
            total += i;
        }
        return total;
    }
}
