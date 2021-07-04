package com.atguigu.day01

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object WordCountFromSocket {

  case class WordWithCount(word: String, count: Int)

  def main(args: Array[String]): Unit = {
    // 获取运行时环境，类似于sparkContext
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //设置分区（又叫并行任务）
    env.setParallelism(1)

    // 建立数据源
    // 需要先启动 ‘nc -lk 9999’ 来发送数据
    val stream: DataStream[String] = env.socketTextStream("127.0.0.1", 9999, '\n')

    // 写对流的转换处理逻辑
    var transformed = stream
      // 使用空格切分输入的字符串
      .flatMap(line => line.split("\\s"))
      // 类似与mr中的map
      .map(w => WordWithCount(w, 1))
      //使用word字段进行分组，shuffie
      .keyBy(0)
      //开了一个5秒钟的滚动窗口
      .timeWindow(Time.seconds(5))
      // 针对count自盾构进行累加操作，类似mr中的reduce
      .sum(1)


    //将计算结果输出到标准输出和
    transformed.print()

    //执行计算逻辑
    env.execute()


  }
}
