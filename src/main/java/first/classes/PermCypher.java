package first.classes;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static first.classes.Main.FILE_PATH;
import static first.classes.PermUtils.permStringToDecrypt;
import static first.classes.PermUtils.splitTextByKeySize;
import static first.constants.KasiskiConstants.*;


class PermCypher {

    public void generateKey() throws IOException {
        Long keyLength = readKeyLength();
        System.out.println("Generate key");
        StringBuilder initialKey = new StringBuilder();
        for (int i = 0; i < keyLength; i++) {
            initialKey = initialKey.append(i).append(" ");
        }
        List<String> perm = Arrays.asList(initialKey.toString().split(" "));
        Collections.shuffle(perm);
        writeKey(perm);
    }

    public void permText() throws IOException {
        String textToEncrypt = readText();
        List<String> perm = readKeyFromFile();
        compareKeyAndText(textToEncrypt, perm);
        List<StringBuilder> splittedText = splitTextByKeySize(textToEncrypt, perm);
        System.out.println("Start text permutation");
        List<String> result = new ArrayList<>();
        for (StringBuilder s : splittedText) {
            result.add(permString(s, perm));
        }
        System.out.println("Permutation finished");
        writeEncryptedText(result);
        System.out.println("DONE!");
    }

    public void decrypt() throws IOException {
        List<String> perm = readKeyFromFile();
        String textToDecrypt = readEncryptedText();
        List<StringBuilder> splittedText = splitTextByKeySize(textToDecrypt, perm);
        List<String> result = new ArrayList<>();
        for (StringBuilder s : splittedText) {
            result.add(permStringToDecrypt(s, perm));
        }

        System.out.println("Permutation finished");
        writeDecryptedText(result);
        System.out.println("DONE!");
    }


    private List<String> readKeyFromFile() throws IOException {
        String key;
        List<String> r = Files.readAllLines(new File(FILE_PATH + KEY_FILE_NAME).toPath(), StandardCharsets.UTF_8);
        key = r.get(0);
        return Arrays.asList(key.split(" "));
    }

    private Long readKeyLength() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH + TEXT_FILE_NAME))) {
            return Long.parseLong(br.readLine());
        }
    }

    private void writeKey(List<String> perm) throws IOException {
        System.out.println("Write key to file" + KEY_FILE_NAME);
        try (OutputStreamWriter bw = new OutputStreamWriter(new FileOutputStream(FILE_PATH + KEY_FILE_NAME), StandardCharsets.UTF_8)) {
            for (String s : perm) {
                bw.write(s + " ");
            }
        }
    }

    private String readText() throws IOException {
        List<String> r = Files.readAllLines(new File(FILE_PATH + TEXT_FILE_NAME).toPath(), StandardCharsets.UTF_8);
        r.remove(r.get(0));
        String result = "";
        for (String s : r) {
            result = result.concat(s);
        }
        return result;
    }

    private String readEncryptedText() throws IOException {
        List<String> r = Files.readAllLines(new File(FILE_PATH + ENCRYPTED_FILE_NAME).toPath(), StandardCharsets.UTF_8);
        String result = "";
        for (String s : r) {
            result = result.concat(s);
        }
        return result;
    }

    private void writeEncryptedText(List<String> text) throws IOException {
        System.out.println("Write encrypted text to file");
        String result = "";
        for (String s : text) {
            result = result.concat(s);
        }
        try (OutputStreamWriter bw = new OutputStreamWriter(new FileOutputStream(FILE_PATH + ENCRYPTED_FILE_NAME), StandardCharsets.UTF_8)) {
            bw.write(result);
        }
    }

    private void writeDecryptedText(List<String> text) throws IOException {
        System.out.println("Write encrypted text to file");
        String result = "";
        for (String s : text) {
            result = result.concat(s);
        }

        try (OutputStreamWriter bw = new OutputStreamWriter(new FileOutputStream(FILE_PATH + DECRYPTED_FILE_NAME), StandardCharsets.UTF_8)) {
            bw.write(normalizeString(result));
        }
    }

    private void compareKeyAndText(String text, List<String> key) {
        if (text.length() < key.size()) {
            while (text.length() < key.size()) {
                text = text.concat("_");
            }
        }
    }

    private String permString(StringBuilder string, List<String> perm) {
        int pos = 0;
        List<Character> list = new ArrayList<>();
        for (int i = 0; i < string.length(); i++)
            list.add(' ');
        for (String s : perm) {
            list.set(Integer.parseInt(s), string.charAt(pos));
            pos++;
        }
        String finalString = "";
        for (Character character : list) {
            finalString = finalString.concat(String.valueOf(character));
        }
        return finalString;
    }


    private String normalizeString(String s) {
        return s.replaceAll("_", "");
    }


}
