package NestNet.NestNetWebSite.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;

    // 데이터 저장. duration 지나면 레디스에서 데이터 자동 삭제. (밀리초)
    public void setData(String key, String value, long duration) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofMillis(duration);      //밀리초로 설정
        valueOperations.set(key, value, expireDuration);
    }

    // 데이터 저장. duration 지나면 레디스에서 데이터 자동 삭제. (단위 설정 가능)
    public void setData(String key, String value, long duration, TimeUnit timeUnit) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, duration, timeUnit);
    }

    // 레디스에서 key에 해당하는 데이터 조회
    public String getData(String key){

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    // 데이터 삭제
    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    // 키 존재 여부
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
