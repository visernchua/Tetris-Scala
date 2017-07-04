package tetris.controller

import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button,Label}
import scalafx.scene.layout.{BorderPane,AnchorPane}
import scalafxml.core.macros.sfxml
import scalafx.scene.shape.Rectangle
import javafx.{scene => jfxs}
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import tetris.MainApp

@sfxml
class MainViewController() {

	def beginner() = {
        val resource = getClass.getResourceAsStream("view/Board.fxml")
        val loader = new FXMLLoader(null, NoDependencyResolver)
        loader.load(resource);
        val root = loader.getRoot[jfxs.layout.AnchorPane]
        // MainApp.roots.setCenter(root)
    }

    def intermediate() = {
        
    }

    def advance() = {
        
    }
}