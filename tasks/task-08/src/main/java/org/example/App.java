package org.example;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main( String[] args ) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        try {
            EmployeeService service  = context.getBean(EmployeeService.class);

            service.addEmployee(101, "Anand", "CSE");
            service.addEmployee(102, "Divya", "ECE");
            service.addEmployee(103, "Ravi", "IT");

            System.out.println("All Employees");
            service.getAllEmployees().forEach(System.out::println);

            System.out.println("\n Find Employee 101");
            System.out.println(service.getEmployee(101));

            System.out.println("\n Find Employee 102");
            System.out.println(service.getEmployee(102));

            System.out.println("\nDelete Employee 101");
            System.out.println("Deleted? " + service.removeEmployee(101));

            System.out.println("\n All Employees after Deletion");
            service.getAllEmployees().forEach(System.out::println);
        } finally {
            if (context instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) context).close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
