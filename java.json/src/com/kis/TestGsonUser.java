package com.kis;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
 
/**
 * Created by 1004lucifer on 2015-04-08.
 */
public class TestGsonUser {
    public static void main(String[] argv) {
        Company company = Company.getCompanyDummy();
 
        System.out.println("========= Object => Json ==========");
        String company2Json = new Gson().toJson(company);
        System.out.println(company2Json);
 
        System.out.println("========= Json => Object =========");
        Company json2Company = new Gson().fromJson(company2Json, Company.class);
        printCompanyObject(json2Company);
 
        System.out.println("========= Object => Json =========");
        String company2JsonIsNull = new GsonBuilder().serializeNulls().create().toJson(company);
        System.out.println(company2JsonIsNull);
 
        System.out.println("========= Json => Object =========");
        Company json2CompanyIsNull = new Gson().fromJson(company2Json, Company.class);
        printCompanyObject(json2CompanyIsNull);
    }
 
    private static void printCompanyObject(Company company) {
        List<Company.Person> personList = company.getEmployees();
        System.out.println("userName: " + company.getName());
        for (Company.Person person : personList) {
            System.out.println(person);
        }
    }
}