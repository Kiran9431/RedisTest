package com.example.Redis_Caching.repository;

import com.example.Redis_Caching.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class EmployeeDao {
    public static final String HASH_KEY = "Employee";
    @Autowired
    private RedisTemplate template;


    public Employee save(Employee employee) {
        template.opsForHash().put(HASH_KEY, employee.getId(), employee);
        return employee;
    }

    public List<Employee> findfewEmployees() {
        List<Employee> employees = new ArrayList<>();
        Set<String> keys = template.keys("Employee:*");
        for (String key : keys) {
            Employee employee = (Employee) template.opsForValue().get(key);
            if (employee.getId() < 1000) {
                employees.add(employee);
            }
            // Set<Employee> employeesSet = zSetOperations.rangeByScore("Employee", 0, 99);
            //List<Employee> employees=new ArrayList<>(employeesSet);
        }
        return employees;
    }


    public List<Employee> findAll(){
        System.out.println("findall() Method called from DB");
        return template.opsForHash().values(HASH_KEY);
    }

    public Employee findEmployeeById(int id){
        System.out.println("called findEmployeeById() from DB");
        return (Employee) template.opsForHash().get(HASH_KEY,id);
    }

    public String deleteEmployee(int id){
        template.opsForHash().delete(HASH_KEY,id);
        return "employee removed !!";
    }
}
