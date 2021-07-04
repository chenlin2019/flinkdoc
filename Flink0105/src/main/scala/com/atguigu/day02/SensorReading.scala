package com.atguigu.day02

/**
 *  传感器读数样例类
 * @param id 传感器id
 * @param timestamp 时间戳
 * @param temperature 温度值
 */
case class SensorReading(id: String, timestamp: Long, temperature: Double)
