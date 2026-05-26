package com.example.redisintermediateproject.like;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping()
    public void likePost(
            @RequestBody LikePostRequestDto likePostRequestDto
    ) {
        likeService.likePost(likePostRequestDto);
    }

    @PostMapping("/redis")
    public void likePostWithRedis(
            @RequestBody LikePostRequestDto likePostRequestDto
    ) {
        likeService.likePostWithRedis(likePostRequestDto);
    }
}
