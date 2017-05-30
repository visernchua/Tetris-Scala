import java.awt._

import Board._

import scala.collection.JavaConversions._

object Board {

  val COLUMNS: Int = 16

  val ROWS: Int = 32

  val BLUE: Color = new Color(0, 0, 128, 40)

}

class Board extends Grid(Array.ofDim[Int](ROWS, COLUMNS)) {

  setSize(COLUMNS * Tetris.SQUARE_SIZE, ROWS * Tetris.SQUARE_SIZE)

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    g.setColor(BLUE)
    paintStripes(g)
  }

  def paintStripes(g: Graphics): Unit = {
    var i: Int = 0
    while (i < COLUMNS) {
      g.fillRect(i * Tetris.SQUARE_SIZE,
                 0,
                 Tetris.SQUARE_SIZE * 2,
                 Tetris.SQUARE_SIZE * ROWS)
      i += 4
    }
  }

}