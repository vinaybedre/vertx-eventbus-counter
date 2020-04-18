package com.vinaybedre.vertx.counter;

public class Counter{
  private Integer value;

  public Counter(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }
}
