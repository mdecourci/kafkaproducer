package com.netpod.kafkdocker.documents;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class EmployeeTest {

    @Test
    public void message() {
        String msg = "fullName=James Bond,email=james.bond@gmail.com,managerEmail=m.boss@gmail.com";
        Employee employee = Employee.builder().message(msg).build();
        assertThat(employee.getFullName(), is("James Bond"));
        assertThat(employee.getEmail(), is("james.bond@gmail.com"));
        assertThat(employee.getManagerEmail(), is("m.boss@gmail.com"));

    }

    @Test
    public void filter() {
        List<String> messages =
                Arrays.asList("fullName=James Bond,email=james.bond@gmail.com,managerEmail=m.boss@gmail.com",
                        "fullName=Harvey Nicols,email=harvey.nicols@gmail.com,managerEmail=harveys.boss@gmail.com");

        String searchEmail = "harvey.nicols@gmail.com";
        String message =  messages.stream().filter(s -> s.contains(searchEmail)).findFirst().get();
        Employee employee = Employee.builder().message(message).build();

        assertThat(employee.getFullName(), is("Harvey Nicols"));
        assertThat(employee.getEmail(), is("harvey.nicols@gmail.com"));
        assertThat(employee.getManagerEmail(), is("harveys.boss@gmail.com"));

    }
}