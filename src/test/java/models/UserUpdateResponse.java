package models;

import lombok.Data;

@Data
public class UserUpdateResponse {
    private String name;
    private String job;
    private String age;
    private String updatedAt;
}