import javax.swing._

import java.awt._

import java.awt.event._

import Tetris._

import scala.collection.JavaConversions._

object Tetris {

// 10 by 10 pixels
  val SQUARE_SIZE: Int = 10

  var board: Board = _

  var game: Tetris = _

  def getBoard(): Board = board

  def getGame(): Tetris = game

  def sleep(milliseconds: Int): Unit = {
    try Thread.sleep(milliseconds)
    catch {
      case ie: InterruptedException => {}

    }
  }

  def main(args: Array[String]): Unit = {
    val game: Tetris = new Tetris()
    while (true) game.dropPiece()
  }

}

class Tetris extends JFrame("Tetris") with KeyListener {

  var mainPanel: JPanel = new JPanel()

  var currentPiece: Piece = _

  var score: Int = 0

  var scoreButton: JButton = new JButton("0")

  game = this

  val pane: Container = getContentPane

  pane.setLayout(new BorderLayout())

  scoreButton.setEnabled(false)

  pane.add(scoreButton, BorderLayout.NORTH)

  board = new Board()

  mainPanel.setLayout(null)

  mainPanel.add(board)

  mainPanel.setPreferredSize(
    new Dimension(Board.COLUMNS * SQUARE_SIZE, Board.ROWS * SQUARE_SIZE))

  pane.add(mainPanel)

  addKeyListener(this)

  addWindowListener(new WindowAdapter() {
    override def windowClosing(we: WindowEvent): Unit = {
      System.exit(0)
    }
  })

  pack()

  show()

  setResizable(false)

  def addToScore(v: Int): Unit = {
    score += v
    scoreButton.setText("" + score)
  }

  def getScore(): Int = score

  def dropPiece(): Unit = {
    currentPiece = PieceFactory.createPiece()
    mainPanel.add(currentPiece)
    currentPiece.repaint()
    currentPiece.fall()
    //mainPanel.remove(currentPiece)
    board.repaint()
    addToScore(1)
  }

  def keyPressed(event: KeyEvent): Unit = {
    val key: Int = event.getKeyCode
    key match {
// up arrow
      case KeyEvent.VK_UP | KeyEvent.VK_KP_UP =>
        currentPiece.rotateCounterclockwise()
// down arrow
      case KeyEvent.VK_DOWN | KeyEvent.VK_KP_DOWN =>
        currentPiece.rotateClockwise()
// left arrow
      case KeyEvent.VK_LEFT | KeyEvent.VK_KP_LEFT => currentPiece.moveLeft()
// right arrow
      case KeyEvent.VK_RIGHT | KeyEvent.VK_KP_RIGHT => currentPiece.moveRight()
      case //  space bar
          KeyEvent.VK_SPACE =>
        currentPiece.drop()

    }
  }

  def keyReleased(arg0: KeyEvent): Unit = {}

  def keyTyped(arg0: KeyEvent): Unit = {}

}