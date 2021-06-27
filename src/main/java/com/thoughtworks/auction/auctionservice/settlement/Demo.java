package com.thoughtworks.auction.auctionservice.settlement;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Demo {

  public static void main(String[] args)
      throws InterruptedException, ExecutionException, TimeoutException {

    CompletableFuture<String> completableFuture = new CompletableFuture<>();

    Executors.newCachedThreadPool().submit(() -> {
      Thread.sleep(500);
      System.out.println("hello");
      completableFuture.complete("Hello");
      return null;
    });
    try {
      completableFuture.cancel(false);
      completableFuture.get();
    } catch (Exception timeoutException) {
      System.out.println(timeoutException);
    }
  }
}
