import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Utils {
    public static String readFile(String filename){
        File myObj = new File(filename);
        StringBuilder text = new StringBuilder();
        try (Scanner myReader = new Scanner(myObj)) {
            while (myReader.hasNextLine()) {
                text.append(myReader.nextLine());
                if (myReader.hasNextLine()) text.append('\n');
            }

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return text.toString();
    }

    public static Map<Character, Integer> countCharacters(String s){
        Map<Character, Integer> charCount = new HashMap<Character, Integer>();
        for (char ch : s.toCharArray()){
            charCount.put(ch, charCount.containsKey(ch) ? ((charCount).get(ch) + 1) : 1);
        }
        return charCount;
    }

    public static void compressToBinaryFile(String text, Map<Character, String> codes, String filename) {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(filename))) {

            out.writeInt(codes.size());

            for (Map.Entry<Character, String> entry : codes.entrySet()) {
                out.writeChar(entry.getKey());
                out.writeUTF(entry.getValue());
            }

            StringBuilder bitString = new StringBuilder();
            for (char c : text.toCharArray()) {
                bitString.append(codes.get(c));
            }

            int padding = 8 - (bitString.length() % 8);
            if (padding != 8) {
                bitString.append("0".repeat(padding));
            }

            out.writeInt(padding);

            for (int i = 0; i < bitString.length(); i += 8) {
                int byteValue = Integer.parseInt(bitString.substring(i, i + 8), 2);
                out.write(byteValue);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void decompressBinaryFile(String inputFilename, String outputFilename) {
        try (DataInputStream in = new DataInputStream(new FileInputStream(inputFilename))) {

            int tableSize = in.readInt();
            Map<String, Character> codeToChar = new HashMap<>();

            for (int i = 0; i < tableSize; i++) {
                char key = in.readChar();
                String code = in.readUTF();
                codeToChar.put(code, key);
            }

            int padding = in.readInt();

            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            byte[] buffer = in.readAllBytes();
            byteBuffer.write(buffer);

            StringBuilder bitString = new StringBuilder();
            for (byte b : byteBuffer.toByteArray()) {
                bitString.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
            }

            if (padding != 8)
                bitString.setLength(bitString.length() - padding);

            StringBuilder decoded = new StringBuilder();
            String temp = "";
            for (char bit : bitString.toString().toCharArray()) {
                temp += bit;
                if (codeToChar.containsKey(temp)) {
                    decoded.append(codeToChar.get(temp));
                    temp = "";
                }
            }

            try (FileWriter fw = new FileWriter(outputFilename)) {
                fw.write(decoded.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean areFilesEqual(String file1, String file2) {
        try {
            byte[] f1 = java.nio.file.Files.readAllBytes(java.nio.file.Path.of(file1));
            byte[] f2 = java.nio.file.Files.readAllBytes(java.nio.file.Path.of(file2));

            boolean equal = Arrays.equals(f1, f2);
            if (!equal) {
                System.out.println("Different files. Sizes: " + f1.length + " vs " + f2.length);
            }
            return equal;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
