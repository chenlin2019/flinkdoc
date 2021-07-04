package com.atguigu.day02

import org.apache.flink.streaming.api.scala._

/**
 * 多条流合并
 */
object unionExample {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val binjing: DataStream[SensorReading] = env
      .addSource(new SensorSource)
      .filter(_.id.equals("sensor_1"))

    val shanghai: DataStream[SensorReading] = env
      .addSource(new SensorSource)
      .filter(_.id.equals("sensor_2"))

    val shenzheng: DataStream[SensorReading] = env
      .addSource(new SensorSource)
      .filter(_.id.equals("sensor_3"))


    // 没有顺序，不会去重，谁先来先合并
    val union: DataStream[SensorReading] = binjing.union(shanghai, shenzheng)


    union.print()


    env.execute()
  }
}
