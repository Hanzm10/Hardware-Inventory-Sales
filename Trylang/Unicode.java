package Trylang;

import java.util.ArrayList;
import java.util.List;

public class Unicode {
	    private static List<Integer> unicodeValues = new ArrayList<>();

	    public static List<Integer> getUnicode() {
	        return unicodeValues;
	    }

	    public static int sum(List<Integer> list) {
	        int sum = 0;
	        for (int num : list) {
	            sum += num;
	        }
	        return sum;
	    }

	    public static void unicode(String str) {
	        unicodeValues.clear();
	        for (int i = 0; i < str.length(); i++) {
	            char c = str.charAt(i);
	            int unicodeValue = (int) c;
	            unicodeValues.add(unicodeValue);
	            //System.out.println("Character: " + c + ", Unicode Value: " + unicodeValue);
	        }
	    }

}
