package com.kis;

import java.util.ArrayList;
import java.util.List;
 
/**
 * Created by 1004lucifer on 2015-04-08.
 */
public class Company {
    private String name;
    private List<Person> employees;
 
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Person> getEmployees() { return employees; }
    public void setEmployees(List<Person> employees) { this.employees = employees; }
 
    public static class Person {
        private String name;
        private String age;
        private String sex;
 
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getAge() { return age; }
        public void setAge(String age) { this.age = age; }
        public String getSex() { return sex; }
        public void setSex(String sex) { this.sex = sex; }
        public String toString() { return "name: " + name + "\tage: " + age + "\tsex: " + sex; }
    }
 
    public static Company getCompanyDummy() {
        Company company = new Company();
        company.setName("1004lucifer's Company");
 
        List<Company.Person> personList = new ArrayList<Person>();
 
        Company.Person person = new Person();
        person.setName("1004lucifer");
        person.setAge("30");
        person.setSex("M");
        personList.add(person);
 
        person = new Person();
        person.setName("vvoei");
        person.setAge("29");
        person.setSex("M");
        personList.add(person);
 
        person = new Person();
        person.setName("John");
        person.setSex("M");
        personList.add(person);
 
        person = new Person();
        person.setName("Jane");
        person.setAge("20");
        personList.add(person);
 
        person = new Person();
        personList.add(person);
 
        company.setEmployees(personList);
 
        return company;
    }
}