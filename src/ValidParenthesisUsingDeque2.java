import java.util.*;

/**
 * LeetCode 20 - Valid Parentheses
 *
 * Time Complexity:  O(n) - each character is visited once; push/pop and
 *                   Set/Map lookups are O(1).
 * Space Complexity: O(n) - in the worst case (all opening brackets) the
 *                   deque holds n characters.
 */
public class ValidParenthesisUsingDeque2 {

    private static final Map<Character, Character> PAIRS = Map.of('(', ')', '{', '}', '[', ']');

    public boolean isValid(String s) {
        if (s == null) return true;
        if (s.length() % 2 != 0) return false;

        Deque<Character> stack = new ArrayDeque<>();
        for (char ch : s.toCharArray()) {
            if (PAIRS.containsKey(ch)) {
                stack.push(ch);
            } else if (stack.isEmpty() || ch != PAIRS.get(stack.pop())) {
                return false;
            }
        }
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        ValidParenthesisUsingDeque2 solver = new ValidParenthesisUsingDeque2();

        String[] inputs = {
            "()",        // example 1
            "()[]{}",    // example 2
            "(]",        // example 3
            "([)]",      // wrong order
            "{[]}",      // nested
            "",          // empty
            "(",         // single open
            ")",         // single close
            "((",        // unclosed opens, even length
            "))((",      // closes before opens
            "{[()()]}",  // deeper nesting
            "[{]}"       // interleaved mismatch
        };
        boolean[] expected = {
            true, true, false, false, true, true,
            false, false, false, false, true, false
        };

        int passed = 0;
        for (int i = 0; i < inputs.length; i++) {
            boolean actual = solver.isValid(inputs[i]);
            boolean ok = actual == expected[i];
            if (ok) passed++;
            System.out.printf("%-4s %-12s expected=%-5b actual=%-5b%n",
                    ok ? "PASS" : "FAIL", "\"" + inputs[i] + "\"", expected[i], actual);
        }
        System.out.printf("%n%d/%d tests passed%n", passed, inputs.length);
    }
}
