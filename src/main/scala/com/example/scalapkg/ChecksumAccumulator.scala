/*************************************************
//P.084
*/

package com.example.scalapkg

//companion_objects

import scala.collection.mutable.Map

class ChecksumAccumulator {
  private var sum = 0
  def add(b: Byte): Unit = sum += b
  def checkSum(): Int = ~(sum & 0xFF) + 1
}

object ChecksumAccumulator {
  private val cache = Map[String, Int]()
  def calculate(s: String): Int = 
    if(cache.contains(s)) {
      println("cache: " + s + "/" + cache(s))
      cache(s)
    } else {
      val acc = new ChecksumAccumulator
      for(c <-s) {
        acc.add(c.toByte)
        println(c + ": " + c.toByte)
      }
      val cs = acc.checkSum()
      println("checkSum: " + s + "/" + cs)
      cache += (s -> cs)
      cs
    }
}

/*
// val acc = new ChecksumAccumulator	// don't make instance
println("1st:" + ChecksumAccumulator.calculate("1234") + "\n")
println("2nd:" + ChecksumAccumulator.calculate("1111") + "\n")
println("3rd:" + ChecksumAccumulator.calculate("1234") + "\n")
*/