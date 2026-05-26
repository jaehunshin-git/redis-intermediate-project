package com.example.redisintermediateproject.like;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public void likePost(LikePostRequestDto likePostRequestDto) {
        Long userId = likePostRequestDto.getUserId();
        Long postId = likePostRequestDto.getPostId();
        Like like = new Like(userId, postId);
        likeRepository.save(like);
    }

    public void likePostWithRedis(LikePostRequestDto likePostRequestDto) {
        Long userId = likePostRequestDto.getUserId();
        Long postId = likePostRequestDto.getPostId();

        String value = userId + ":" + postId;

        // RPUSH like_queue [value]
        redisTemplate.opsForList().rightPush("like_queue", value);
    }
}
