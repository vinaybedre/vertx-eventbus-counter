package com.vinaybedre.vertx.counter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.ErrorHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

public class CounterVerticle extends AbstractVerticle {

  private CounterHandler counterHandler;

  @Override
  public void start(Promise<Void> startPromise) {
    counterHandler = new CounterHandler(vertx.sharedData());

    Router router = Router.router(vertx);
    router.route("/").handler(StaticHandler.create());
    router.get("/:counterId").handler(counterHandler::getCurrentCounter);
    router.patch("/:counterId/increment").handler(counterHandler::incrementCounter);
    router.patch("/:counterId/decrement").handler(counterHandler::decrementCounter);
    router.mountSubRouter("/eventbus", eventBusHandler());
    router.route().failureHandler(errorHandler());

    vertx.createHttpServer().requestHandler(router).listen(8080, ar -> {
      if (ar.succeeded()) {
        startPromise.complete();
      } else {
        startPromise.fail(ar.cause());
      }
    });
  }

  private Router eventBusHandler() {
    PermittedOptions counterSync = new PermittedOptions()
      .setAddressRegex("counter\\..+");
    BridgeOptions bridgeOptions = new BridgeOptions()
      .addOutboundPermitted(counterSync);
    return SockJSHandler.create(vertx).bridge(bridgeOptions, event -> event.complete(true));
  }

  private ErrorHandler errorHandler() {
    return ErrorHandler.create(true);
  }
}
