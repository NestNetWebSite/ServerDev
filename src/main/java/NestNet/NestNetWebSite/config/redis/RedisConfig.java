package NestNet.NestNetWebSite.config.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.collections.RedisProperties;

@Getter
@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories        //레디스 리포지토리 활성화
public class RedisConfig {

    @Value("#{environment['spring.data.redis.host']}")
    private String host;

    @Value("#{environment['spring.data.redis.port']}")
    private int port;

    /*
    레디스 연결
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(host, port);
    }

    /*
    redis template을 이용해 데이터 접근한다. RedisTemplate로 레디스 서버에 명령을 수행한다.
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate() {

        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();        // redisTemplate를 받아와서 set, get, delete를 사용

        redisTemplate.setKeySerializer(new StringRedisSerializer());        // 데이터 직렬화
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }
}
