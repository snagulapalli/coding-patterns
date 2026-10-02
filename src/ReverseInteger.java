class ReverseInteger {
    public int reverse(int x) {

        int reversedX =0;
        while (x != 0){

            int digit = x % 10;

            if (reversedX > Integer.MAX_VALUE / 10 ||
                    (reversedX == Integer.MAX_VALUE / 10 && digit > Integer.MAX_VALUE % 10)) return 0;
            if (reversedX < Integer.MIN_VALUE / 10 ||
                    (reversedX == Integer.MIN_VALUE / 10 && digit < Integer.MIN_VALUE % 10)) return 0;

            reversedX = reversedX * 10 + digit;

            x = x/10;
        }
        return reversedX;
    }

    public static void main(String[] args) {
        ReverseInteger r = new ReverseInteger();
        System.out.println("Input:" + Integer.MAX_VALUE + " reversed:" + r.reverse(Integer.MAX_VALUE));
        System.out.println("Input:123 reversed:" + r.reverse(123));
        System.out.println("Input:-123 reversed:" + r.reverse(-123));
        System.out.println("Input:120 reversed:" + r.reverse(120));
    }

}