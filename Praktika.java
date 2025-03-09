public class CommandLineArgs {
    public static void main(String[] args) {
        System.out.println("Аргументи командного рядка:");

        if (args.length == 0) {
            System.out.println("Немає переданих аргументів.");
        } else {
            for (int i = 0; i < args.length; i++) {
                System.out.println("Аргумент " + i + ": " + args[i]);
            }
        }
    }
}