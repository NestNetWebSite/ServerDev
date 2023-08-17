package NestNet.NestNetWebSite.api;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LoginApiResult<T> {

    private final boolean success;
    private final T accessTokenResponse;
    private final T refreshTokenResponse;
    private final ApiError error;

    public LoginApiResult(boolean success, T accessTokenResponse, T refreshTokenResponse, ApiError apiError) {
        this.success = success;
        this.accessTokenResponse = accessTokenResponse;
        this.refreshTokenResponse = refreshTokenResponse;
        this.error = apiError;
    }

    public static <T> LoginApiResult<T> success(T accessTokenResponse, T refreshTokenResponse) {

        return new LoginApiResult<>(true, accessTokenResponse, refreshTokenResponse, null);
    }

    public static <T> LoginApiResult<?> error(HttpStatus status, String message) {

        return new LoginApiResult<>(false, null, null, new ApiError(status, message));
    }

}

