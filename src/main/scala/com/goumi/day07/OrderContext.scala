package com.goumi.day07

object OrderContext {
  /*implicit val manOrdering: Man => Ordering[Man1] = (man:Man)=>new Ordering[Man1]{
    override def compare(x: Man1, y: Man1) = {
      if(x.fv == y.fv){
        x.age - y.age
      } else{
        java.lang.Double.compare(y.fv, x.fv)
      }
    }
  }*/

  implicit object OrderingMan extends Ordering[Man1] {
    override def compare(x: Man1, y: Man1): Int = {
      if(x.fv == y.fv) {
        x.age - y.age
      } else {
        java.lang.Double.compare(y.fv, x.fv)
      }
    }
  }
}