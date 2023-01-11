package lab1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ParetoSlaterSample {

    private static final List<Integer> row1 = List.of(77, 51, 38, 71, 49, 89, 67, 88, 92, 95, 43, 44, 29, 90, 82, 40, 41, 69, 26, 32);
    private static final List<Integer> row2 = List.of(61, 42, 60, 17, 23, 61, 81, 9, 90, 25, 96, 67, 77, 34, 90, 26, 24, 57, 14, 68);
    private static final List<Integer> row3 = List.of(5, 58, 12, 86, 51, 46, 26, 94, 16, 52, 78, 29, 46, 90, 47, 70, 51, 80, 31, 93);

    private static final Map<String, List<Integer>> map;

    static {
        List<Integer> row = new ArrayList<>();
        row.addAll(row1);
        row.addAll(row2);
        row.addAll(row3);
        map = Map.of("1", row1, "2", row2, "3", row3, "all", row);
    }

    public static void main(String[] args) {
        List<Integer> row = map.get(chooseRowOption());
        System.out.println("Chosen number array:");
        System.out.println(row);
        List<NumberPair> pairs = getPairsFromList(row);
        setParetoDomination(pairs);
        setSlaterDomination(pairs);
        printResult(pairs);
    }

    private static String chooseRowOption() {
        System.out.println("All rows:");
        System.out.println(map.get("1"));
        System.out.println(map.get("2"));
        System.out.println(map.get("3"));
        System.out.println();
        System.out.print("Enter row number (1, 2, 3 or 'all'): ");
        return new Scanner(System.in).nextLine();
    }

    private static void setParetoDomination(List<NumberPair> pairs) {
        Comparator<NumberPair> paretoComparator = ParetoComparator();
        for (int i = 0; i < pairs.size(); i++) {
            for (int j = 0; j < pairs.size(); j++) {
                if (j <= i) {
                    continue;
                }
                if (!continueParetoComparing(pairs.get(i), pairs.get(j), paretoComparator)) {
                    break;
                }
            }
        }
    }

    private static void setSlaterDomination(List<NumberPair> pairs) {
        Comparator<NumberPair> slaterComparator = SlaterComparator();
        for (int i = 0; i < pairs.size(); i++) {
            for (int j = 0; j < pairs.size(); j++) {
                if (j <= i) {
                    continue;
                }
                if (!continueSlaterComparing(pairs.get(i), pairs.get(j), slaterComparator)) {
                    break;
                }
            }
        }
    }

    private static boolean continueParetoComparing(NumberPair numberPair1, NumberPair numberPair2, Comparator<NumberPair> paretoComparator) {
        int compareResult = paretoComparator.compare(numberPair1, numberPair2);
        if (compareResult > 0) {
            numberPair2.setParetoDominated(true);
            return true;
        } else if (compareResult < 0) {
            numberPair1.setParetoDominated(true);
            return false;
        }
        return true;
    }

    private static boolean continueSlaterComparing(NumberPair numberPair1, NumberPair numberPair2, Comparator<NumberPair> slaterComparator) {
        int compareResult = slaterComparator.compare(numberPair1, numberPair2);
        if (compareResult > 0) {
            numberPair2.setSlaterDominated(true);
            return true;
        } else if (compareResult < 0) {
            numberPair1.setSlaterDominated(true);
            return false;
        }
        return true;
    }

    private static List<NumberPair> getPairsFromList(List<Integer> list) {
        return list.stream().map(num -> {
            String s = String.valueOf(num);
            if (s.length() == 1) {
                s = "0".concat(s);
            }
            return s;
        }).map(str -> {
            String s1 = str.substring(0, 1);
            String s2 = str.substring(1);
            return new NumberPair(Integer.parseInt(s1), Integer.parseInt(s2), false, false);
        }).collect(Collectors.toList());
    }

    private static Comparator<NumberPair> ParetoComparator() {
        return (pair1, pair2) -> {
            if (pair1.getQ1() >= pair2.getQ1() && pair1.getQ2() >= pair2.getQ2()) {
                return 1;
            } else if (pair2.getQ1() >= pair1.getQ1() && pair2.getQ2() >= pair1.getQ2()) {
                return -1;
            }
            return 0;
        };
    }

    private static Comparator<NumberPair> SlaterComparator() {
        return (pair1, pair2) -> {
            if (pair1.getQ1() > pair2.getQ1() && pair1.getQ2() > pair2.getQ2()) {
                return 1;
            } else if (pair2.getQ1() > pair1.getQ1() && pair2.getQ2() > pair1.getQ2()) {
                return -1;
            }
            return 0;
        };
    }

    private static void printResult(List<NumberPair> pairs) {
        List<String> paretoOptimalResult = pairs.stream()
                .filter(pair -> !pair.isParetoDominated())
                .map(pair -> String.valueOf(pair.getQ1()) + pair.getQ2())
                .toList();
        System.out.println("Pareto optimal results: " + paretoOptimalResult);

        List<String> slaterOptimalResult = pairs.stream()
                .filter(pair -> !pair.isSlaterDominated())
                .map(pair -> String.valueOf(pair.getQ1()) + pair.getQ2())
                .toList();
        System.out.println("Slater optimal results: " + slaterOptimalResult);
    }

}
