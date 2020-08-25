package com.shivsoftech.util;

import java.util.HashMap;
import java.util.Map;

import static com.shivsoftech.util.Constants.EMPTY_STRING;

public class CharMap {

    private static Map<String, String> map = null;

    static {

        map = new HashMap<>();

        map.put(" ", "0001");
        map.put("&", "0007");

        map.put(",", "000D");

        map.put("0", "0011");
        map.put("1", "0012");
        map.put("2", "0013");
        map.put("3", "0014");
        map.put("4", "0015");
        map.put("5", "0016");
        map.put("6", "0017");
        map.put("7", "0018");
        map.put("8", "0019");
        map.put("9", "001A");

        map.put("A", "0022");
        map.put("B", "0023");
        map.put("C", "0024");
        map.put("D", "0025");
        map.put("E", "0026");
        map.put("F", "0027");
        map.put("G", "0028");
        map.put("H", "0029");
        map.put("I", "002A");
        map.put("J", "002B");
        map.put("K", "002C");
        map.put("L", "002D");
        map.put("M", "002E");
        map.put("N", "002F");
        map.put("O", "0030");
        map.put("P", "0031");
        map.put("Q", "0032");
        map.put("R", "0033");
        map.put("S", "0034");
        map.put("T", "0035");
        map.put("U", "0036");
        map.put("V", "0037");
        map.put("W", "0038");
        map.put("X", "0039");
        map.put("Y", "003A");
        map.put("Z", "003B");

        map.put("[", "003C");
        map.put("]", "003E");

        map.put("a", "0042");
        map.put("b", "0043");
        map.put("c", "0044");
        map.put("d", "0045");
        map.put("e", "0046");
        map.put("f", "0047");
        map.put("g", "0048");
        map.put("h", "0049");
        map.put("i", "004A");
        map.put("j", "004B");
        map.put("k", "004C");
        map.put("l", "004D");
        map.put("m", "004E");
        map.put("n", "004F");
        map.put("o", "0050");
        map.put("p", "0051");
        map.put("q", "0052");
        map.put("r", "0053");
        map.put("s", "0054");
        map.put("t", "0055");
        map.put("u", "0056");
        map.put("v", "0057");
        map.put("w", "0058");
        map.put("x", "0059");
        map.put("y", "005A");
        map.put("z", "005B");

    }

    public static String get(char character) {
        return map.get(Character.toString(character));
    }

    public static String get(String text) {

        if (text == null) {
            return null;
        }

        if (Utility.isEmpty(text)) {
            return EMPTY_STRING;
        }

        StringBuilder sb = new StringBuilder();

        for (char ch : text.toCharArray()) {
            sb.append(CharMap.get(ch));
        }

        return sb.toString();
    }

    public static void main(String... args) {

        System.out.println("A = "+CharMap.get('A'));

        String text = "August 23, 2020";
        //text = "[JFMASOND 1234567890 abcdefghijklmnopqrstuvwxyz]";
        //text = "In the matrix above,";
        System.out.println("Text = "+CharMap.get(text));

        String[] splitedText = text.split(",");
        for (int index=0; index<splitedText.length; index++) {
            System.out.println("Index : "+index+" Text : "+splitedText[index]);
        }
    }
}
