import java.util.Scanner;

/** The scanner class used to easily get the keyboard input
 */
public class EasyScanner
{
    public static int nextInt()
    {
        Scanner sc = new Scanner(System.in);
        int i = sc.nextInt();
        return i;
    }

    public static long nextLong()
    {
        Scanner sc = new Scanner(System.in);
        long l = sc.nextLong();
        return l;
    }

    public static String nextString()
    {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        return s;
    }

    public static char nextChar()
    {
        Scanner sc = new Scanner(System.in);
        char c = sc.next().charAt(0);
        return c;
    }
}
