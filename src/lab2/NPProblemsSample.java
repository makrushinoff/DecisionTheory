package lab2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class NPProblemsSample {

    enum SortType {
        NO_SORT((num1, num2) -> 0),
        ASCEND_SORT((num1, num2) -> num1.compareTo(num2)),
        DESCEND_SORT((num1, num2) -> num1.compareTo(num2) * -1);

        private final Comparator<Integer> comparator;

        SortType(Comparator<Integer> comparator) {
            this.comparator = comparator;
        }

        public Comparator<Integer> getComparator() {
            return comparator;
        }
    }

    private static final Integer MAX_VALUE = 100;

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

    private static int calculateComplexity(List<Integer> row) {
        return (int) Math.round(Math.log(row.size()) * row.size());
    }

    private static List<Integer> copyList(List<Integer> integers) {
        return integers.stream()
                .map(integer -> Integer.valueOf(integer))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("All rows:");
        System.out.println(map.get("1"));
        System.out.println(map.get("2"));
        System.out.println(map.get("3"));
        System.out.println();
        System.out.print("Enter row number (1, 2, 3 or 'all'): ");
        final String rowNumber = scanner.nextLine();
        System.out.println("All sort types:");
        System.out.println(SortType.NO_SORT);
        System.out.println(SortType.ASCEND_SORT);
        System.out.println(SortType.DESCEND_SORT);
        System.out.print("Enter sort type: ");
        final String sortType = scanner.nextLine();
        NFA(copyList(map.get(rowNumber)), SortType.valueOf(sortType));
        FFA(copyList(map.get(rowNumber)), SortType.valueOf(sortType));
        WFA(copyList(map.get(rowNumber)), SortType.valueOf(sortType));
        BFA(copyList(map.get(rowNumber)), SortType.valueOf(sortType));
    }

    private static void NFA(List<Integer> row, SortType sortType) {
        if (!sortType.equals(SortType.NO_SORT)) {
            row.sort(sortType.getComparator());
        }
        AtomicInteger compares = new AtomicInteger();
        List<Integer> containers = new ArrayList<>();
        AtomicInteger currentContainerIndex = new AtomicInteger();
        row.forEach(number -> {
            if (containers.isEmpty()) {
                containers.add(number);
                return;
            }
            Integer container = containers.get(currentContainerIndex.get());
            if (container + number <= MAX_VALUE) {
                containers.remove(currentContainerIndex.get());
                container += number;
                containers.add(currentContainerIndex.get(), container);
            } else {
                currentContainerIndex.getAndIncrement();
                containers.add(currentContainerIndex.get(), number);
            }
            compares.getAndIncrement();
        });
        System.out.println("NFA result containers:");
        System.out.println(containers);
        int cmp = compares.get();
        cmp += sortType != SortType.NO_SORT ? calculateComplexity(row) : 0;
        System.out.println("NFA compares number: " + cmp);
        System.out.println("NFA containers number: " + containers.size());
        System.out.println();
    }

    private static void FFA(List<Integer> row, SortType sortType) {
        if (!sortType.equals(SortType.NO_SORT)) {
            row.sort(sortType.getComparator());
        }
        AtomicInteger compares = new AtomicInteger();
        List<Integer> containers = new ArrayList<>();
        AtomicInteger currentContainerIndex = new AtomicInteger();
        row.forEach(number -> {
            if (containers.isEmpty()) {
                containers.add(number);
                return;
            }
            Integer currentContainer = containers.get(currentContainerIndex.get());
            if (currentContainer + number <= MAX_VALUE) {
                containers.remove(currentContainerIndex.get());
                currentContainer += number;
                containers.add(currentContainerIndex.get(), currentContainer);
            } else {
                for(int i = 0; i < containers.size(); i++) {
                    Integer container = containers.get(i);
                    compares.getAndIncrement();
                    if(container + number <= MAX_VALUE) {
                        containers.remove(i);
                        container += number;
                        containers.add(i, container);
                        currentContainerIndex.set(containers.size() - 1);
                        break;
                    }
                    if(i == containers.size() - 1) {
                        currentContainerIndex.getAndIncrement();
                        containers.add(currentContainerIndex.get(), number);
                        break;
                    }
                }
            }
            compares.getAndIncrement();
        });
        System.out.println("FFA result containers:");
        System.out.println(containers);
        int cmp = compares.get();
        cmp += sortType != SortType.NO_SORT ? calculateComplexity(row) : 0;
        System.out.println("FFA compares number: " + cmp);
        System.out.println("FFA containers number: " + containers.size());
        System.out.println();
    }

    private static void WFA(List<Integer> row, SortType sortType) {
        if (!sortType.equals(SortType.NO_SORT)) {
            row.sort(sortType.getComparator());
        }
        AtomicInteger compares = new AtomicInteger();
        List<Integer> containers = new ArrayList<>();
        AtomicInteger currentContainerIndex = new AtomicInteger();
        row.forEach(number -> {
            if (containers.isEmpty()) {
                containers.add(number);
                return;
            }
            Integer currentContainer = containers.get(currentContainerIndex.get());
            if (currentContainer + number <= MAX_VALUE) {
                containers.remove(currentContainerIndex.get());
                currentContainer += number;
                containers.add(currentContainerIndex.get(), currentContainer);
            } else {
                int minFitContainerSize = Integer.MAX_VALUE;
                int minFitContainerIndex = 0;
                for(int i = 0; i < containers.size(); i++) {
                    compares.getAndIncrement();
                    if(minFitContainerSize > containers.get(i)) {
                        minFitContainerSize = containers.get(i);
                        minFitContainerIndex = i;
                    }
                }
                if(containers.get(minFitContainerIndex) + number <= MAX_VALUE) {
                    Integer containerValue = containers.remove(minFitContainerIndex);
                    containerValue += number;
                    containers.add(minFitContainerIndex, containerValue);
                    currentContainerIndex.set(containers.size() - 1);
                } else {
                    currentContainerIndex.getAndIncrement();
                    containers.add(currentContainerIndex.get(), number);
                }
                compares.getAndIncrement();
            }
            compares.getAndIncrement();
        });
        System.out.println("WFA result containers:");
        System.out.println(containers);
        int cmp = compares.get();
        cmp += sortType != SortType.NO_SORT ? calculateComplexity(row) : 0;
        System.out.println("WFA compares number: " + cmp);
        System.out.println("WFA containers number: " + containers.size());
        System.out.println();
    }

    private static void BFA(List<Integer> row, SortType sortType) {
        if (!sortType.equals(SortType.NO_SORT)) {
            row.sort(sortType.getComparator());
        }
        AtomicInteger compares = new AtomicInteger();
        List<Integer> containers = new ArrayList<>();
        AtomicInteger currentContainerIndex = new AtomicInteger();
        row.forEach(number -> {
            if (containers.isEmpty()) {
                containers.add(number);
                return;
            }
            Integer currentContainer = containers.get(currentContainerIndex.get());
            if (currentContainer + number <= MAX_VALUE) {
                containers.remove(currentContainerIndex.get());
                currentContainer += number;
                containers.add(currentContainerIndex.get(), currentContainer);
            } else {
                int bestFitContainerResult = Integer.MIN_VALUE;
                int bestFitContainerIndex = 0;
                for(int i = 0; i < containers.size(); i++) {
                    compares.set(compares.get() + 2);
                    if(containers.get(i) + number <= MAX_VALUE && containers.get(i) + number > bestFitContainerResult) {
                        bestFitContainerResult = containers.get(i) + number;
                        bestFitContainerIndex = i;
                    }
                }
                if(bestFitContainerResult != Integer.MIN_VALUE) {
                    Integer containerValue = containers.remove(bestFitContainerIndex);
                    containerValue += number;
                    containers.add(bestFitContainerIndex, containerValue);
                    currentContainerIndex.set(containers.size() - 1);
                } else {
                    currentContainerIndex.getAndIncrement();
                    containers.add(currentContainerIndex.get(), number);
                }
                compares.getAndIncrement();
            }

            compares.getAndIncrement();
        });
        System.out.println("BFA result containers:");
        System.out.println(containers);
        int cmp = compares.get();
        cmp += sortType != SortType.NO_SORT ? calculateComplexity(row) : 0;
        System.out.println("BFA compares number: " + cmp);
        System.out.println("BFA containers number: " + containers.size());
        System.out.println();
    }
}
