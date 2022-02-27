package home.employeeapp.service;

import home.employeeapp.dto.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
public class EmployeeRestClient {

    static final String GET_ALL_EMPLOYEES_V1 = "/v1/allEmployees";
    static final String EMPLOYEE_BY_ID_V1 = "/v1/employee/{id}";
    static final String GET_EMPLOYEE_BY_NAME_V1  ="/v1/employeeName";
    static final String ADD_NEW_EMPLOYEE_V1 = "/v1/employee";

    private final WebClient webClient;

    public EmployeeRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    //http://localhost:8081/employeeservice/v1/allEmployees

    public List<Employee> retrieveAllEmployees() {

        return webClient.get().uri(GET_ALL_EMPLOYEES_V1)
                .retrieve()
                .bodyToFlux(Employee.class)
                .collectList()
                .block();
    }

    public Employee retrieveEmployeeById(int employeeId) {

        //http://localhost:8081/employeeservice/v1/employee/2

        try {
            return webClient.get().uri(EMPLOYEE_BY_ID_V1, employeeId)
                    .retrieve()
                    .bodyToMono(Employee.class)
                    .block();
        }
        catch (WebClientResponseException ex) {
            log.error("Error Response code is {} and the Response body is {}",
                    ex.getRawStatusCode(), ex.getResponseBodyAsString());
            log.error("WebClientResponseException in retrieveEmployeeById ", ex);
            throw ex;
        }
        catch (Exception ex) {
            log.error("Exception in retrieveEmployeeById ", ex);
            throw ex;
        }
    }

    public List<Employee> retrieveEmployeeByName(String employeeName) {

        //http://localhost:8081/employeeservice/v1/employeeName?employee_name=Chris

        String uriString = UriComponentsBuilder.fromUriString(GET_EMPLOYEE_BY_NAME_V1)
                .queryParam("employee_name", employeeName)
                .build().toUriString();
        try {
            return webClient.get().uri(uriString)
                    .retrieve()
                    .bodyToFlux(Employee.class)
                    .collectList()
                    .block();
        }
        catch (WebClientResponseException ex) {
            log.error("Error Response code is {} and the Response body is {}",
                    ex.getRawStatusCode(), ex.getResponseBodyAsString());
            log.error("WebClientResponseException in retrieveEmployeeByName ", ex);
            throw ex;
        }
        catch (Exception ex) {
            log.error("Exception in retrieveEmployeeByName ", ex);
            throw ex;
        }
    }

    public Employee addNewEmployee(Employee employee) {

        try {
            return webClient.post().uri(ADD_NEW_EMPLOYEE_V1)
                    .bodyValue(employee)
                    .retrieve()
                    .bodyToMono(Employee.class)
                    .block();
        }
        catch (WebClientResponseException ex) {
            log.error("Error Response code is {} and the Response body is {}",
                    ex.getRawStatusCode(), ex.getResponseBodyAsString());
            log.error("WebClientResponseException in addNewEmployee ", ex);
            throw ex;
        }
        catch (Exception ex) {
            log.error("Exception in addNewEmployee ", ex);
            throw ex;
        }
    }

    public Employee updateEmployee(int employeeId, Employee employee) {

        try {
            return webClient.put().uri(EMPLOYEE_BY_ID_V1, employeeId)
                    .bodyValue(employee)
                    .retrieve()
                    .bodyToMono(Employee.class)
                    .block();
        }
        catch (WebClientResponseException ex) {
            log.error("Error Response code is {} and the Response body is {}",
                    ex.getRawStatusCode(), ex.getResponseBodyAsString());
            log.error("WebClientResponseException in updateEmployee ", ex);
            throw ex;
        }
        catch (Exception ex) {
            log.error("Exception in updateEmployee ", ex);
            throw ex;
        }
    }

    public String deleteEmployeeById(int employeeId) {
        return webClient.delete().uri(EMPLOYEE_BY_ID_V1, employeeId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
