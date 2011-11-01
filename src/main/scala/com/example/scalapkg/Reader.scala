package com.example.scalapkg

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/31
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */

import java.io._
class Reader(fname: String) {
  private val in =new BufferedReader(new FileReader(fname))
  @throws(classOf[IOException])
  def read() = in.read()
  def readline = in.readLine()
}