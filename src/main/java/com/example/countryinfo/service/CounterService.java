package com.example.countryinfo.service;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class CounterService {
  static AtomicInteger requestCounter = new AtomicInteger(0);

  public void increment() {
    requestCounter.incrementAndGet();
  }

  public static int get() {
    return requestCounter.get();
  }
}