package ecommerce.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis configuration class for the e-commerce application.
 * Configures how data is serialized and stored in Redis cache.
 */
@Configuration
public class RedisConfig {

    /**
     * Creates and configures a RedisTemplate bean for Redis operations.
     * This template handles the serialization/deserialization of keys and values
     * when storing and retrieving data from Redis.
     *
     * @param factory The Redis connection factory automatically injected by Spring
     * @return A configured RedisTemplate instance with custom serializers
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // Initialize a new RedisTemplate with String keys and Object values
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // Set the connection factory to establish connection with Redis server
        template.setConnectionFactory(factory);

        // Configure key serialization: Keys will be stored as plain strings in Redis
        // This makes keys human-readable when inspecting Redis directly
        template.setKeySerializer(new StringRedisSerializer());

        // Configure value serialization: Values will be stored as JSON
        // This allows storing complex objects (like PendingUser) in Redis
        // GenericJackson2JsonRedisSerializer automatically converts Java objects to/from JSON
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // Return the fully configured template for use throughout the application
        return template;
    }
}