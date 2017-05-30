import javax.swing.JComponent

import java.awt._

import scala.collection.JavaConversions._

class Grid(var contents: Array[Array[Int]]) extends JComponent {

  val d: Dimension = new Dimension(getColumns * Tetris.SQUARE_SIZE,
                                   getRows * Tetris.SQUARE_SIZE)

  setSize(d)

  setPreferredSize(d)

  setOpaque(false)

  def getColumns(): Int = contents(0).length

  def getRows(): Int = contents.length

  def paintSquare(row: Int, col: Int, g: Graphics): Unit = {
    if (contents(row)(col) != 0)
      g.fillRect(Tetris.SQUARE_SIZE * col + 1,
                 Tetris.SQUARE_SIZE * row + 1,
                 Tetris.SQUARE_SIZE - 2,
                 Tetris.SQUARE_SIZE - 2)
  }

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    for (row <- 0 until contents.length; col <- 0 until contents(row).length) {
      paintSquare(row, col, g)
    }
  }

}