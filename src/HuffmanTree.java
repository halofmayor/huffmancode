import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanTree {
    public static HuffmanNode buildHuffmanTree(Map<Character, Integer> freqMap) {
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();

        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            pq.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();

            HuffmanNode parent = new HuffmanNode(left.freq + right.freq, left, right);
            pq.add(parent);
        }

        return pq.poll();
    }

    public static Map<Character, String> generateHuffmanCodes(HuffmanNode root) {
        Map<Character, String> codes = new HashMap<>();
        generateCodesHelper(root, "", codes);
        return codes;
    }

    private static void generateCodesHelper(HuffmanNode node, String code, Map<Character, String> codes) {
        if (node != null) {
            if (node.isLeaf()) {
                codes.put(node.ch, code);
            }
            generateCodesHelper(node.left, code + "0", codes);
            generateCodesHelper(node.right, code + "1", codes);
        }
    }

}
