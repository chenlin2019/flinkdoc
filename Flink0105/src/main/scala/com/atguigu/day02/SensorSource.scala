package com.atguigu.day02

import java.util.Calendar

import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction

import scala.util.Random

/**
 * 自定义数据源 继承RichParallelSourceFunction接口
 *
 * 泛型是SensorReading，表名产生的流中的时间额度类型是SensorReading
 */


class SensorSource extends RichParallelSourceFunction[SensorReading] {
  var running = true

  override def run(ctx: SourceContext[SensorReading]): Unit = {
    val rand = new Random

    var curFTemp = (1 to 10).map(i => ("sensor_" + i, rand.nextGaussian() * 20))

    // 无限发送数据
    while (running) {
      curFTemp = curFTemp.map(t => (t._1, t._2 + (rand.nextGaussian() * 0.5)))

      // 产生一个毫秒时间戳
      val curTime = Calendar.getInstance.getTimeInMillis

      curFTemp.foreach(t => ctx.collect(SensorReading(t._1, curTime, t._2)))

      Thread.sleep(1000L)
    }
  }

  // 取消发送数据
  override def cancel(): Unit = running = false
}
