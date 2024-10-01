package org.example.coforge;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MapComparator {

    public static void main(String[] args) {
        Map<String, Integer> nonSortedMap = Map.of(
                "a", 2,
                "s", 3,
                "w", 1,
                "g", 5,
                "m", 4
        );

        var sortedMap = sortByValue(nonSortedMap);
        sortedMap.entrySet().forEach(System.out::println);
    }

    private static Map<String, Integer> sortByValue(Map<String, Integer> nonSortedMap) {
        return nonSortedMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
