package com.atguigu.day02

import com.atguigu.day02.mapExample.MyMapFunction
import org.apache.flink.api.common.functions.{FilterFunction, MapFunction}
import org.apache.flink.streaming.api.scala._

object filterExample {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val stream = env.addSource(new SensorSource)
    // 方式1：
    stream.filter(r => r.id.equals("sensor_1")).print()
    // 方式2： 实现自定义接口的方式
    stream.filter(new MyFliterFunction()).print()
    // 方式3：实现匿名接口的方式
    stream.filter(new FilterFunction[SensorReading] {
      override def filter(value: SensorReading): Boolean = value.id.equals("sensor_1")
    }).print()


    env.execute()
  }

  /**
   * filter 算子输入和输出的泛型是一样的，所以只有一个泛型SensorReading
   */
  class MyFliterFunction extends FilterFunction[SensorReading] {
    override def filter(value: SensorReading): Boolean = value.id.equals("sensor_1")
  }
}
