package com.vinaybedre.vertx.counter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise){
    vertx.deployVerticle(new CounterVerticle()).setHandler(ar->{
      if(ar.succeeded()){
        startPromise.complete();
      }else{
        startPromise.fail(ar.cause());
      }
    });
  }
}
