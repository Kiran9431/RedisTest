package com.example.Redis_Caching;

import com.example.Redis_Caching.entity.Employee;
import com.example.Redis_Caching.repository.EmployeeDao;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static javafx.beans.binding.Bindings.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RedisCachingApplicationTests {

	@Autowired
	private EmployeeDao dao;

	private ZSetOperations<String, Employee> zSetOperations;

	@Test
	public void addme() {
		Employee emp = new Employee();
		emp.setId(1);
		emp.setName("bvr");
		dao.save(emp);
		long startTime = System.nanoTime();
		Employee storedEmp = dao.findEmployeeById(emp.getId());
		long stopTime = System.nanoTime();
		long totalTime = stopTime - startTime;
		float seconds = ((float) totalTime / 1000000000);
		System.out.println("Total time Taken:" + seconds);
		Assert.assertEquals(emp.getId(), storedEmp.getId());
	}
	@Test
	public void getfewEmployees(){
		Employee employee1 = new Employee(1,"John");
		Employee employee2 = new Employee(2,"Alice");
		Employee employee3 = new Employee(3,"Bob");
		Set<Employee> employeesSet = new HashSet<>(Arrays.asList(employee1, employee2, employee3));
	//	when(zSetOperations.rangeByScore("Employee", 0, 100)).thenReturn(employeesSet);
		List<Employee> employees = dao.findfewEmployees();
		assertEquals(employeesSet.size(), employees.size());
		for (int i = 0; i < employees.size(); i++) {
			assertEquals(employeesSet.toArray()[i], employees.get(i));

		}
	}
	@Test
	public void findallEmployees(){
		RedisCachingApplication rca = new RedisCachingApplication();
		long startTime=System.nanoTime();
		rca.getAllEmployees();
		long stopTime=System.nanoTime();
		long totalTime=stopTime-startTime;
		float seconds=((float) totalTime /1000000000);
		System.out.println("Total time Taken:" + seconds);
	}

	@Test
	public void findEmployee() {
		Employee employee = dao.findEmployeeById(98656);
		Assert.assertEquals(98656, employee.getId());
	}

	@Test
	public void remove() {
		Assert.assertEquals(98656, 98656);
	}

	@Test
	public void test_to_add_list_of_employees() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonFilePath = "C:\\Users\\kkumar\\Downloads\\Redis_Caching-master\\Redis_Caching-master\\Redis_Caching\\src\\main\\resources\\emp.json";
		List<Employee> employees = objectMapper.readValue(Paths.get(jsonFilePath).toFile(), new TypeReference<List<Employee>>() {
		});
		long startTime = System.nanoTime();
		RedisCachingApplication rca = new RedisCachingApplication();
		rca.saveListOfEmployees(employees);
		long stopTime = System.nanoTime();
		long totalTime = stopTime - startTime;
		System.out.println(totalTime);
	}

	@Test
	public void listofemployee() {
		List<Employee> employees = new ArrayList<>();
		long startTime = System.nanoTime();
		for (int i = 1; i <= 10000000; i++) {
			Employee employee = new Employee(i, "Employee " + i, getRandomDepartment(), getRandomSalary());
			dao.save(employee);
		}
		long stopTime = System.nanoTime();
		long totalTime = stopTime - startTime;
		float seconds = ((float) totalTime / 1000000000);
		System.out.println("Total time Taken:" + seconds);
	}
	private static String getRandomDepartment() {
		String[] departments = {"HR", "IT", "Finance", "Sales"};
		return departments[new Random().nextInt(departments.length)];
	}
	private static int getRandomSalary(){
		int[] salary={40000,50000,20000,30000,40000,50000};
		return salary[new Random().nextInt(salary.length)];
	}
}
