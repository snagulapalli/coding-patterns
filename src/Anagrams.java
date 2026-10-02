import java.util.HashMap;
import java.util.Map;

/*
   Time Complexity: O(n*m)= O(m)+O(n-m+1)*O(m) : (1) O(m) to populate patternCountMap (2) loop runs n-m+1 times, O(m) each
   Space Complexity:  O(m)- window substring; maps are bounded by the length of pattern (m)
 */
public class Anagrams {
    /* Given two strings, s and t, both consisting of lowercase English letters, return the number of substrings in s that are anagrams of t.
    Example: s = "caabab", t = "aba"
    Output: 2
     */
    static int getNumberOfAnagrams(String input, String pattern) {
        int count = 0;

        if (input == null || pattern == null || input.isEmpty() || pattern.isEmpty()) return count;
        if (input.length() < pattern.length()) return count;

        Map<Character, Integer> patternCountMap = new HashMap<>();
        for (char ch : pattern.toCharArray()) {
            patternCountMap.put(ch, patternCountMap.getOrDefault(ch, 0) + 1);
        }

        Map<Character, Integer> inputCountMap = new HashMap<>();
        int leftIndex = 0;
        while (leftIndex <= input.length() - pattern.length()  ) {
                String window = input.substring(leftIndex, leftIndex + pattern.length());
                for (Character ch : window.toCharArray()) {
                    inputCountMap.put(ch, inputCountMap.getOrDefault(ch, 0) + 1);
                }
                if (inputCountMap.equals(patternCountMap)) {
                    count++;
                }
                inputCountMap.clear();
                leftIndex = leftIndex + 1;
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
        check("", "a", 0);// empty input
    }
}