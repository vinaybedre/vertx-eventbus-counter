package com.vinaybedre.vertx.counter;

import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;

import java.util.Optional;

public class CounterRepository {
  private final SharedData sharedData;
  private static final String COUNTER = "counter";

  public CounterRepository(SharedData sharedData) {
    this.sharedData = sharedData;
  }

  public Counter increment(String counterId){
    LocalMap<String, Integer> localMap = this.sharedData.getLocalMap(COUNTER);
    Integer currentValue = Optional.ofNullable(localMap.get(counterId)).orElse(0);
    currentValue++;
    localMap.put(counterId,currentValue);
    return new Counter(currentValue);
  }

  public Counter decrement(String counterId){
    LocalMap<String, Integer> localMap = this.sharedData.getLocalMap(COUNTER);
    Integer currentValue = Optional.ofNullable(localMap.get(counterId)).orElse(0);
    currentValue--;
    localMap.put(counterId,currentValue);
    return new Counter(currentValue);
  }

  public Counter getCurrentCounter(String counterId){
    LocalMap<String, Integer> localMap = this.sharedData.getLocalMap(COUNTER);
    Integer currentValue = Optional.ofNullable(localMap.get(counterId)).orElse(0);
    return new Counter(currentValue);
  }
}
