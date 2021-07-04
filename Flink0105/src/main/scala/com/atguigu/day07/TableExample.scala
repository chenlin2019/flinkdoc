package com.atguigu.day07

import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api._
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.table.descriptors._
import org.apache.flink.table.types.DataType
import org.apache.flink.types.Row




object TableExample {
  def main(args: Array[String]): Unit = {


/*    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    //  使用BlinkPlanner，BlinkPlanner 是流批统一的
    // 有关表环境的配置
    val bsSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build()

    // 初始化表环境
    val tEnv = StreamTableEnvironment.create(env, bsSettings)


    tEnv
      .connect(new FileSystem().path("sensor.txt"))//定义表数据来源，外部接入
      .withFormat(new Csv()) //定义从外部系统读取数据之后的格式化方法
      .withSchema(
        new Schema()
          .field("id",DataType)
          .field("timestamp",DataType)
          .field("temperature",DataType)
      ) // 定义表结构
      .createTemporaryTable("inputTable") // 创建临时表


      // 将临时表转换成table数据类型
      val sensorTable: Table = tEnv.from("inputTable")

      //  使用table api 查询
      val result: Table = sensorTable
        .select("id,temperature")
        .filter("id='sensor_1'")

      tEnv.toAppendStream[Row](result).print()


      //  使用sql api 查询
      val result1: Table = tEnv.sqlQuery("SELECT id,temperature FROM inputTable where id = 'sensor_1' ")
    tEnv.toAppendStream[Row](result1).print()

    env.execute()*/
  }
}
