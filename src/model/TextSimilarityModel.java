package model;

import java.util.ArrayList;
import java.util.List;

public class TextSimilarityModel {

    // Holds the final LCS result.
    public static class LcsResult {
        public final int length;
        public final List<String> sequence;

        public LcsResult(int length, List<String> sequence) {
            this.length = length;
            this.sequence = sequence;
        }
    }

    public static class ComparisonResult {
        public double similarity;
        public LcsResult lcsResult;
        public int tokens1Length;
        public int tokens2Length;
    }

    // Main comparison block logic
    public ComparisonResult compare(String text1, String text2) throws Exception {
        // Clean and tokenize both texts.
        String[] tokens1 = preprocess(text1);
        String[] tokens2 = preprocess(text2);

        // Avoid comparison if preprocessing removes everything.
        if (tokens1.length == 0 || tokens2.length == 0) {
            throw new Exception("After preprocessing, one or both texts became empty.");
        }

        // Run LCS on token arrays.
        LcsResult lcsRes = lcs(tokens1, tokens2);
        
        // Similarity is based on LCS length over the larger text length.
        double similarity = (double) lcsRes.length / Math.max(tokens1.length, tokens2.length) * 100.0;

        ComparisonResult res = new ComparisonResult();
        res.similarity = similarity;
        res.lcsResult = lcsRes;
        res.tokens1Length = tokens1.length;
        res.tokens2Length = tokens2.length;
        return res;
    }

    // Preprocesses the text by converting it to lowercase, removing punctuation,
    // collapsing extra spaces, and splitting it into words.
    private String[] preprocess(String text) {
        String cleaned = text.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();

        if (cleaned.isEmpty()) {
            return new String[0];
        }
        return cleaned.split(" ");
    }

    // Dynamic Programming implementation of Longest Common Subsequence.
    // dp[i][j] stores the LCS length for the first i tokens of a and first j tokens of b.
    private LcsResult lcs(String[] a, String[] b) {
        int n = a.length;
        int m = b.length;
        int[][] dp = new int[n + 1][m + 1];

        // Fill the DP table.
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a[i - 1].equals(b[j - 1])) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        // Backtrack to recover the actual matched subsequence.
        List<String> sequence = new ArrayList<>();
        int i = n, j = m;

        while (i > 0 && j > 0) {
            if (a[i - 1].equals(b[j - 1])) {
                sequence.add(0, a[i - 1]);
                i--;
                j--;
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }

        return new LcsResult(dp[n][m], sequence);
    }
}
