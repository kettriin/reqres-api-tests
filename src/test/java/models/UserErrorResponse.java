package models;
import lombok.Data;

@Data
public class UserErrorResponse {
    private String error;
    private String message;
}
