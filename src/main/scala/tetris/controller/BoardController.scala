package tetris.controller

import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button,Label}
import scalafx.scene.layout.GridPane
import scalafxml.core.macros.sfxml
import scalafx.scene.shape.Rectangle
import scalafx.animation._

@sfxml
class BoardController(tetrisBoard: GridPane, 
	nextPieceBoard: GridPane, 
	score: Label,
	backButton: Button) {

	var pieces: List[Int]= List()

	var piece = Array(0,0)

	var counter: Int = 0
	var time = 0L
	val timer = AnimationTimer(t => {
		//println("t: "+t)
		//println("Time: "+time)
		if ((t - time) > 1e9) {
		
		tetrisBoard.add(new Rectangle {
		width = 30
		height = 30
		fill = "blue"}, piece(0), piece(1))
			counter = counter + 1
			time = t
			piece(1) = piece(1) + 1
		}
		})
		

	timer.start

	def refreshBoard() = {
		tetrisBoard.children.clear()
	}
}