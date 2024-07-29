package org.example.coforge;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.System.out;

public class Runner {
    private static HashMap<String, Employer> dataSource = new HashMap<>();

    private static final Employer EMPLOYEE_TEMPLATE = Employer.builder()
            .address("some_address")
            .destination(Destination.DEVELOPER)
            .age(0)
            .department("some_department")
            .email("some_email@test.com")
            .dateOfJoining(1231231231L)
            .build();

    public static void main(String[] args) {
        IntStream.range(0, 10).forEach(
                i -> {
                    create(
                            EMPLOYEE_TEMPLATE.toBuilder()
                                    .age(20 + i)
                                    .destination(Destination.QUALITY_ANALYST)
                                    .build(),
                            "Jacob Gaba");
                }
        );

        out.println("dataSource size: " + dataSource.size());

        var sortedEmployers = sortedEmployers(25, 35, "Jacob Gaba");
        out.println("Sorted list size: " + sortedEmployers.size());
        sortedEmployers.forEach(emp -> out.println(emp.toString()));
    }


    private static List<Employer> sortedEmployers(int startAge,
                                                  int endAge,
                                                  String employeeName) {
        var findAllByName = dataSource.keySet().stream()
                .filter(id -> id.contains(employeeName))
                .collect(Collectors.toList());

        return findAllByName.stream()
                .map(id -> dataSource.get(id))
                .collect(Collectors.toList()).stream()
                .filter(employee -> startAge <= employee.getAge() && endAge >= employee.getAge())
                .collect(Collectors.toList());
    }

    private static Employer create(Employer employee, String employeeName) {
        var id = UUID.randomUUID() + "_" + employeeName;
        dataSource.put(id, employee);
        out.println("Employer id: " + id +
                "\nEmployer attribute: [" + employee.toString() + "]");
        return dataSource.get(id);
    }

    @Data
    @Builder(toBuilder = true)
    @ToString
    private static class Employer {
        private String address;
        private Destination destination;
        private int age;
        private String department;
        private String email;
        private long dateOfJoining;
    }

    private enum Destination {
        SOFTWARE_ENGINEER,
        SENIOR_SOFTWARE_ENGINEER,
        QUALITY_ANALYST,
        SENIOR_SOFTWARE,
        DEVELOPER,
        SENIOR_DEVELOPER
    }
}