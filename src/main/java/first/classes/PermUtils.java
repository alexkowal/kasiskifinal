package first.classes;

import java.util.ArrayList;
import java.util.List;

public class PermUtils {

    public static List<StringBuilder> splitTextByKeySize(String text, List<String> key) {
        List<StringBuilder> slittedText = new ArrayList<>();
        int count = text.length() / key.size();
        for (int i = 0; i < count; i++) {
            slittedText.add(new StringBuilder(text.substring(0, key.size())));
            text = text.substring(key.size());
        }
        if (text.length() > 0) {
            if (text.length() < key.size()) {
                while (text.length() < key.size()) {
                    text = text.concat("_");
                }
            }
            slittedText.add(new StringBuilder(text));
        }
        return slittedText;
    }


    public static String permStringToDecrypt(StringBuilder string, List<String> perm) {
        int pos = 0;
        List<Character> list = new ArrayList<>();
        for (int i = 0; i < string.length(); i++)
            list.add(' ');
        for (String s : perm) {
            list.set(pos, string.charAt(Integer.parseInt(s)));
            pos++;
        }
        String finalString = "";
        for (Character character : list) {
            finalString = finalString.concat(String.valueOf(character));
        }
        return finalString;
    }

}
