import io.restassured.http.Header;

public class TestData {
    private TestData() {}
    public static final Header API_KEY = new Header("x-api-key", "reqres_5652f8a1d58a47aba967c9c460e7a15d");
    public static final String VALID_DATA = "{\"name\": \"Karlos\", \"job\": \"lawyer\"}";
    public static final String EMPTY_DATA = "{}";
    public static final String EMPTY_BODY = "";
    public static final String EXTRA_DATA = "{\"name\": \"Sophi\", \"job\": \"devops\", \"age\": \"32\"}";
}
