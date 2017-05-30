import scala.collection.JavaConversions._

class Piece(shape: Array[Array[Int]]) extends Grid(shape) {

// current X location on  the board
  var currentX: Int = 7

// current Y location on the board
  var currentY: Double = 0

  updateLocation()

  def updateSize(): Unit = {
    setSize(Tetris.SQUARE_SIZE * getColumns, Tetris.SQUARE_SIZE * getRows)
  }

  def updateLocation(): Unit = {
    setLocation(Tetris.SQUARE_SIZE * currentX,
                (Tetris.SQUARE_SIZE * currentY).toInt)
  }

  def moveDown(): Unit = {
    synchronized {
      currentY += 1.0
      updateLocation()
    }
  }

  def moveLeft(): Unit = {
    synchronized {
      currentX -= 1
      updateLocation()
    }
  }

  def moveRight(): Unit = {
    synchronized {
      currentX += 1
      updateLocation()
    }
  }

  def rotateClockwise(): Unit = {
    synchronized {}
  }

  def rotateCounterclockwise(): Unit = {
    synchronized {}
  }

  def fall(): Unit = {
    while (currentY != 29) {
      moveDown()
      Tetris.sleep(200)
    } 
  }

  def drop(): Unit = {
    synchronized {
      while (currentY != 29) {
        moveDown()
      }
    }
  }

}