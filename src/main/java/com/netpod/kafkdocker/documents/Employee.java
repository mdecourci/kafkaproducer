package com.netpod.kafkdocker.documents;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@Data
@Builder(builderClassName = "Builder", toBuilder = true)
public class Employee {
    @Id
    private String id;
    private String email;
    private String fullName;
    private String managerEmail;

    @Tolerate
    public Employee() {
    }

    public boolean isEmpty() {
        return StringUtils.isEmpty(email) && StringUtils.isEmpty(fullName) && StringUtils.isEmpty(managerEmail);
    }

    public static class Builder {

        public Employee.Builder message(final String msg) {
            if(StringUtils.isEmpty(msg)) {
                return this;
            }
            String[] fields = StringUtils.split(msg, ",");
            if (fields != null) {
                for (String field : fields) {
                    if (field.contains("fullName")) {
                        this.fullName(StringUtils.split(field, "=")[1]);
                    } else if (field.contains("email")) {
                        this.email(StringUtils.split(field, "=")[1]);
                    } else if (field.contains("managerEmail")) {
                        this.managerEmail(StringUtils.split(field, "=")[1]);
                    }
                }
            }
            return this;

        }
    }
    
    //    public static Employee.Builder parseMessage(final String pMessage) {
//        String[] fields = StringUtils.split(pMessage, ",");
//        Employee.Builder builder = Employee.builder();
//        for (String field : fields) {
//            if (field.contains("fullName")) {
//                builder.fullName(StringUtils.split(field, "=")[1]);
//            } else if (field.contains("email")) {
//                builder.email(StringUtils.split(field, "=")[1]);
//            } else if (field.contains("managerEmail")) {
//                builder.managerEmail(StringUtils.split(field, "=")[1]);
//            }
//        }
//        return builder;
//    }
}
