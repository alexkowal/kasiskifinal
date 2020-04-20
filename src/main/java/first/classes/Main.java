package first.classes;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public class Main {
    public static String FILE_PATH;

    public static void main(String[] args) throws IOException, URISyntaxException {
        FILE_PATH = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI()).getPath();
        FILE_PATH = FILE_PATH.replace("kasiski.jar", "/");
//        FILE_PATH = "/Users/aleksandr/Desktop/kasiskifinal/src/main/java/first/files/";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("1 - сгенерировать ключ \n" +
                    "2 - перестановка текста \n" +
                    "3 - расшифровать перестановку \n" +
                    "4 - Подсчитать длину ключа методом Казиски и вывести первые подсчитанные 10 НОД \n" +
                    "5 - Расшифровать текст с помощью подсчитанного методом Казиски ключа \n");
            Integer mode = Integer.valueOf(br.readLine());
            PermCypher permCypher = new PermCypher();

            switch (mode) {
                case 1:
                    permCypher.generateKey();
                    break;
                case 2:
                    permCypher.permText();
                    break;
                case 3:
                    permCypher.decrypt();
                    break;
                case 0:
                    System.exit(0);
                    break;
                case 4: {
                    KasiskiExamination kasiskiExamination = new KasiskiExamination();
                    kasiskiExamination.calculate();
                    break;
                }
                case 5: {
                    KasiskiExamination kasiskiExamination = new KasiskiExamination();
                    kasiskiExamination.encryptText();
                    break;
                }
                default:
                    System.out.println("Выберите режим 1 - 3!");
            }
        }


    }
}
