class IsIntegerPalindrome {
    public boolean isPalindrome(int x) {

        if (x ==0) return true;
        if ( x < 0 || x%10 ==0) return false;

        int reversedX = 0;

        while (x > reversedX){
            reversedX = reversedX* 10 + x%10;
            x = x/10;
        }

        return x == reversedX || x == reversedX/10;
    }

    public static void main(String[] args) {
        IsIntegerPalindrome p = new IsIntegerPalindrome();

        int[] inputs = {
                0, 7, 11, 121, 1221, 12321, 1000021,      // palindromes / non-palindrome lookalike
                -121, -1,                                  // negatives
                10, 100, 1210,                             // trailing zeros
                123, 12, 1231,                             // plain non-palindromes
                Integer.MAX_VALUE,                         // 2147483647 - not a palindrome
                Integer.MIN_VALUE,                         // negative extreme
                2147447412                                 // near MAX, is a palindrome
        };
        boolean[] expected = {
                true, true, true, true, true, true, false,
                false, false,
                false, false, false,
                false, false, false,
                false,
                false,
                true
        };

        int passed = 0;
        for (int i = 0; i < inputs.length; i++) {
            boolean actual = p.isPalindrome(inputs[i]);
            boolean ok = actual == expected[i];
            if (ok) passed++;
            System.out.printf("%-12d expected=%-5b actual=%-5b %s%n",
                    inputs[i], expected[i], actual, ok ? "PASS" : "FAIL");
        }
        System.out.println(passed + "/" + inputs.length + " passed");
    }
}