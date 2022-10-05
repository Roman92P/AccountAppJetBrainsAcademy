import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String ipToCheck = scanner.nextLine();
        String rgx = "^([01]?[0-9][0-9]?|2[0-4][0-9]|25[0-5])\\.([01]?[0-9][0-9]?|2[0-4][0-9]|25[0-5])\\.([01]?[0-9][0-9]?|2[0-4][0-9]|25[0-5])\\.([01]?[0-9][0-9]?|2[0-4][0-9]|25[0-5])$";  
        System.out.print(ipToCheck.matches(rgx)? "YES" : "NO");
    }
}
