package com.vinaybedre.vertx.counter;

public class Counter {
  private Long value;

  public Counter(Long value) {
    this.value = value;
  }

  public Long getValue() {
    return value;
  }

  public void setValue(Long value) {
    this.value = value;
  }
}
