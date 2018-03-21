package com.netpod.kafkdocker.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
public class EmployeeDto {
    private String email;
    private String fullName;
    private String managerEmail;

    @Tolerate
    public EmployeeDto() {
    }

    public boolean isEmpty() {
        return StringUtils.isEmpty(email) && StringUtils.isEmpty(fullName) && StringUtils.isEmpty(managerEmail);
    }

    public static class Builder {

        public EmployeeDto.Builder message(final String msg) {
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
}
