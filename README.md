# coding-patterns

Java solutions to problems from the book *Coding Interview Patterns*, grouped by the technique each one uses. I'm adding to it as I work through the book, mainly as a personal reference. I hope it's useful to others too.

Each solution:
- notes its **time and space complexity** in a header comment, along with any caveats (for example, "modifies the input in place")
- includes a `main` method with built-in test cases that print PASS or FAIL when you run it
- sometimes comes in more than one version so the trade-offs are easy to compare (recursive vs. iterative, extra space vs. none, top-down vs. bottom-up)

## Patterns covered

| Pattern | Solutions |
|---|---|
| Two pointers | `PairSum`, `TripletSum`, `LargestContainer` |
| Hash maps & sliding window | `Anagrams`, `AnagramsSlidingWindow` |
| Prefix sums | `PrefixSum`, `SubarraySumEqualsK`, `ProductOfArrayExceptSelfUsingExtraSpace`, `ProductOfArrayExceptSelfUsingNoExtraSpace` |
| Trees (DFS / BFS) | `InvertTreeUsingRecursion`, `InvertTreeUsingIteration`, `BalancedBinaryTreeValidationTopDown`, `BalancedBinaryTreeValidationBottomUp`, `BinarySearchTreeValidationUsingRecursion`, `LowestCommonAncestorUsingRecursion`, `BinaryTreeLeftSideViewUsingLevelOrder`, `BinaryTreeRightSideViewUsingLevelOrder`, `MaximumWidthOfBinaryTree` |
| Graphs | `NumberOfIslandsUsingRecursion`, `RottenOrangesUsingMultisourceBFS`, `DeepCopyOfGraphWithRecursion` |
| Math | `ReverseInteger`, `IsIntegerPalindrome` |

## Running

Requires Java 17 or later. No build tool is needed:

```bash
cd src
javac RottenOrangesUsingMultisourceBFS.java
java RottenOrangesUsingMultisourceBFS
```

## Note

The problem statements belong to the book and aren't reproduced here. The solutions are my own.