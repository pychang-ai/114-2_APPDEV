public class ex02 {
    public static void main(String[] args) {
        // alt + shift + f 自動格式化
        int grade = 80;
        char type = 'm';
        if(grade>60 && type=='m') {
            System.out.println("課程：" + type + "you can  pass 成績：" + grade);
        } else {
            System.out.println("課程不正確 or 不及格");
        }
    }
}