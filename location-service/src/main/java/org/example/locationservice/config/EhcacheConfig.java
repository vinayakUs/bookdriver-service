package org.example.locationservice.config;

import com.google.maps.model.AutocompletePrediction;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.example.locationservice.model.dto.PlaceDetailDto;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;

@Configuration
@EnableCaching
public class EhcacheConfig {

    @Bean
    public CacheManager ehcacheManager() {
        // Get the default CachingProvider (Ehcache)
        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager = provider.getCacheManager();

        // Define cache configuration
        CacheConfiguration<String, AutocompletePrediction[]> cacheConfiguration = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(
                        String.class, // Key type
                        AutocompletePrediction[].class, // Value type
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .heap(50, EntryUnit.ENTRIES) // Heap size
                )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(60))) // TTL
                .build();

        // Define cache configuration
        CacheConfiguration<String, PlaceDetailDto> PlaceDetailCache = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(
                        String.class, // Key type
                        PlaceDetailDto.class, // Value type
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .heap(50, EntryUnit.ENTRIES) // Heap size
                )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(60))) // TTL
                .build();







        // Create or get the cache
        javax.cache.configuration.Configuration<String, AutocompletePrediction[]> configuration =
                Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfiguration);
        cacheManager.createCache("autocompleteCache", configuration);

        // Create or get the cache
        javax.cache.configuration.Configuration<String, PlaceDetailDto> placeDetailConfiguration =
                Eh107Configuration.fromEhcacheCacheConfiguration(PlaceDetailCache);
        cacheManager.createCache("placeDetailCache", placeDetailConfiguration);

        return cacheManager;
    }
}