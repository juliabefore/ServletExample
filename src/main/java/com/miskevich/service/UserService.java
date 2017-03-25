package com.miskevich.service;

import com.miskevich.beans.User;
import com.miskevich.db.QueryGenerator;

import java.util.Arrays;
import java.util.Map;

public abstract class UserService {
    public static User populateUser(Map<String, String[]> parameterMap){
        User user = new User();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()){
            String array = Arrays.toString(entry.getValue());
            String value = "";
            if(array.length() > 2){
                value = array.substring(1, array.length() - 1);
            }

            if(entry.getKey().equals("firstName")){
                user.setFirstName(value);
            }else if(entry.getKey().equals("lastName")){
                user.setLastName(value);
            }else if(entry.getKey().equals("dateOfBirth")){
                user.setDateOfBirth(QueryGenerator.convertDate(value));
            }else if(entry.getKey().equals("salary")){
                if(!value.isEmpty()){
                    user.setSalary(Long.valueOf(value));
                }
            }
        }
        return user;
    }
}
