package tetris
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import javafx.{scene => jfxs}
import scalafx.stage.{Stage, Modality}
import scalafx.event.ActionEvent

object MainApp extends JFXApp {


    // transform path of RootLayout.fxml to InputStream for resource location.
    val rootResource = getClass.getResourceAsStream("view/Board.fxml")
    // initialize the loader object.
    val loader = new FXMLLoader(null, NoDependencyResolver)
    // Load root layout from fxml file.
    loader.load(rootResource);
    // retrieve the root component BorderPane from the FXML 
    val roots = loader.getRoot[jfxs.layout.AnchorPane]

    stage = new PrimaryStage {
        title = "AddressApp"
        scene = new Scene {
          root = roots
        }
    }
    stage.setResizable(false)
}