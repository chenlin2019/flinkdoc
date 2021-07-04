package com.atguigu.day08

import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.annotation._
import org.apache.flink.table.api._
import org.apache.flink.table.api.bridge.scala._
import org.apache.flink.table.functions._
import org.apache.flink.types.Row

import scala.reflect.internal.util.NoFile.output

object TableFunctionExample {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val stream: DataStream[String] = env.fromElements(
      "hello#word",
      "atguigu#bigdata"
    )



    val settings = EnvironmentSettings
      .newInstance()
      .useBlinkPlanner()
      .inStreamingMode()
      .build()


    // 初始化表环境
    val tEnv = StreamTableEnvironment.create(env, settings)
    tEnv.createTemporaryView("MyTable", stream, $"s")

    // table 写法
    tEnv.createTemporarySystemFunction("Split", classOf[Split])
    val tableResult = tEnv
      .from("MyTable")
      .joinLateral(call("Split", $"s"))
      .select($"s", $"word", $"length")

    tEnv.toAppendStream[Row](tableResult).print()



    // sql写法
    val table: Table = tEnv.fromDataStream(stream,$"s")
    val split = new Split("#")
    tEnv.registerFunction("split",split)
    tEnv.createTemporaryView("t",table) // 定义临时表 表名为t
//    tEnv
      // T 的意思是元祖，flink里的固定语法
//      .sqlQuery("select s,word,length from t ,LATERAL TABLE(split(s) as T(word,length))")
//      .sqlQuery("select s,word,length from t left join LATERAL TABLE(split(s) as T(word,length) on true)")
//      .toAppendStream[Row]
//      .print()

  }

    // 输出的泛型是(string ,int)

  /**
   *
   * @param sep 根据sep 分割
   */
  @FunctionHint(output = new DataTypeHint("ROW<word STRING, length INT>"))
  class Split(sep:String) extends TableFunction[(String,Int)]{


    def eval(s:String):Unit={
      // 使用collect方法向下游发送数据
//      string.split("#").foreach(s => collect(Row.of(s, Int.box(s.length))))
    }
  }

}
