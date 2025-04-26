package Trylang;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Methods {
	private String strPass;

    private static List<Integer> unicodeValues = new ArrayList<>();

    public static List<Integer> getUnicode() {
        return unicodeValues;
    }
    
    public static int sum(List<Integer> list) {
    	int sum = 0;
    	for(int num : list) {
    		sum += num;
    	}
    	return sum;
    }

    public static void setUnicode(int value) {
        unicodeValues.clear();
        unicodeValues.add(value);
    }
   
    public static void unicode(String str) {
    	unicodeValues.clear(); 
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            int unicodeValue = (int) c;
            unicodeValues.add(unicodeValue); 
            System.out.println("Character: " + c + ", Unicode Value: " + unicodeValue);
        }
    }
    public static String convert(String password) {
    	unicode(password);
    	List<Integer> val2 = getUnicode();
    	int sum2 = sum(val2);
    	double  intPassword = Math.pow(sum2, 10) * 15;
    	String strPass = String.valueOf(intPassword);
    	return strPass;

    }
    /*public static void main(String [] args) {
    	Scanner sc = new Scanner(System.in);
    	final String str1 = "hello";
    	unicode(str1);
    	List<Integer> val1 = getUnicode();
    	int sum1 = sum(val1);
    	double  password1 = Math.pow(sum1, 10) * 15;
    	System.out.println(sum1);
    	System.out.println(password1);
    	String strPass1 = String.valueOf(password1);
    	System.out.println(strPass1);
    	
    	
    	String strPass = convert("hello");
  
    	
    	if(strPass.equals(strPass1)) {
    		System.out.print("equals");
    	}
    	sc.close();
    	
   }
*/
}
