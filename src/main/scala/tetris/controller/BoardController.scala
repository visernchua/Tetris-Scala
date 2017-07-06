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

	// hold nextPiece data
	var nextPiece: List[Array[Int]] = List()

	// a virtual board that holds all the diePiece data
	var board = Array.ofDim[Int](20, 10)

	for (row <- 0 until 20) {
		for (col <- 0 until 10) {
			board(row)(col) = 0
		}
	}

	// example of a piece
	//var piece = List(Array(0,0),Array(1,0),Array(2,0),Array(3,0))

	//print(Tetromino.I(0).size)

	//Tetris shape
	// for (a <- 0 until Tetromino.I(0).size) {
	// 	tetrisBoard.add(new Rectangle {
	// 		width = 30
	// 		height = 30
	// 		fill = "red"
	// 		},Tetromino.I(1)(a)(1)+1, Tetromino.I(1)(a)(0)+1)
	// }

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

	var counter: Int = 0
	var time = 0L

	//var piece = List(Array(0,0),Array(1,0),Array(2,0),Array(3,0))


	//animation for Tetromino.I
	val timer: AnimationTimer = AnimationTimer(t => {
		
		// if currentPiece is empty, get new one
		// now just for testing, hardcoded
		if (currentPiece.isEmpty) {
			currentTetromino = randomPiece()
			currentPiece = currentTetromino(0)
			for (a <- 0 until currentPiece.size) {
				// rectangles(x)(y)
				// if y + 1, move right
				// if y - 1, move left
				// if x + 1, move down
				// if x - 1, move up
				rectangles(currentPiece(a)(1))(currentPiece(a)(0)).fill = "blue"
			}
		}

		//collisionDetection(currentPiece,rectangles)

		// make the body of this if statement to run every second
		if ((t - time) > 1e+9) {
			// paint the board back white
			for (a <- 0 until currentPiece.size) {
				rectangles(currentPiece(a)(1))(currentPiece(a)(0)).fill = "white"
			}
			var rotated = rotate(currentTetromino, currentPiece)
			currentPiece = rotated
			// move down one
			for (a <- 0 until currentPiece.size) {
				currentPiece(a)(1) += 1
			}

			// paint the board back blue
			for (a <- 0 until currentPiece.size) {
				rectangles(currentPiece(a)(1))(currentPiece(a)(0)).fill = "blue"
			}

			time = t
		}


		//println("t: "+t)
		//println("Time: "+time)

		//rectangle.fill="white"

		//rectangles(0)(0).fill = "blue"

		// for (a <- 0 until Tetromino.I(0).size) {
		// tetrisBoard.add(new Rectangle {
		// width = 30
		// height = 30
		// fill = "white"
		// }, Tetromino.I(1)(a)(1), Tetromino.I(1)(a)(0))
		// 	counter = counter + 1
		// 	if (counter >= 75) {
		// 		timer.stop
		// 	}
		// 	time = t
		// 	Tetromino.I(1)(a)(0) = Tetromino.I(1)(a)(0) + 1

		// 	for (b <- 0 until Tetromino.I(0).size) {
		// 	tetrisBoard.add(new Rectangle {
		// 		width = 30
		// 		height = 30
		// 		fill = "blue"
		// 	}, Tetromino.I(1)(b)(1), Tetromino.I(1)(b)(0))
		// }
	// }
	// }

		if(leftPressed) {
			print("left")
		}
		if(rightPressed) {
			print("right")
		}
		if(upPressed) {
			print("up")
		}
		if(downPressed) {
			print("down")
		}
		if(spacePressed) {
			print("space")
		}
	})

	timer.start

	def rotate(tetromino: List[List[Array[Int]]], piece: List[Array[Int]]): List[Array[Int]] = {
		for (a <- 0 until tetromino.size) {
			if (tetromino(a) == piece) {
				if (a + 1 >= tetromino.size) {
					println(0)
					return tetromino(0)
				} else {
					println(a + 1)
					return tetromino(a+1)
				}
			}
		}
		// if it goes to this line means got error
		println("Error: ")
		return tetromino(0)
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
		for (row <- 0 until 20) {
			for (col <- 0 until 10) {
				if (board(row)(col) == 1) {
					rectangles(row)(col).fill = "blue"
				} else {
					rectangles(row)(col).fill = "white"
				}
			}
		}
	}

	def randomPiece(): List[List[Array[Int]]] = {
		return tetromino(Random.nextInt(tetromino.size))
	}

}