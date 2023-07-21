package NestNet.NestNetWebSite.api;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

    public static <T> ApiResult<?> error(int status, String message) {

        return new ApiResult<>(false, null, new ApiError(status, message));
    }

}
