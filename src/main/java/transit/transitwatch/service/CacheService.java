package transit.transitwatch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import transit.transitwatch.dto.common.CommonApiDTO;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
@Service
public class CacheService {
    private final RedisTemplate<String, Object> cacheRedisTemplate;

    public void saveCache(String key, Object data, int timeout, TimeUnit timeUnit) {
        cacheRedisTemplate.opsForValue().set(key, data, timeout, timeUnit);
    }

    public Object getCache(String key) {
        return cacheRedisTemplate.opsForValue().get(key);
    }

    public <T> T getCache(String key, Supplier<T> tSupplier, int timeout, TimeUnit timeUnit) {
       T data = (T) cacheRedisTemplate.opsForValue().get(key);

        if (data == null) {
            data = tSupplier.get();
            if (data != null && data instanceof CommonApiDTO && !((CommonApiDTO)data).isItemListNull()) {
                saveCache(key, data, timeout, timeUnit);
            }
        }
        return data;
    }

    public <T> Optional<T> getCacheOptional(String key, Supplier<Optional<T>> tSupplier, int timeout, TimeUnit timeUnit) {
        T data = (T) cacheRedisTemplate.opsForValue().get(key);
        if (data == null) {
            Optional<T> result = tSupplier.get();
            result.ifPresent(t -> saveCache(key, t, timeout, timeUnit));
            return result;
        }
        return Optional.of(data);
    }
}
