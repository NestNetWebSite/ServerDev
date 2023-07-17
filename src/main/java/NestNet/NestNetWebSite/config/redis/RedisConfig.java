package NestNet.NestNetWebSite.config.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Getter
@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories        //레디스 리포지토리 활성화
public class RedisConfig {

    @Value("#{environment['cache.redis.host']}")
    private String host;

    @Value("#{environment['cache.redis.port']}")
    private int port;

    /*
    레디스 연결
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(host, port);
    }


}
