package account.app.utils;

public class SalaryLongToStringConverter {
    public static <T>  String convertSalary(T salaryNum) {
        String s = String.valueOf(salaryNum);
        int length = s.length();
        if (length <= 2) {
            return String.format("0 dollar(s) %s cent(s)", s);
        }
        return String.format("%s dollar(s) %s cent(s)", s.substring(0, s.length()-2), s.substring(s.length()-2, s.length()));
    }
}
