import java.util.HashMap;
import java.util.Map;

/*
   Time Complexity:  O(n) = O(m) + O(m) + O(n-m)·O(1)
                     (1) O(m) to populate patternCountMap
                     (2) O(m) to build the first window
                     (3) n-m slides, O(1) each (one remove, one add, equals on ≤26 keys)
   Space Complexity: O(1) — both maps hold at most 26 keys (lowercase alphabet); no substrings
 */
public class AnagramsSlidingWindow {
    /* Given two strings, s and t, both consisting of lowercase English letters, return the number of substrings in s that are anagrams of t.
    Example: s = "caabab", t = "aba"
    Output: 2
     */
    static int getNumberOfAnagrams(String input, String pattern) {
        int count = 0;
        if (input == null || pattern == null || input.isEmpty() || pattern.isEmpty()) return count;
        if (input.length() < pattern.length()) return count;

        int n = input.length(), m = pattern.length();

        // count the pattern once
        Map<Character, Integer> patternCountMap = new HashMap<>();
        for (char ch : pattern.toCharArray()) {
            patternCountMap.merge(ch, 1, Integer::sum);
        }

        // count the first window of input once
        Map<Character, Integer> inputCountMap = new HashMap<>();
        for (int i = 0; i < m; i++) {
            inputCountMap.merge(input.charAt(i), 1, Integer::sum);
        }
        if (inputCountMap.equals(patternCountMap)) count++;

        // slide: each step is O(1)
        for (int leftIndex = 1; leftIndex <= n - m; leftIndex++) {
            char out = input.charAt(leftIndex - 1);          // leaving (old left)
            if (inputCountMap.merge(out, -1, Integer::sum) == 0) {
                inputCountMap.remove(out);                   // keep equals() accurate
            }
            char in = input.charAt(leftIndex + m - 1);       // entering (new right end)
            inputCountMap.merge(in, 1, Integer::sum);

            if (inputCountMap.equals(patternCountMap)) count++;
        }

        return count;
    }


    static void check(String input, String anagram, int expectCount) {
        int got = getNumberOfAnagrams(input, anagram);
        if (got != expectCount) {
            System.out.println("Failed: Input:" + input + " anagram:" + anagram + " got " + got + " expectCount " + expectCount);
        }else {
            System.out.println("Passed: Input:" + input + " anagram:" + anagram + " got " + got + " matches expectCount " + expectCount);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== getNumberOfAnagrams ===");
        check("caabab", "aba", 2);
        check("abab", "ab", 3);          // last window
        check("aba", "aab", 1);          // n == m
        check("a", "ab", 0);             // pattern longer than input
        check("aaaa", "aa", 3);          // overlapping repeats
        check("cbaebabacd", "abc", 2);
        check("", "a", 0);              // empty input
        check("abcabc", "abc", 4);       // counts hit 0 and get removed
        check("baa", "aab", 1);          // match only at the end
    }
}