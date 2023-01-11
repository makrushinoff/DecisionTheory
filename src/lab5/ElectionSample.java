package lab5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ElectionSample {

    static class ElectionResult {
        private Integer votes;
        private List<String> candidatesOrder;

        public ElectionResult(Integer votes, List<String> candidatesOrder) {
            this.votes = votes;
            this.candidatesOrder = candidatesOrder;
        }

        public Integer getVotes() {
            return votes;
        }

        public void setVotes(Integer votes) {
            this.votes = votes;
        }

        public List<String> getCandidatesOrder() {
            return candidatesOrder;
        }

        public void setCandidatesOrder(List<String> candidatesOrder) {
            this.candidatesOrder = candidatesOrder;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ElectionResult that = (ElectionResult) o;
            return Objects.equals(votes, that.votes) && Objects.equals(candidatesOrder, that.candidatesOrder);
        }

        @Override
        public int hashCode() {
            return Objects.hash(votes, candidatesOrder);
        }
    }

    private static final List<ElectionResult> ELECTION_RESULTS = List.of(
            new ElectionResult(2, List.of("a", "b", "c", "d")),
            new ElectionResult(6, List.of("c", "a", "b", "d")),
            new ElectionResult(3, List.of("b", "a", "c", "d"))
    );
    private static final List<String> CANDIDATES = List.of("a", "b", "c", "d");

    private static List<String> reverseList(List<String> list) {
        List<String> newList = new ArrayList<>();
        for(int i = list.size() - 1; i >= 0; i--) {
            newList.add(list.get(i));
        }
        return newList;
    }

    private static void BordDecisions() {
        System.out.println("-------- Bord rule --------");
        List<ElectionResult> electionsCopy = ELECTION_RESULTS.stream()
                .peek(electionResult -> electionResult.setCandidatesOrder(reverseList(electionResult.getCandidatesOrder())))
                .toList();
        Map<String, Integer> candidatesPoints = new HashMap<>();
        electionsCopy.forEach(electionResult -> {
            electionResult.getCandidatesOrder().forEach(candidate -> {
                int index = electionResult.getCandidatesOrder().indexOf(candidate);
                candidatesPoints.merge(candidate, index * electionResult.getVotes(), Integer::sum);
            });
        });
        System.out.println(candidatesPoints);
        System.out.println("-------- --------- --------");
    }

    private static void ComplandDecisions() {
        System.out.println("-------- Compland rule --------");
        List<ElectionResult> electionsCopy = ELECTION_RESULTS.stream().collect(Collectors.toList());
        record CoplandValue(String candidate, int value) {}
        List<CoplandValue> coplandValueList = new ArrayList<>();
        for(int i = 0; i < CANDIDATES.size(); i++) {
            int wins = 0;
            for(int j = 0; j < CANDIDATES.size(); j++) {
                if(i == j) {
                    continue;
                }
                if(compareCandidates(electionsCopy, CANDIDATES.get(i), CANDIDATES.get(j), false).equals(CANDIDATES.get(i))) {
                    wins++;
                } else {
                    wins--;
                }
            }
            coplandValueList.add(new CoplandValue(CANDIDATES.get(i), wins));
        }
        System.out.println(coplandValueList);
        System.out.println("-------- ----------- --------");
    }

    private static void ParallelExclusions() {
        System.out.println("-------- Parallel exclusions rule --------");
        List<ElectionResult> electionsCopy = ELECTION_RESULTS.stream().collect(Collectors.toList());
        System.out.println(defineWinner(electionsCopy, CANDIDATES));
        System.out.println("-------- ------------------------ --------");
    }

    private static String defineWinner(List<ElectionResult> electionResults, List<String> candidates) {
        if(candidates.size() > 2) {
            return compareCandidates(electionResults,
                    defineWinner(electionResults, getListHalf(candidates, (candidates.size() / 2), true)),
                    defineWinner(electionResults, getListHalf(candidates, (candidates.size() / 2), false)),
                    true
            );
        } else {
            if(candidates.size() == 1) {
                return candidates.get(0);
            } else {
                return compareCandidates(electionResults, candidates.get(0), candidates.get(1), true);
            }
        }
    }

    private static List<String> getListHalf(List<String> list, int indexPivot, boolean firstHalf) {
        List<String> strings = new ArrayList<>();
        if(firstHalf) {
            for(int i = 0; i < indexPivot; i++) {
                strings.add(list.get(i));
            }
            return strings;
        }
        for(int i = indexPivot; i < list.size(); i++) {
            strings.add(list.get(i));
        }
        return strings;
    }

    private static String compareCandidates(List<ElectionResult> electionResults, String candidate1, String candidate2, boolean print) {
        AtomicInteger candidate1BeatsCandidate2 = new AtomicInteger(0);
        AtomicInteger candidate2BeatsCandidate1 = new AtomicInteger(0);
        electionResults.forEach(electionResult -> {
            int candidate1Index = electionResult.getCandidatesOrder().indexOf(candidate1);
            int candidate2Index = electionResult.getCandidatesOrder().indexOf(candidate2);
            if(candidate1Index < candidate2Index) {
                candidate1BeatsCandidate2.addAndGet(electionResult.getVotes());
            } else {
                candidate2BeatsCandidate1.addAndGet(electionResult.getVotes());
            }
        });
        final String winner = candidate1BeatsCandidate2.get() > candidate2BeatsCandidate1.get() ? candidate1 : candidate2;
        if(print) {
            System.out.print(candidate1 + " vs " + candidate2 + " -> ");
            System.out.print(candidate1BeatsCandidate2.get() + ":" + candidate2BeatsCandidate1 + " -> " );
            System.out.println(winner + " is winner");
        }
        return winner;
    }

    public static void main(String[] args) {
        BordDecisions();
        ComplandDecisions();
        ParallelExclusions();
    }

}
