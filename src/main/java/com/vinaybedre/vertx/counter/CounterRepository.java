package com.vinaybedre.vertx.counter;

import io.vertx.core.Future;
import io.vertx.core.shareddata.SharedData;

import java.util.Optional;
import java.util.function.UnaryOperator;

public class CounterRepository {
  private static final String COUNTER = "counter";
  private static final String LOCK = "counterLock";
  private final SharedData sharedData;

  public CounterRepository(SharedData sharedData) {
    this.sharedData = sharedData;
  }

  public Future<Counter> increment(String counterId) {
    return updateAndGet(counterId,value->value+1);
  }

  public Future<Counter> decrement(String counterId) {
    return updateAndGet(counterId,value->value-1);
  }

  public Future<Counter> getCurrentCounter(String counterId) {
    return updateAndGet(counterId,value->value);
  }

  private Future<Counter> updateAndGet(String counterId, UnaryOperator<Long> updateFunction){
    return this.sharedData.getLock(LOCK)
      .compose(lock ->
        this.sharedData.<String, Long>getAsyncMap(COUNTER)
          .compose(asyncMap -> asyncMap.get(counterId)
            .map(nullableValue -> {
              Long value = Optional.ofNullable(nullableValue).orElse(0L);
              value = updateFunction.apply(value);
              asyncMap.put(counterId, value)
                      .onComplete((ar) -> lock.release());
              return new Counter(value);
            })));
  }
}
