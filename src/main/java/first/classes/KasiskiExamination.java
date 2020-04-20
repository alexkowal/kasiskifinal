package first.classes;

import com.google.common.collect.Lists;
import javafx.util.Pair;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static first.classes.Main.FILE_PATH;
import static first.classes.PermUtils.permStringToDecrypt;
import static first.classes.PermUtils.splitTextByKeySize;
import static first.constants.KasiskiConstants.*;

class KasiskiExamination {
    private static final int digramLength = 5;

    private static Long gcd(Long a, Long b) {
        if (b == 0)
            return a;
        else return gcd(b, a % b);
    }

    void calculate() throws IOException {
        String text = readEncryptedText();
        List<Long> repeatCount = new ArrayList<>();
        for (int i = 0; i < text.length() - digramLength + 1; i++) {
            String temp = text.substring(i, i + digramLength);
            for (int j = i + 1; j < text.length() - digramLength + 1; j++) {
                String temp2 = text.substring(j, j + digramLength);
                if (temp.equals(temp2)) {
                    repeatCount.add((long) (j - i));
                }
            }
        }
        long[] nods = new long[5000];//массив для подсчета количества НОД
        for (int i = 0; i < repeatCount.size(); ++i)
            for (int j = i + 1; j < repeatCount.size(); ++j)
                nods[Math.toIntExact(gcd(repeatCount.get(i), repeatCount.get(j)))]++;
        nods[0] = 0; //загоняем все в новый массив, чтобы удобно отсортировать
        List<Pair<Long, Long>> ans = Lists.newArrayList();
        for (int i = 0; i < 500; ++i) {
            ans.add(new Pair(i, nods[i]));
        }
        ans.sort((o1, o2) -> {
            if (o1.getValue().equals(o2.getValue())) {
                return 0;
            }
            if (o1.getValue() > (o2.getValue())) {
                return -1;
            } else
                return 1;
        });
        writeTopTenNodes(ans);
        System.out.println("Первые 10 НОД в файле top-10-GCD.txt");
        writeKeyLength(ans);
    }

    void encryptText() throws IOException {
        String text = readEncryptedText();
        Long keyLength = readKeyLength();
        List<String> initPerm = new ArrayList<>();
        List<List<String>> exPerm = new ArrayList<>();
        for (int i = 0; i < keyLength; i++) {
            initPerm.add(i + "");
        }
        try (OutputStreamWriter bw = new OutputStreamWriter(new FileOutputStream(FILE_PATH + DECRYPT_KASISKI), StandardCharsets.UTF_8)) {
            while (exPerm.size() < calculateFactorial(keyLength)) {
                List<String> tmpPerm = Lists.newArrayList(initPerm);
                Collections.shuffle(tmpPerm, new Random());
                if (!permAlreadyExist(tmpPerm, exPerm)) {
                    exPerm.add(tmpPerm);
                    String decryptedText = decrypt(text, tmpPerm);
                    bw.write(tmpPerm.toString() + "\n");
                    bw.write("\n");
                    bw.write("\n");
                    bw.write(decryptedText);
                    bw.write("_______________________________");
                    bw.write("\n");
                    bw.write("\n");
                }
            }
        }
    }

    private boolean permAlreadyExist(List<String> newPerm, List<List<String>> exPerm) {
        for (List<String> strings : exPerm) {
            if (newPerm.equals(strings))
                return true;
        }
        return false;
    }

    private static Long calculateFactorial(Long n) {
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result = result * i;
        }
        return result;
    }

    public String decrypt(String textToDecrypt, List<String> perm) throws IOException {
        List<StringBuilder> splittedText = splitTextByKeySize(textToDecrypt, perm);
        List<String> result = new ArrayList<>();
        for (StringBuilder s : splittedText) {
            result.add(permStringToDecrypt(s, perm));
        }
        String res = "";
        for (String s : result) {
            res = res.concat(s);
        }
        return res;
    }


    private String readEncryptedText() throws IOException {
        List<String> r = Files.readAllLines(new File(FILE_PATH + ENCRYPTED_FILE_NAME).toPath(), StandardCharsets.UTF_8);
        String result = "";
        for (String s : r) {
            result = result.concat(s);
        }
        return result;
    }

    private Long readKeyLength() throws IOException {
        List<String> r = Files.readAllLines(new File(FILE_PATH + KEY_LENGTH_KASISKY).toPath(), StandardCharsets.UTF_8);
        return Long.valueOf(r.get(0));

    }


    private void writeTopTenNodes(List<Pair<Long, Long>> ans) throws IOException {
        try (OutputStreamWriter bw = new OutputStreamWriter(new FileOutputStream(FILE_PATH + TOP_TEN_NODES), StandardCharsets.UTF_8)) {
            bw.write("Format: Длина ключа - НОД \n");

            for (int i = 0; i < 10; i++) {
                bw.write(ans.get(i).getKey() + " - " + ans.get(i).getValue());
                bw.write("\n");
            }
        }
    }

    private void writeKeyLength(List<Pair<Long, Long>> ans) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH + KEY_LENGTH_KASISKY))) {
            bw.write(ans.get(0).getKey() + "");
        }
    }

}