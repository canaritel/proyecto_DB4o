package com.televoip.practica_guiada_db4o;

import controlador.LoginVistaController;
import datos.PersonaDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.image.Image;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;   //donde se produce la acción con los elementos creados
    private static Stage stage;   //el maro de la ventana actual

    @Override
    public void init() {  //Primer método que se ejecuta al instanciar la clase
        //Usado para validaciones con bases de datos
        //Cargar configuración inicial
        //NO vale para cargar componentes de nuestra interfaz gráfica
        System.out.println("Método init()");
    }

    @Override
    public void start(Stage stage) throws IOException {  //Cargamos todo lo referente a nuestra interfaz gráfica
        //cargamos la vista FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginVista.fxml"));
        //instanciamos y cargamos el FXML en el padre
        Parent root = loader.load();
        //instanciamos al controlador LoginVistaController haciendo uso del nuevo método getController
        LoginVistaController ctrLogin = loader.getController();
        //creamos la nueva escena que viene del padre
        scene = new Scene(root);
        stage = new Stage();    //creamos la nueva ventana
        stage.setScene(scene); //establecemos la escena
        //Cargamos el resto de componentes de la vista
        stage.setTitle("Creado en JavaFX");
        //Cargamos el icono en la ventana
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/icons8_java_duke_50px.png")));
        stage.setResizable(false); //no permitimos que la ventana cambie de tamaño
        stage.show(); //mostramos la ventana
        System.out.println("Método start()");
        System.out.println("Java version: " + System.getProperty("java.version") + "\nJavaFX version: " + System.getProperty("javafx.version"));

    }

    @Override
    public void stop() { //Creamos procesos de finalización
        System.out.println("Método stop()");
        PersonaDAO personaDao = new PersonaDAO();
        personaDao.cerrar();  //cerramos la base de datos
        System.out.println("Base de datos cerrada");
    }

    public static void runApp(String[] args) {
        launch(args);
    }

    public void cerrar() {
        Platform.exit(); //Es ideal para cerrar la aplicación. Además ejecuta el proceso stop()
    }

}
