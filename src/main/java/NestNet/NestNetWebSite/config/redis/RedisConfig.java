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

    @Value("#{environment['cache.redis.host']}")
    private String host;

    @Value("#{environment['cache.redis.port']}")
    private int port;

    private final RedisProperties redisProperties;

    /*
    레디스 연결
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(host, port);
    }

    /*
    레디스 템플릿을 이용한 데이터 저장, 조회 . 데이터를 직렬화하여 바이트 형태로 전송해야 한다.
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(){

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();        //레디스에 데이터 저장,조회 담당
        redisTemplate.setKeySerializer(new StringRedisSerializer());                //key 직렬화
        redisTemplate.setValueSerializer(new StringRedisSerializer());              //value 직렬화

        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }

}
