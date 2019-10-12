//package com.suntr.java9FlowSample.steamSample;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import static java.util.stream.Collectors.*;
//
///**
// * Name   |  Department| Salary    |  Spoken Languages
// * -------|------------|-----------|---------------------
// * John   |  Sales     | 1000.89   |  English, French
// * Wally  |  Sales     | 900.89    |  Spanish, Wu
// * Ken    |  Sales     | 1900.00   |  English, French
// * Li     |  HR        | 1950.89   |  Wu, Lao
// * Manuel |  IT        | 2001.99   |  English, German
// * Tony   |  IT        | 1700.89   |  English
// *
// */
//public class Java9SteamCollectorTester {
//    public static void main(String[] args) {
//        System.out.println("Testing Collectors.filtering():");
//        testFiltering();
//        System.out.println("\nTesting Collectors.flatMapping():");
//        testFlatMapping();
//    }
//    public static void testFiltering() {
//        Map<String, List<Employee>> empGroupedByDept = Employee.employees()
//                .stream()
//                .collect(groupingBy(Employee::getDepartment, toList()));
//        System.out.println("Employees grouped by department:");
//        System.out.println(empGroupedByDept);
//        // Employees having salary > 1900 grouped by department:
//        Map<String, List<Employee>> empSalaryGt1900GroupedByDept = Employee.employees()
//                .stream()
//                .filter(e -> e.getSalary() > 1900)
//                .collect(groupingBy(Employee::getDepartment, toList()));
//        System.out.println("\nEmployees having salary > 1900 grouped by department:");
//        System.out.println(empSalaryGt1900GroupedByDept);
//        // Group employees by department who have salary > 1900
//        Map<String, List<Employee>> empGroupedByDeptWithSalaryGt1900 = Employee.employees()
//                .stream()
//                .collect(groupingBy(Employee::getDepartment,
//                        filtering(e -> e.getSalary() > 1900.00, toList())));
//        System.out.println("\nEmployees grouped by department having salary > 1900:");
//        System.out.println(empGroupedByDeptWithSalaryGt1900);
//        // Group employees by department who speak at least 2 languages
//        // and 1 of them is English
//        Map<String, List<Employee>> empByDeptWith2LangWithEn = Employee.employees()
//                .stream()
//                .collect(groupingBy(Employee::getDepartment,
//                        filtering(e -> e.getSpokenLanguages().size() >= 2
//                                        &&
//                                        e.getSpokenLanguages().contains("English"),
//                                toList())));
//        System.out.println("\nEmployees grouped by department speaking min. 2" +
//                " languages of which one is English:");
//        System.out.println(empByDeptWith2LangWithEn);
//    }
//    public static void testFlatMapping(){
//        Map<String, Set<List<String>>> langByDept = Employee.employees()
//                .stream()
//                .collect(groupingBy(Employee::getDepartment,
//                        mapping(Employee::getSpokenLanguages, toSet())));
//        System.out.println("Languages spoken by department using mapping():");
//        System.out.println(langByDept);
//        Map<String,Set<String>> langByDept2 = Employee.employees()
//                .stream()
//                .collect(groupingBy(Employee::getDepartment,
//                        flatMapping(e -> e.getSpokenLanguages().stream(), toSet())));
//        System.out.println("\nLanguages spoken by department using flapMapping():");
//        System.out.println(langByDept2) ;
//    }
//}
