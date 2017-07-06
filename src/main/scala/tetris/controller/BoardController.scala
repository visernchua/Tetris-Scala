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
	var nextPiece: List[Array[Int]] = List()

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

	// create every single rectangles
	for (row <- 0 until 20) {
		var tmpRec: List[Rectangle] = List()
		for (col <- 0 until 10) {
			tmpRec = tmpRec :+ new Rectangle {
				width = 30
				height = 30
				fill = "white"
			}
			tetrisBoard.add(tmpRec(col),col,row)
		}
		rectangles = rectangles ++: List(tmpRec)
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
	tetris.onKeyReleased = (e: KeyEvent) => {
		if(e.code == KeyCode.LEFT) leftPressed = false
		if(e.code == KeyCode.RIGHT) rightPressed = false
		if(e.code == KeyCode.UP) upPressed = false
		if(e.code == KeyCode.DOWN) downPressed = false
		if(e.code == KeyCode.SPACE) spacePressed = false
	}

	// for animationTimer
	var time = 0L

	//animationTimer
	val timer: AnimationTimer = AnimationTimer(t => {


		// if currentPiece is empty, get new one
		// randomPiece
		if (currentPiece.isEmpty) {
			currentTetromino = randomPiece()
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
			clearCurrentPieceFromBoard()
			currentPiece = rotate()
			paintCurrentPieceToBoard()			
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
		currentZ += 1
		if (currentZ >= currentTetromino.size) {
			currentZ = 0
			currentTetromino(currentZ)
		} else {
			currentTetromino(currentZ)
		}
	}


	// paint the virtual board
	def paintBoard(piece: List[Array[Int]], currentY: Int, currentX: Int) {
		for (a <- 0 until piece.size) {
			board(piece(a)(0)+currentX)(piece(a)(1)+currentY)
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
	def collisionDetection(piece: List[Array[Int]], board: List[List[Rectangle]]) = {
		// for (a <- 0 until piece.size) {
		// 	if (board(piece(a)(1)+1)(piece(a)(0)).getFill.equals("0x0000ffff")) {
		// 	 	println("true")
		// 	}
		// }
		// println("false")
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
			if (currentPiece(a)(0) + currentX + x >= 10 || currentPiece(a)(0) + currentX + x < 0) {
				return
			}

			if (currentPiece(a)(0) + currentY + y >= 20) {
				return
			}
		}
		clearCurrentPieceFromBoard()
		currentX += x
		currentY += y
		paintCurrentPieceToBoard()
	}

	def clearCurrentPieceFromBoard() = {
		// paint the board back to white according to the currentPiece
		for (a <- 0 until currentPiece.size) {
			rectangles(currentPiece(a)(1)+currentY)(currentPiece(a)(0)+currentX).fill = "white"
		}
	}

	def paintCurrentPieceToBoard() = {
		// paint the board back blue according to the currentPiece
		for (a <- 0 until currentPiece.size) {
			rectangles(currentPiece(a)(1)+currentY)(currentPiece(a)(0)+currentX).fill = "blue"
		}
	}

}