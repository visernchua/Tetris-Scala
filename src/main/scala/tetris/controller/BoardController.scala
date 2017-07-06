package tetris.controller

import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button,Label}
import scalafx.scene.layout.{AnchorPane, GridPane}
import scalafxml.core.macros.sfxml
import scalafx.scene.shape.Rectangle
import scalafx.animation._
import scala.util.Random
import scalafx.scene.input.{KeyCode,KeyEvent}
import tetris.model.Tetromino

@sfxml
class BoardController(tetris: AnchorPane, tetrisBoard: GridPane, 
	nextPieceBoard: GridPane, 
	score: Label,
	backButton: Button) {

	// hold all the tetromino
	val tetromino = List(Tetromino.I, Tetromino.J, Tetromino.L, Tetromino.T, Tetromino.O, Tetromino.Z, Tetromino.S)

	// hold the piece with the orientation
	var currentTetromino: List[List[Array[Int]]] = List()

	// hold currentPiece data
	var currentPiece: List[Array[Int]] = List()

	// hold the current orientation number
	var currentZ: Int = 0

	// hold the currentY
	var currentY: Int = 0

	// hold the currentX
	var currentX: Int = 5

	// hold nextPiece data
	var nextPiece: List[List[Array[Int]]] = List()

	// a virtual board that holds all the diePiece data
	var board = Array.ofDim[Int](20, 10)

	// Initialize the board
	for (row <- 0 until 20) {
		for (col <- 0 until 10) {
			board(row)(col) = 0
		}
	}

	// rectangles(row)(col)
	var rectangles: List[List[Rectangle]] = List()
	// create every single rectangles for tetrisBoard
	for (row <- 0 until 20) {
		var tmpRec: List[Rectangle] = List()
		for (col <- 0 until 10) {
			tmpRec = tmpRec :+ new Rectangle {
				width = 27
				height = 27
				fill = "white"
			}
			tetrisBoard.add(tmpRec(col),col,row)
		}
		rectangles = rectangles ++: List(tmpRec)
	}
	
	// create every single rectangles for nextPiece
	var nextPieceRectangles: List[List[Rectangle]] = List()

	// create every single rectangles for nextPieceBoard
	for (row <- 0 until 4) {
		var tmpRec: List[Rectangle] = List()
		for (col <- 0 until 4) {
			tmpRec = tmpRec :+ new Rectangle {
				width = 27
				height = 27
				fill = "white"
			}
			nextPieceBoard.add(tmpRec(col),col,row)
		}
		nextPieceRectangles = nextPieceRectangles ++: List(tmpRec)
	}

	// control
	var leftPressed = false
	var rightPressed = false
	var upPressed = false
	var downPressed = false
	var spacePressed = false
	tetris.onKeyPressed = (e: KeyEvent) => {
		if(e.code == KeyCode.LEFT) leftPressed = true
		if(e.code == KeyCode.RIGHT) rightPressed = true
		if(e.code == KeyCode.UP) upPressed = true
		if(e.code == KeyCode.DOWN) downPressed = true
		if(e.code == KeyCode.SPACE) spacePressed = true
	}
	// tetris.onKeyReleased = (e: KeyEvent) => {
	// 	if(e.code == KeyCode.LEFT) leftPressed = false
	// 	if(e.code == KeyCode.RIGHT) rightPressed = false
	// 	if(e.code == KeyCode.UP) upPressed = false
	// 	if(e.code == KeyCode.DOWN) downPressed = false
	// 	if(e.code == KeyCode.SPACE) spacePressed = false
	// }

	// for animationTimer
	var time = 0L

	//animationTimer
	val timer: AnimationTimer = AnimationTimer(t => {

		// if nextPiece is empty, get new piece from randomPiece
		if (nextPiece.isEmpty) {
			nextPiece = randomPiece()
			for (a <- 0 until nextPiece(0).size) {
				nextPieceRectangles(nextPiece(0)(a)(1))(nextPiece(0)(a)(0)+1).fill = "red"
			}
		}

		// if currentPiece is empty, get new one
		// randomPiece
		if (currentPiece.isEmpty) {
			var tmpPiece = nextPiece
			currentTetromino = tmpPiece
			for (a <- 0 until nextPiece(0).size) {
				nextPieceRectangles(nextPiece(0)(a)(1))(nextPiece(0)(a)(0)+1).fill = "white"
			}
			nextPiece = List()
			currentZ = 0
			currentX = 5
			currentY = 0
			currentPiece = currentTetromino(0)
			for (a <- 0 until currentPiece.size) {
				// rectangles(x)(y)
				// if y + 1, move right
				// if y - 1, move left
				// if x + 1, move down
				// if x - 1, move up
				rectangles(currentPiece(a)(1)+currentY)(currentPiece(a)(0)+currentX).fill = "blue"
			}
		}

		if(leftPressed) {
			move(-1,0)
			leftPressed = false
		}
		if(rightPressed) {
			// go right
			move(1,0)
			rightPressed = false
		}
		if(upPressed) {
			// rotate
			clearPieceFromBoard(currentPiece,currentX,currentY)
			currentPiece = rotate()
			paintPieceToBoard(currentPiece,currentX,currentY)
			upPressed = false
		}
		if(downPressed) {
			move(0,1)
			downPressed = false
		}
		if(spacePressed) {
			print("space")
		}

		//collisionDetection(currentPiece,rectangles)

		// make the body of this if statement to run every second
		if ((t - time) > 1e+9) {
			move(0,1)

			time = t
		}

	})

	timer.start

	// rotate the piece
	def rotate(): List[Array[Int]] = {
		// if (currentZ + 1 >= currentTetromino.size) {
		// 	if (isRotatable(currentTetromino(0))) {
		// 		currentZ = 0
		// 		currentTetromino(currentZ)
		// 	} else {
		// 		currentTetromino(currentZ)
		// 	}
		// } else {
		// 	if (isRotatable(currentTetromino(currentZ+1))) {
		// 		currentZ += 1
		// 		currentTetromino(currentZ)
		// 	} else {
		// 		currentTetromino(currentZ)
		// 	}
		// }

		if (currentZ + 1 >= currentTetromino.size) {
			currentZ = 0
			currentTetromino(currentZ)
		} else {
			currentZ += 1
			currentTetromino(currentZ)
		}
	}

	// def isRotatable(piece: List[Array[Int]]): Boolean = {
	// 	for (a <- 0 until currentPiece.size) {
	// 		if (currentPiece(a)(0) >= 10 || currentPiece(a)(0) < 0) {
	// 			return false
	// 		}

	// 		if (currentPiece(a)(1) >= 20) {
	// 			return false
	// 		}
	// 	}
	// 	return true
	// }

	// paint the virtual board
	// Got ERROR in this code
	def paintBoard(currentX: Int, currentY: Int) {
		for (a <- 0 until currentPiece.size) {
			var tmpCol = currentPiece(a)(0) + currentX
			var tmpRow = currentPiece(a)(1) + currentY
			board(tmpRow)(tmpCol) = 1
		}
		refreshBoard()
	}
	// check virtual board
	// use virtual board length
	// if it collide, save the piece to virtual board
	// refresh the baord
	// refresh the whole board
	// reset currentPiece
	// set currentPiece = nextPiece
	// reset nextPiece
	// call random to get next piece
	def collisionDetection(piece: List[Array[Int]], currentX: Int, currentY: Int): Boolean = {
		// for (a <- 0 until piece.size) {
		// 	if (board(piece(a)(0)+currentX)(piece(a)(1)+currentY+1) == 1 || (currentPiece(a)(1)+currentY+1) >= 20) {
		// 		return true
		// 	}
		// }
		return false
	}

	// bind with virtual board
	def refreshBoard() = {
		// row
		for (row <- 0 until 20) {
			// column
			for (col <- 0 until 10) {
				// if it occupy, fill blue, else white
				if (board(row)(col) == 1) {
					rectangles(row)(col).fill = "blue"
				} else {
					rectangles(row)(col).fill = "white"
				}
			}
		}
	}

	// return a random tetromino
	def randomPiece(): List[List[Array[Int]]] = {
		return tetromino(Random.nextInt(tetromino.size))
	}

	// move method, pass the x and y to move left,right,up,down
	// pass 0 if you dont want to move the block
	def move(x: Int, y: Int): Unit = {

		for (a <- 0 until currentPiece.size) {
			// check if it hits the side of the board
			if (currentPiece(a)(0) + currentX + x >= 10 || currentPiece(a)(0) + currentX + x < 0) {
				return
			}

			// check if it hits the bottom of the board
			// include collisionDetection
			// if (collisionDetection(currentPiece,currentX,currentY)) {
			// 	paintBoard(currentPiece,currentX,currentY)
			// 	currentPiece = List()
			// 	return
			// }
			if (currentPiece(a)(1) + currentY + y >= 20) {
				//error
				paintBoard((currentX),(currentY))
				currentPiece = List()
				return
			}
		}
		clearPieceFromBoard(currentPiece,currentX,currentY)
		currentX += x
		currentY += y
		paintPieceToBoard(currentPiece,currentX,currentY)
	}

	def clearPieceFromBoard(piece: List[Array[Int]], currentX: Int, currentY: Int) = {
		// paint the board back to white according to the piece
		for (a <- 0 until piece.size) {
			rectangles(piece(a)(1)+currentY)(piece(a)(0)+currentX).fill = "white"
		}
	}


	def paintPieceToBoard(piece: List[Array[Int]], currentX: Int, currentY: Int) = {
		// paint the board back blue according to the piece
		for (a <- 0 until piece.size) {
			rectangles(piece(a)(1)+currentY)(piece(a)(0)+currentX).fill = "blue"
		}
	}

}