public class CommandLineArgs {
    public static void main(String[] args) {
        System.out.println("Arguments received:");
        for (String arg : args) {
            System.out.println(arg);
        }
    }
}