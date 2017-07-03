package tetris.controller

import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button,Label}
import scalafx.scene.layout.GridPane
import scalafxml.core.macros.sfxml
import scalafx.scene.shape.Rectangle

@sfxml
class BoardController(tetrisBoard: GridPane, 
	nextPieceBoard: GridPane, 
	score: Label,
	backButton: Button) {
	tetrisBoard.add(new Rectangle(), 1,1)
}