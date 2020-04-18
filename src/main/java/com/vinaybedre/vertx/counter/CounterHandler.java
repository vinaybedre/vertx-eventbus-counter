package com.vinaybedre.vertx.counter;

import io.vertx.core.json.Json;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.web.RoutingContext;

import static java.text.MessageFormat.format;

public class CounterHandler {
  private final CounterRepository counterRepository;

  public CounterHandler(SharedData sharedData) {
    this.counterRepository = new CounterRepository(sharedData);
  }

  public void incrementCounter(RoutingContext context) {
    String counterId = context.request().getParam("counterId");
    Counter updatedCounter = counterRepository.increment(counterId);
    context.vertx().eventBus().publish(format("counter.{0}",counterId), Json.encode(updatedCounter));
    context.json(updatedCounter);
  }

  public void decrementCounter(RoutingContext context) {
    String counterId = context.request().getParam("counterId");
    Counter updatedCounter = counterRepository.decrement(counterId);
    context.vertx().eventBus().publish(format("counter.{0}",counterId), Json.encode(updatedCounter));
    context.json(updatedCounter);
  }

  public void getCurrentCounter(RoutingContext context) {
    String counterId = context.request().getParam("counterId");
    Counter currentCounter = counterRepository.getCurrentCounter(counterId);
    context.json(currentCounter);
  }
}
