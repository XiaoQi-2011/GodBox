import java.io.Serializable;

public class Test {
    public static void main(String[] args) {
        int a = 10;
        Serializable b = a;
        System.out.println(b.getClass().getName());
    }
}
