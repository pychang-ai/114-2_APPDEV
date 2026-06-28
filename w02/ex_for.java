public class ex_for {
    public static void main(String[] args) {
        String str = "";
        int total = 0;
        for (int i=0; i<=10; i++) {
            str += "|" + i;
            total += i;
        }
        System.out.println(str + "|=" + total);
    }
}