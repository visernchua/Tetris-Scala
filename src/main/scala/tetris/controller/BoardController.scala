package tetris.controller

import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button,Label}
import scalafx.scene.layout.GridPane
import scalafxml.core.macros.sfxml
import scalafx.scene.shape.Rectangle
import scalafx.animation._
import scala.util.Random
import scalafx.scene.input.{KeyCode,KeyEvent}
import tetris.model.Tetromino

@sfxml
class BoardController(tetrisBoard: GridPane, 
	nextPieceBoard: GridPane, 
	score: Label,
	backButton: Button) {

	// hold the piece with the orientation
	var tetromino: List[List[Array[Int]]] = List()
	// hold currentPiece data
	var currentPiece: List[Array[Int]] = List()

	// hold nextPiece data
	var nextPiece: List[Array[Int]] = List()

	// hold diePiece data
	var diePiece: List[List[Array[Int]]] = List()

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
	// onKeyPressed = (e:KeyEvent) => {
	// 	if(e.code == KeyCode.LEFT) leftPressed = true
	// 	if(e.code == KeyCode.RIGHT) rightPressed = true
	// 	if(e.code == KeyCode.UP) upPressed = true
	// 	if(e.code == KeyCode.DOWN) downPressed = true
	// }
	// onKeyReleased = (e:KeyEvent) => {
	// 	if(e.code == KeyCode.LEFT) leftPressed = false
	// 	if(e.code == KeyCode.RIGHT) rightPressed = false
	// 	if(e.code == KeyCode.UP) upPressed = false
	// 	if(e.code == KeyCode.DOWN) downPressed = false
	// }

	var counter: Int = 0
	var time = 0L

	//var piece = List(Array(0,0),Array(1,0),Array(2,0),Array(3,0))


	//animation for Tetromino.I
	val timer: AnimationTimer = AnimationTimer(t => {
		
		// if currentPiece is empty, get new one
		// now just for testing, hardcoded
		if (currentPiece.isEmpty) {
			tetromino = Tetromino.I
			currentPiece = Tetromino.I(0)
			for (a <- 0 until currentPiece.size) {
				// rectangles(y)(x)
				// if y + 1, move right
				// if y - 1, move left
				// if x + 1, move down
				// if x - 1, move up
				rectangles(currentPiece(a)(1))(currentPiece(a)(0)).fill = "blue"
			}
		}

		// make the body of this if statement to run every second
		if ((t - time) > 1e+9) {
			// paint the board back white
			for (a <- 0 until currentPiece.size) {
				rectangles(currentPiece(a)(1))(currentPiece(a)(0)).fill = "white"
			}
			var rotated = rotate(tetromino, currentPiece)
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

		// if(leftPressed) {
		// 	rectangles(0)(0).fill = "blue"
		// }
		// if(rightPressed) {
		// 	print("right")
		// }
		// if(upPressed) {
		// 	print("up")
		// }
		// if(downPressed) {
		// 	print("down")
		// }
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

	def collisionDetection(piece: List[Array[Int]], board: List[List[Rectangle]]) = {
		
	}

	def refreshBoard() = {
		tetrisBoard.children.clear()
	}

}