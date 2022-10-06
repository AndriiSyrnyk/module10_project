import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class Module10Test {
    public static void main(String[] args) {
        String phoneNumberPath = "C:\\Users\\38098\\IdeaProjects\\Module10Project\\src\\main\\resources\\phoneNumber.txt";
        String inJSONFile = "C:\\Users\\38098\\IdeaProjects\\Module10Project\\src\\main\\resources\\user.txt";
        String outJSONFile = "C:\\Users\\38098\\IdeaProjects\\Module10Project\\src\\main\\resources\\user.json";
        String wordsFile = "C:\\Users\\38098\\IdeaProjects\\Module10Project\\src\\main\\resources\\words.txt";

        readPhoneNumberFromFile(phoneNumberPath);
        toJSONfile(inJSONFile, outJSONFile);
        countTheWords(wordsFile);
    }
    public static void readPhoneNumberFromFile(String filePath) {
        File file = new File(filePath);

        if (file.exists()) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                String line = bufferedReader.readLine().trim();

                while (line != null) {
                    if (isPhoneNumberValid(line))
                        System.out.println(line);
                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    private static boolean isPhoneNumberValid(String line) {
        return Pattern.matches("(\\d{3}-\\d{3}\\-\\d{4})", line)
                || Pattern.matches("(\\(\\d{3}\\) \\d{3}\\-\\d{4})", line);
    }

    public static void toJSONfile(String inFile, String outFile) {
        File inputFile = new File(inFile);
        File outputFile = new File(outFile);
        Queue<User> users = new LinkedList<>();

        if (inputFile.exists()) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile))) {
                String line = bufferedReader.readLine();
                line = bufferedReader.readLine();

                while (line != null) {
                    String[] arrLine = line.split(" ");
                    if (arrLine.length == 2) {
                        String name = line.split(" ")[0];
                        int age = Integer.valueOf(line.split(" ")[1]);
                        users.add(new User(name, age));
                    }
                    line = bufferedReader.readLine();
                }

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile))) {
                bufferedWriter.write("[\n");

                while (!users.isEmpty()) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String element = "\t" + gson.toJson(users.poll()).replaceAll("\n", "\n\t");
                    bufferedWriter.write(element);
                    if (!users.isEmpty())
                        bufferedWriter.write(",\n");
                }

                bufferedWriter.write("\n]");

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static void countTheWords(String inFile) {
        File inputFile = new File(inFile);
        HashMap<String, Integer> wordsCounter = new HashMap<>();

        if (inputFile.exists()) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile))) {
                String line = bufferedReader.readLine();

                while (line != null) {
                    String[] wordsInLine = line.split("\\s");

                    if (wordsInLine.length > 0)
                        for (String word: wordsInLine)
                            wordsCounter.compute(word, (k, v) -> v == null ? 1 : v + 1);

                    line = bufferedReader.readLine();
                }

                List<Map.Entry<String, Integer>> words = new LinkedList<>(wordsCounter.entrySet());
                Collections.sort(words, (e1, e2) -> (e2.getValue().compareTo(e1.getValue())));

                for (Map.Entry entry: words)
                    System.out.println(entry.getKey() + " " + entry.getValue());
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
