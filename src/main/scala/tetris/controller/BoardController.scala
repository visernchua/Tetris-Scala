package tetris.controller

import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button,Label}
import scalafx.scene.layout.{AnchorPane, GridPane}
import scalafx.scene.media.{Media, MediaPlayer}
import scalafx.util.Duration
import java.io.File
import scalafxml.core.macros.sfxml
import scalafx.scene.shape.Rectangle
import scalafx.animation._
import scala.util.Random
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.application.Platform
import scalafx.scene.input.{KeyCode,KeyEvent}
import tetris.model.Tetromino

@sfxml
class BoardController(tetris: AnchorPane, tetrisBoard: GridPane, 
	nextPieceBoard: GridPane, 
	score: Label, showPaused: Label) {

	// hold the scores
	var scores = 0

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

	// whether gameOver or not
	var gameOver = false

	val file = new File("src/main/resources/tetris/sound/rotate.wav")
	val media = new Media(source = file.toURI().toString) 
	val player = new MediaPlayer(media = media)

	val file2 = new File("src/main/resources/tetris/sound/clear.wav")
	val media2= new Media(source = file2.toURI().toString) 
	val player2= new MediaPlayer(media = media2)

	val file3 = new File("src/main/resources/tetris/sound/move.wav")
	val media3= new Media(source = file3.toURI().toString) 
	val player3= new MediaPlayer(media = media3)

	val file4 = new File("src/main/resources/tetris/sound/collision.wav")
	val media4= new Media(source = file4.toURI().toString) 
	val player4= new MediaPlayer(media = media4)

	val file5 = new File("src/main/resources/tetris/sound/gameover.wav")
	val media5= new Media(source = file5.toURI().toString) 
	val player5= new MediaPlayer(media = media5)

	val file6 = new File("src/main/resources/tetris/sound/hitBorder.wav")
	val media6= new Media(source = file6.toURI().toString) 
	val player6= new MediaPlayer(media = media6)

	var sound: List[MediaPlayer] = List(player,player2,player3,player4,player5,player6)

	for (a <- 0 until sound.size) {
	}

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
	var pause = false
	var leftPressed = false
	var rightPressed = false
	var upPressed = false
	var downPressed = false
	var enterPressed = false
	tetris.onKeyPressed = (e: KeyEvent) => {
		if(e.code == KeyCode.LEFT) leftPressed = true
		if(e.code == KeyCode.RIGHT) rightPressed = true
		if(e.code == KeyCode.UP) upPressed = true
		if(e.code == KeyCode.DOWN) downPressed = true
		if(e.code == KeyCode.ENTER) enterPressed = true
		if(e.code == KeyCode.P) {
			if (pause == false && gameOver == false) {
				timer.stop
				showPaused.setText("Game Paused!")
				pause = true
			} else {
				if (pause == true && gameOver == false) {
					timer.start
					showPaused.setText("")
					pause = false
				}
			}
		}
	}

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
			checkRows()
			var tmpPiece = nextPiece
			currentTetromino = tmpPiece
			for (a <- 0 until nextPiece(0).size) {
				nextPieceRectangles(nextPiece(0)(a)(1))(nextPiece(0)(a)(0)+1).fill = "white"
			}
			nextPiece = List()
			currentZ = 0
			currentX = 4
			currentY = 0
			currentPiece = currentTetromino(0)
			if (checkGameOver()) {
				gameOver = true
				timer.stop
				sound(4).play
				val alert = new Alert(AlertType.Information) {
					title = "Game Over"
					headerText = "You Lose!"
					contentText = "You have scored: " + scores.toString
				}
				showPaused.setText("Game Over!")
				Platform.runLater(alert.showAndWait())
			}
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
			if (isRotatable()) {
				sound(0).stop
				sound(0).play
				clearPieceFromBoard(currentPiece,currentX,currentY)
				currentPiece = rotate()
				paintPieceToBoard(currentPiece,currentX,currentY)
			} else {
				sound(3).stop
				sound(3).play
			}
			upPressed = false
		}
		if(downPressed) {
			move(0,1)
			downPressed = false
		}
		if(enterPressed) {
			do {
				move(0,1)
			}
			while (!currentPiece.isEmpty)
			enterPressed = false
		}

		// make the body of this if statement to run every second
		if ((t - time) > 1e+9) {
			move(0,1)

			time = t
		}

	})

	timer.start

	// rotate the piece
	def rotate(): List[Array[Int]] = {
		if (currentZ + 1 >= currentTetromino.size) {
			currentZ = 0
			currentTetromino(currentZ)
		} else {
			currentZ += 1
			currentTetromino(currentZ)
		}
	}

	def isRotatable(): Boolean = {
		var piece = currentTetromino((currentZ + 1) % currentTetromino.size)
		for (a <- 0 until piece.size) {
			// check whether it hits the side
			if ((piece(a)(0) + currentX) >= 10 || (piece(a)(0) + currentX) < 0) {
				return false
			}

			// check whether it hits the bottom
			if ((piece(a)(1) + currentY) >= 20) {
				return false
			}

			// check whether it is occupied
			if ((board((piece(a)(1) + currentY))(piece(a)(0) + currentX) == 1)) {
				return false
			}
		}
		return true
	}

	def checkGameOver(): Boolean = {
		for (a <- 0 until currentPiece.size) {
			if (collisionDetection(currentPiece(a),currentX,currentY)) {
				return true
			}
		}

		for (row <- 0 until 2) {
			for (col <- 0 until 10) {
				if (board(row)(col) == 1) {
					return true
				}
			}
		}

		return false
	}

	// paint the virtual board
	def paintBoard(currentX: Int, currentY: Int) {
		for (a <- 0 until currentPiece.size) {
			var tmpCol = currentPiece(a)(0) + currentX
			var tmpRow = currentPiece(a)(1) + currentY
			board(tmpRow)(tmpCol) = 1
		}
		refreshBoard()
	}

	// clear row
	def checkRows() {
		for (row <- 0 until board.size) {
			if (isRowComplete(row)) {
				clearRow(row)
				rowDrop(row)
				sound(1).stop
				sound(1).play
				refreshBoard()
			}
		}
	}

	// clear the row and add score point
	def clearRow(row: Int) {
		// turn everything on that row to 0
		for (col <- 0 until board(row).size) {
			board(row)(col) = 0
		}
		addScore(100)
	}

	// move all the above down by 1
	def rowDrop(row: Int) {
		for (rows <- row to 0 by -1) {
			for (col <- 0 until board(rows).size) {
				if (board(rows)(col) == 1) {
					board(rows+1)(col) = 1
					board(rows)(col) = 0
				}
			}
		}
	}

	// check whether the row is complete
	def isRowComplete(row: Int): Boolean = {
		for (col <- 0 until board(row).size) {
			if (board(row)(col) == 0) {
				return false
			}
		}
		return true
	}

	// add score
	def addScore(point: Int) {
		scores += point
		score.setText(scores.toString)
	}

	// check virtual board

	// if collide at row, add to board
	// if collide at col, dont let it move
	def collisionDetection(piece: Array[Int], currentX: Int, currentY: Int): Boolean = {
		if (board(piece(1)+currentY)(piece(0)+currentX) == 1) {
			return true
		}
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
				sound(5).stop
				sound(5).play
				return
			}

			// check whether it collide with other pieces when pressed left or right
			if (collisionDetection(currentPiece(a),(currentX + x),(currentY))) {
				sound(3).stop
				sound(3).play
				return
			}

			//println(currentY+y)
			// check if it hits the bottom of the board
			if ((currentPiece(a)(1) + currentY + y) > 19) {
				sound(3).stop
				sound(3).play
				paintBoard(currentX + x,currentY)
				currentPiece = List()
				return
			}

			// check whether it collides the other pieces
			if (collisionDetection(currentPiece(a),(currentX + x),(currentY + y))) {
				sound(3).stop
				sound(3).play
				paintBoard(currentX + x,currentY)
				currentPiece = List()
				return
			}
		}
		sound(2).stop
		sound(2).play

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