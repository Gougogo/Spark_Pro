package com.goumi.scala.test

object foldDemo {
  def main(args: Array[String]): Unit = {
    val ints = List(1, 2, 3, 4)
    println(ints.fold(1)((x, y) => x - y))

    /*
      def foldLeft[B](z: B)(op: (B, A) => B): B = {
      var result = z
      this foreach (x => result = op(result, x))
      result

      println(ints.fold(1)((x, y) => y - x))  ====3
      res = 1
      res = (1, 1) = 1-1 = 0
      res = (0, 2) = 2-0 = 2
      res = (2, 3) = 3-2 = 1
      res = (1, 4) = 4-1 = 3

      println(ints.fold(1)((x, y) => x - y))  ====-9
      res = 1
      res = (1, 1) = 1-1 = 0
      res = (0, 2) = 0-2 = -2
      res = (-2, 3) = -2-3 = -5
      res = (-5, 4) = -5-4 = -9
    }*/

    println(ints.foldRight(0)((x, y) => x - y))

    /*override def foldRight[B](z: B)(op: (A, B) => B): B =
      reverse.foldLeft(z)((right, left) => op(left, right))

      println(ints.foldRight(0)((x, y) => x - y)) =====-2
      reverse：4 3 2 1

      def foldLeft[B](z: B)(op: (B, A) => B): B = {
      var result = z
      this foreach (x => result = op(result, x))
      result

      res = 0
      res = (0, 4) = 4-0 = 4
      res = (4, 3) = 3-4 = -1
      res = (-1, 2) = 2-(-1) = 3
      res = (3, 1) = 1-3 = -2


      */
  }

}
