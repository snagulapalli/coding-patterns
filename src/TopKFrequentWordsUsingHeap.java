import java.util.*;

/**
 * Top K Frequent Words (LeetCode 692) using a size-k min-heap.
 *
 * Mnemonic: Rank it. Reverse it. Reverse it.
 *   1. Rank it    -> write `best` comparator best-first (like the problem statement)
 *   2. Reverse it -> heap uses best.reversed() so the WORST word sits at the root
 *   3. Reverse it -> heap drains worst-first, so reverse the result list
 *
 * Time:  O(n + m log k)  -- O(n) counting, O(m log k) heap over m distinct words
 * Space: O(m + k)        -- map of m distinct words + heap of size k
 */
public class TopKFrequentWordsUsingHeap {

    public static List<String> topKFrequent(String[] words, int k) {
        // Count
        Map<String, Integer> freq = new HashMap<>();
        for (String word : words) {
            freq.merge(word, 1, Integer::sum);
        }

        // Rank it: best-first
        Comparator<String> best = (a, b) -> {
            int freqA = freq.get(a);
            int freqB = freq.get(b);
            if (freqA != freqB) return Integer.compare(freqB, freqA); // higher freq first
            return a.compareTo(b);                                     // then dictionary order
        };

        // Reverse it: worst word at the root, so it gets evicted
        PriorityQueue<String> heap = new PriorityQueue<>(best.reversed());
        for (String w : freq.keySet()) {          // distinct words only, not `words`
            heap.offer(w);
            if (heap.size() > k) heap.poll();     // evict the worst
        }

        // Reverse it: heap drains worst-first
        List<String> result = new ArrayList<>(k);
        while (!heap.isEmpty()) {
            result.add(heap.poll());
        }
        Collections.reverse(result);
        return result;
    }

    // ---------------- Test harness ----------------

    private static int passed = 0, failed = 0;

    private static void check(String name, String[] words, int k, List<String> expected) {
        List<String> actual = topKFrequent(words, k);
        boolean ok = actual.equals(expected);
        if (ok) passed++; else failed++;
        System.out.printf("%s %-32s k=%d  expected=%s  actual=%s%n",
                ok ? "PASS" : "FAIL", name, k, expected, actual);
    }

    public static void main(String[] args) {
        // LeetCode example 1
        check("LC example 1",
                new String[]{"i", "love", "leetcode", "i", "love", "coding"}, 2,
                List.of("i", "love"));

        // LeetCode example 2
        check("LC example 2",
                new String[]{"the", "day", "is", "sunny", "the", "the", "the", "sunny", "is", "is"}, 4,
                List.of("the", "is", "sunny", "day"));

        // Frequency tie -> dictionary order decides
        check("Tie broken alphabetically",
                new String[]{"banana", "apple", "banana", "apple", "cherry"}, 1,
                List.of("apple"));

        // All words same frequency -> pure dictionary order
        check("All same frequency",
                new String[]{"delta", "alpha", "charlie", "bravo"}, 3,
                List.of("alpha", "bravo", "charlie"));

        // k equals number of distinct words -> everything returned, fully ordered
        check("k == distinct count",
                new String[]{"b", "a", "b", "c", "c", "c"}, 3,
                List.of("c", "b", "a"));

        // Single word
        check("Single word",
                new String[]{"solo"}, 1,
                List.of("solo"));

        // The duplicate trap: looping over `words` instead of keySet would give [i, i]
        check("Duplicates (keySet matters)",
                new String[]{"i", "i", "love"}, 2,
                List.of("i", "love"));

        System.out.printf("%n%d passed, %d failed%n", passed, failed);
    }
}
