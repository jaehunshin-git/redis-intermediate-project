package com.example.redisintermediateproject.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

  private final StockService stockService;
  private final StockLockService stockLockService;

  @PostMapping("/{id}/decrease")
  public void decrease(
          @PathVariable Long id
  ) {
    stockService.decrease(id);
  }

  @PostMapping("/{id}/decrease/redis")
  public void decreaseWithRedis(@PathVariable Long id) throws InterruptedException {
    stockLockService.decrease(id);

  }
}
