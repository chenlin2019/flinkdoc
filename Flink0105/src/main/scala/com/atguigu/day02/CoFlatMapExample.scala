package com.atguigu.day02

import org.apache.flink.streaming.api.functions.co.CoFlatMapFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

object CoFlatMapExample {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val stream1: DataStream[(String,Int)] = env.fromElements(
      ("zhangsan",130),
      ("lisi",100)
    )

    val stream2: DataStream[(String,Int)] = env.fromElements(
      ("zhangsan",35),
      ("lisi",33)
    )

    val connected: ConnectedStreams[(String, Int), (String, Int)] =
      stream1
        .keyBy(_._1)
        .connect(stream2.keyBy(_._1))

    val printed: DataStream[String] = connected.flatMap(new MyCoFlatMapFunction)
    printed.print()


    env.execute()
  }

  class  MyCoFlatMapFunction extends CoFlatMapFunction[(String, Int), (String, Int),String]{
    // map 处理来自第一条流的元素
    override def flatMap1(value: (String, Int), out: Collector[String]): Unit = {
      out.collect(value._1 + "的体重是："+value._2+"斤")
      out.collect(value._1 + "的体重是："+value._2+"斤")

    }
    // map 处理来自第一条流的元素
    override def flatMap2(value: (String, Int), out: Collector[String]): Unit = {
      out.collect(value._1 + "的年龄是："+value._2+"岁")
    }

  }
}
