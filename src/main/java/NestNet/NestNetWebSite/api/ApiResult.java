package NestNet.NestNetWebSite.api;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@Getter
public class ApiResult<T> {

    private final boolean success;
    private final T response;
    private final ApiError error;

    public ApiResult(boolean success, T response, ApiError apiError) {
        this.success = success;
        this.response = response;
        this.error = apiError;
    }

    public static <T> ApiResult<T> success(T response) {

        return new ApiResult<>(true, response, null);
    }

    public static <T> ApiResult<?> error(HttpServletResponse response, HttpStatus status, String message) {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        return new ApiResult<>(false, null, new ApiError(status, message));
    }

}
