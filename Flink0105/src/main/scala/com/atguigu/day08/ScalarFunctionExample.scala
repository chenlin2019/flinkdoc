package com.atguigu.day08

import com.atguigu.day02.{SensorReading, SensorSource}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.{EnvironmentSettings, Table}
import org.apache.flink.table.api._
import org.apache.flink.table.api.bridge.scala._
import org.apache.flink.table.functions.ScalarFunction
import org.apache.flink.types.Row


object ScalarFunctionExample {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val stream: DataStream[SensorReading] = env.addSource(new SensorSource)



    val settings = EnvironmentSettings
      .newInstance()
      .useBlinkPlanner()
      .inStreamingMode()
      .build()


    // 初始化表环境
    val tEnv = StreamTableEnvironment.create(env, settings)


    val hashCode = new HashCode(10)

    // 将临时表转换成table数据类型
    val table: Table = tEnv.fromDataStream(stream)

    table
      .select('id,hashCode('id))
      .toAppendStream[Row]
      .print()


    // TODO: sql 写法
    // 注册udf
    tEnv.registerFunction("hashCode",hashCode)
    // 创建临时表
    tEnv.createTemporaryView("sensor",table)

    tEnv
      .sqlQuery("select id,hashCode(id) from sensor")
      .toAppendStream[Row]
      .print()


    env.execute()
  }


  // 实现自定义udf
  class HashCode(factor:Int) extends ScalarFunction{
    def eval(s:String): Int ={
      s.hashCode*factor
    }

  }
}
