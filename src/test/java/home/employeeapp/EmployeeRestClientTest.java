package home.employeeapp;

import home.employeeapp.dto.Employee;
import home.employeeapp.exception.ClientDataException;
import home.employeeapp.exception.EmployeeServiceException;
import home.employeeapp.service.EmployeeRestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeRestClientTest {

    private static final String baseUrl = "http://localhost:8081/employeeservice";

    private final WebClient webClient = WebClient.create(baseUrl);

    EmployeeRestClient employeeRestClient = new EmployeeRestClient(webClient);

    @Test
    void retrieveAllEmployees() {

        List<Employee> employeeList = employeeRestClient.retrieveAllEmployees();
        System.out.println("employeeList: " + employeeList);
        assertTrue(employeeList.size() > 0);
    }

    @Test
    void retrieveEmployeeById() {

        int employeeId = 1;
        Employee employee = employeeRestClient.retrieveEmployeeById(employeeId);
        assertEquals("Chris", employee.getFirstName());
    }

    @Test
    void retrieveEmployeeById_notFound() {

        int employeeId = 10;
        Assertions.assertThrows(WebClientResponseException.class,
                () -> employeeRestClient.retrieveEmployeeById(employeeId));
    }

    @Test
    void retrieveEmployeeById_custom_error_handling() {

        int employeeId = 10;
        Assertions.assertThrows(ClientDataException.class,
                () -> employeeRestClient.retrieveEmployeeById_custom_error_handling(employeeId));
    }

    @Test
    void retrieveEmployeeByName() {

        String name = "Chris";
        List<Employee> employees = employeeRestClient.retrieveEmployeeByName(name);
        assertTrue(employees.size() > 0);
        Employee employee = employees.get(0);
        assertEquals("Chris", employee.getFirstName());
    }

    @Test
    void retrieveEmployeeByName_notFound() {

        String name = "ABC";
        Assertions.assertThrows(WebClientResponseException.class,
                () -> employeeRestClient.retrieveEmployeeByName(name));
    }

    @Test
    void addNewEmployee() {

        Employee addedEmployee = new Employee(null, 54, "Oleg", "Olegov",
                "male", "Cleaner");

        Employee employee = employeeRestClient.addNewEmployee(addedEmployee);
        System.out.println("employee : " + employee);
        assertNotNull(employee.getId());

    }

    @Test
    void addNewEmployee_BadRequest() {

        Employee employee = new Employee(null, 54, null, "Olegov",
                "male", "Cleaner");

        Assertions.assertThrows(WebClientResponseException.class,
                () -> employeeRestClient.addNewEmployee(employee));
    }

    @Test
    void updateEmployee() {

        Employee employee = new Employee(null, null, "Adam1", "Sandler1",
                null, null);
        Employee upadtedEmployee = employeeRestClient.updateEmployee(2, employee);
        assertEquals("Adam1", upadtedEmployee.getFirstName());
        assertEquals("Sandler1", upadtedEmployee.getLastName());
    }

    @Test
    void updateEmployee_notFound() {

        Employee employee = new Employee(null, null, "Adam1", "Sandler1",
                null, null);
        Assertions.assertThrows(WebClientResponseException.class,
                () -> employeeRestClient.updateEmployee(200, employee));
    }

    @Test
    void deleteEmployeeById() {

        Employee addedEmployee = new Employee(null, 54, "Oleg1", "Olegov1",
                "male", "Cleaner");

        Employee employee = employeeRestClient.addNewEmployee(addedEmployee);
        System.out.println("employee : " + employee);
        String response = employeeRestClient.deleteEmployeeById(employee.getId());
        String exceptedMessage = "Employee deleted successfully.";
        assertEquals(response, exceptedMessage);
    }

    @Test
    void errorEndpoint() {
        Assertions.assertThrows(EmployeeServiceException.class,
                () -> employeeRestClient.errorEndpoint());
    }

    @Test
    void errorEndpoint_NotFound() {
        Assertions.assertThrows(WebClientResponseException.class,
                () -> employeeRestClient.deleteEmployeeById(200));
    }
}

