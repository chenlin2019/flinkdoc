package com.atguigu.day02

import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object FlatMapExample {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val stream = env.fromElements(data = "white","gray","black")
    // 方式1：flatmap 针对流中的每个元素，生成0个，1个，或多个数据
    stream.flatMap(new MyFlatMapFunction).print()
    // 方式2： 实现自定义接口的方式

    // 方式3：实现匿名接口的方式



    env.execute()
  }

  /**
   * FlatMapFunction
   */
  class MyFlatMapFunction extends FlatMapFunction [String,String] {
    // 调用out.collect(value) 方法，将数据发送到下游
    override def flatMap(value: String, out: Collector[String]): Unit = {
      if (value.equals("white")){
        out.collect(value)
      }else if( value.equals("black")){
        out.collect(value)
        out.collect(value)
      }
    }
  }
}
