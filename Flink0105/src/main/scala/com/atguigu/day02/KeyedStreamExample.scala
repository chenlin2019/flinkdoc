package com.atguigu.day02

import org.apache.flink.streaming.api.scala._

object KeyedStreamExample {
  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val stream: DataStream[SensorReading] = env.addSource(new SensorSource)

    // 泛型变成了两个，第二个泛型是key的类型，相当于在分组的过程中给每个元素添加了key的信息，逻辑上就分开了
    val keyed: KeyedStream[SensorReading, String] = stream.keyBy(_.id)

    // 使用第三个字段来做滚动聚合，求每个传感器流上的最小温度值
    //  每个key分组下第三个字段最小的值 min(2) : 2 是对象中的第三个字段也是就温度
    // 滚动聚合之后类型变成了DataStream
    val min: DataStream[SensorReading] = keyed.min(2)
    min.print()

    // r1,r2代表第一和第二个流对象
    val reduce: DataStream[SensorReading] = keyed.reduce((r1, r2) => SensorReading(r1.id, 0, r2.temperature.min(r2.temperature)))
    reduce.print()


    env.execute()
  }
}
