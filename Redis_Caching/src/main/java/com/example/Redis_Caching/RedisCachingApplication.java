package com.example.Redis_Caching;

import com.example.Redis_Caching.entity.Employee;
import com.example.Redis_Caching.repository.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("employee")
@EnableCaching
public class RedisCachingApplication {
	@Autowired
	private EmployeeDao dao;


	@PostMapping("/addEmployee")
	public Employee save(@RequestBody Employee employee) {
		return dao.save(employee);
	}

	@PostMapping("/addListOfEmployees")
    public String saveListOfEmployees(@RequestBody List<Employee> employeeList){
		long startTime=System.nanoTime();
        for (Employee emp:employeeList) {
            dao.save(emp);
        }
		long stopTime=System.nanoTime();
		long totalTime=stopTime-startTime;
		System.out.println("Total time taken :"+totalTime);
        return "Total time taken= "+totalTime;
    }
	@GetMapping("/getfew")
	public List<Employee> findfewEmployees(){
		List<Employee> employeeList=dao.findfewEmployees();
	 return employeeList;
	}

	@GetMapping("/getAllEmployees")
	public List<Employee> getAllEmployees() {
		long startTime=System.nanoTime();
		List<Employee> employeeList=dao.findAll();
		long stopTime=System.nanoTime();
		long totalTime=stopTime-startTime;
		float seconds=((float) totalTime /1000000000);
		System.out.println("total time"+seconds);
		return employeeList;
	}


	@Cacheable(key = "#id", value = "Employee")
	@GetMapping("/getEmp/{id}")
	public Employee findEmployee(@PathVariable int id) {
		return dao.findEmployeeById(id);
	}

	@DeleteMapping("/deleteEmployee/{id}")
	@CacheEvict(key = "#id", value = "Employee")
	public String remove(@PathVariable int id)   {
		return dao.deleteEmployee(id);
	}

	public static void main(String[] args) {

		SpringApplication.run(RedisCachingApplication.class, args);
	}

}
