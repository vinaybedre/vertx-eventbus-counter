package com.vinaybedre.vertx.counter;

import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.web.RoutingContext;

import java.util.function.Function;

import static java.text.MessageFormat.format;

public class CounterHandler {
  private final CounterRepository counterRepository;

  public CounterHandler(SharedData sharedData) {
    this.counterRepository = new CounterRepository(sharedData);
  }

  public void incrementCounter(RoutingContext context) {
    handleUpdateCounter(context, counterRepository::increment);
  }

  public void decrementCounter(RoutingContext context) {
    handleUpdateCounter(context, counterRepository::decrement);
  }

  private void handleUpdateCounter(RoutingContext context, Function<String, Future<Counter>> updateMethod){
    String counterId = context.request().getParam("counterId");
    updateMethod.apply(counterId)
      .setHandler(ar -> {
        if (ar.failed()) {
          context.response()
            .setStatusCode(500)
            .setStatusMessage(ar.cause().toString())
            .end();
          return;
        }

        Counter updatedCounter = ar.result();
        context.vertx().eventBus().publish(format("counter.{0}", counterId), Json.encode(updatedCounter));
        context.json(updatedCounter);
      });
  }


  public void getCurrentCounter(RoutingContext context) {
    String counterId = context.request().getParam("counterId");
    counterRepository.getCurrentCounter(counterId).setHandler(ar -> {
      if (ar.failed()) {
        return;
      }
      Counter currentCounter = ar.result();
      context.json(currentCounter);
    });
  }
}
