package com.example.scalapkg

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/10/28
 * Time: 15:48
 * To change this template use File | Settings | File Templates.
 */

abstract class CCTherm {
  val description: String
  val yearMade: Int
  val dateObtained: String
  val bookPrice: Int
  val purchasePrice: Int
  val condition: Int
  override def toString = description

  def toXML =
    <cctherm>
      <description>{description}</description>
      <yearMade>{yearMade}</yearMade>
      <dateObtained>{dateObtained}</dateObtained>
      <bookPrice>{bookPrice}</bookPrice>
      <purchasePrice>{purchasePrice}</purchasePrice>
      <condition>{condition}</condition>
    </cctherm>
}