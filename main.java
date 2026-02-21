import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.*;

public class Main {

    // Convert base to decimal
    public static BigInteger convertToDecimal(String value, int base) {
        return new BigInteger(value, base);
    }

    // Lagrange interpolation to compute f(0)
    public static BigInteger findConstant(int[] x, BigInteger[] y, int k) {

        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {

            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    numerator = numerator.multiply(BigInteger.valueOf(-x[j]));
                    denominator = denominator.multiply(
                            BigInteger.valueOf(x[i] - x[j])
                    );
                }
            }

            BigInteger term = y[i].multiply(numerator).divide(denominator);
            result = result.add(term);
        }

        return result;
    }

    public static void main(String[] args) throws Exception {

        String content = new String(Files.readAllBytes(Paths.get("input.json")));
        Pattern kPattern = Pattern.compile("\"k\"\\s*:\\s*(\\d+)");
        Matcher kMatcher = kPattern.matcher(content);
        kMatcher.find();
        int k = Integer.parseInt(kMatcher.group(1));

        // Extract all roots
        Pattern pattern = Pattern.compile("\"(\\d+)\"\\s*:\\s*\\{\\s*\"base\"\\s*:\\s*\"(\\d+)\"\\s*,\\s*\"value\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(content);

        List<Integer> xList = new ArrayList<>();
        List<BigInteger> yList = new ArrayList<>();

        while (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int base = Integer.parseInt(matcher.group(2));
            String value = matcher.group(3);

            BigInteger y = convertToDecimal(value, base);

            xList.add(x);
            yList.add(y);
        }
        int[] x = new int[k];
        BigInteger[] y = new BigInteger[k];

        for (int i = 0; i < k; i++) {
            x[i] = xList.get(i);
            y[i] = yList.get(i);
        }

        BigInteger constant = findConstant(x, y, k);
        System.out.println(constant);
    }
}