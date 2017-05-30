import scala.collection.JavaConversions._

object PieceFactory {

  val L1: Array[Array[Int]] = Array(Array(1, 1), Array(0, 1), Array(0, 1))

  val L2: Array[Array[Int]] = Array(Array(0, 1), Array(0, 1), Array(1, 1))

  val T: Array[Array[Int]] = Array(Array(0, 1), Array(1, 1), Array(0, 1))

  val BOX: Array[Array[Int]] = Array(Array(1, 1), Array(1, 1))

  val BAR: Array[Array[Int]] = Array(Array(1, 1, 1, 1))

  val STEP1: Array[Array[Int]] = Array(Array(1, 0), Array(1, 1), Array(0, 1))

  val STEP2: Array[Array[Int]] = Array(Array(0, 1), Array(1, 1), Array(1, 0))

  val SHAPES: Array[Array[Array[Int]]] =
    Array(L1, L2, T, BOX, BAR, STEP1, STEP2)

  def createPiece(): Piece = {
  	val s: Array[Array[Int]] = SHAPES((Math.random() * SHAPES.length).toInt)
  	(Math.random() * 10).toInt match {
  		case _ => return new Piece(s)
  	}
  }

}