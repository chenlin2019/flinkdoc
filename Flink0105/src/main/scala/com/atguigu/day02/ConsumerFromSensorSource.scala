package com.atguigu.day02

import org.apache.flink.streaming.api.scala._

/**
 * 消费=》自定义数据源SensorSource
 */

object SensorStream {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val stream = env.addSource(new SensorSource)

    stream.print()

    env.execute()
  }
}