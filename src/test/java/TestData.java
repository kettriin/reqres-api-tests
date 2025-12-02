import io.restassured.http.Header;

public class TestData {
    private TestData() {}
    public static final Header apiKey = new Header("x-api-key", "reqres_5652f8a1d58a47aba967c9c460e7a15d");
    public static final String validData = "{\"name\": \"Karlos\", \"job\": \"lawyer\"}";
    public static final String emptyData = "{}";
    public static final String emptyBody = "";
    public static final String extraData = "{\"name\": \"Sophi\", \"job\": \"devops\", \"age\": \"32\"}";
}
