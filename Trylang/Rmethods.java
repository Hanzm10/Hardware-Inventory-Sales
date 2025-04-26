package Trylang;

import java.util.List;
import java.util.Scanner;


public class Rmethods {
    private String password;

    public Rmethods(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public static String convert(String password) {
        Unicode.unicode(password);
        List<Integer> val2 = Unicode.getUnicode();
        int sum2 = Unicode.sum(val2);
        double intPassword = Math.pow(sum2, 10) * 15;
        return String.valueOf(intPassword);
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Password: ");
        String rawPassword = sc.nextLine();
        String hashed = Rmethods.convert(rawPassword);
        System.out.println("Final Hashed Password: " + hashed);
        }
    }


