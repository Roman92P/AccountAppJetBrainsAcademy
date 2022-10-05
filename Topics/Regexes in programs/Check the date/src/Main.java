import java.util.*;

class Date {

    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        String date = scn.nextLine();
        String dateRegex = "^[0-9]{2,4}([\\.-\\/])(0?[1-9]|1[0-2])([\\.-\\/])[0-9]{2,4}$";
        String result = date.matches(dateRegex)? "Yes" : "No";
        System.out.print(result);
    }
}
