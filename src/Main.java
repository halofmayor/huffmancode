
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String text = Utils.readFile("src/Original_Text.txt");

        HuffmanNode huffmanTree = HuffmanTree.buildHuffmanTree(Utils.countCharacters(text));
        Map<Character, String> codes = HuffmanTree.generateHuffmanCodes(huffmanTree);

        Utils.compressToBinaryFile(text, codes, "src/Compressed_Text.bin");
        Utils.decompressBinaryFile("src/Compressed_Text.bin", "src/Decompressed_Text.txt");

        System.out.println(Utils.areFilesEqual("src/Original_Text.txt", "src/Decompressed_Text.txt"));
    }

}
