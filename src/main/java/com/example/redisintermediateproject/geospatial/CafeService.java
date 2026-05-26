package com.example.redisintermediateproject.geospatial;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CafeService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String GEOSPATIAL_KEY = "cafe";

    public void addCafe(AddCafeRequestDto addCafeRequestDto) {
        // GEOADD [key] [lon] [lat] member
        redisTemplate.opsForGeo()
                .add(
                        GEOSPATIAL_KEY,
                        new Point(
                                addCafeRequestDto.getLongitude(),
                                addCafeRequestDto.getLatitude()),
                        addCafeRequestDto.getName()
                );
    }

    public List<Object> findCafesNearby(double longitude, double latitude, double distanceKm) {

        RedisGeoCommands.GeoSearchCommandArgs args = RedisGeoCommands.GeoSearchCommandArgs
                .newGeoSearchArgs()
                .includeDistance()
                .sortAscending();

        // GEOSEARCH [key] FROMLONLAT [lon] [lat] BYRADIUS [거리] [단위] ASC WITHDIST
        return redisTemplate.opsForGeo().search(
                        GEOSPATIAL_KEY,
                        GeoReference.fromCoordinate(new Point(longitude, latitude)),
                        new Distance(distanceKm, Metrics.KILOMETERS),
                        args
                ).getContent().stream()
                .map(geoLocationGeoResult -> new CafeDistance(
                        geoLocationGeoResult.getContent().getName(),
                        geoLocationGeoResult.getDistance().getValue()
                ))
                .collect(Collectors.toList());
    }

}
