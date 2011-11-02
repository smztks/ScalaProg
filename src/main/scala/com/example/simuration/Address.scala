package com.example.simuration

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11/11/02
 * Time: 13:50
 * To change this template use File | Settings | File Templates.
 */

trait Adders extends Circuit {
  def halfAdder(a: Wire, b: Wire, s: Wire, c: Wire) {
    val d, e = new Wire
    orGate(a, b, d)
    andGate(a, b, c)
    inverter(c, e)
    andGate(d, e, s)
  }

  def fullAdder(a: Wire, b: Wire, cin: Wire, sum: Wire, cout: Wire) {
    val s, c1, c2 = new Wire
    halfAdder(a, cin, s, c1)
    halfAdder(b, s, sum, c2)
    orGate(c1, c2, cout)
  }
}