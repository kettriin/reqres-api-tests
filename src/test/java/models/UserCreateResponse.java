package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCreateResponse {
    private String name;
    private String job;
    private String id;
    private String createdAt;
    private String age;
}