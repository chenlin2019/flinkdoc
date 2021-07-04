package com.atguigu.day02

import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.streaming.api.scala._


/**
 * map 案例
 */
object mapExample {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val stream = env.addSource(new SensorSource)
    // 方式1：
    stream.map(r=>r.id).print()
    // 方式2：
    stream.map(new MyMapFunction()).print()
    // 方式3：
    stream.map(new MapFunction[SensorReading,String]{
      override def map(value: SensorReading): String = value.id
    }).print()



    env.execute()
  }


  /**
   *  MapFunction[输入,输出]
   */
  class MyMapFunction extends MapFunction[SensorReading,String]{
    override def map(value: SensorReading): String = value.id
  }

}
