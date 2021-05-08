package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import negocio.PersonaNegocio;

/**
 * FXML Controller class
 *
 * @author telev
 */
public class LoginVistaController implements Initializable {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnAcceder;
    @FXML
    private RadioButton radioLogin;
    @FXML
    private RadioButton radioRegistro;

    private ToggleGroup tg; //creamos una variable para el grupo de radiobuttons
    private PersonaNegocio CONTROL; //creamos un objeto de tipo Negocio para gestionar el CRUD
    private static Scene scene;   //variable de clase Scene donde se produce la acción con los elementos creados
    private static Stage stage;   //variable de clase Stage que es la ventana actual

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Agrupamos los radiobutton
        tg = new ToggleGroup();
        this.radioLogin.setToggleGroup(tg);
        this.radioRegistro.setToggleGroup(tg);
    }

    /**
     * Este metodo NO permite que se introduzcan espacios en blanco en las
     * cadenas de texto a introducir
     *
     * @param event Envíamos el evento de tipo KeyEvent
     * @return Nada.
     */
    @FXML
    private void teclaPulsada(KeyEvent event) {
        Object evt = event.getSource();
        if (evt.equals(txtUsuario)) {
            if (" ".equals(event.getCharacter())) {
                txtUsuario.deletePreviousChar();
            }
        } else if (evt.equals(txtPassword)) {
            if (" ".equals(event.getCharacter())) {
                txtPassword.deletePreviousChar();
            }
        }
    }

    @FXML
    private void comprobarAcceder(ActionEvent event) {
        if (validarCampos()) {
            validarAcceso();
        }
    }

    /**
     * Este metodo se usa para validar los campos del frm
     *
     * @return true si están todos los campos correctos y false si hay algún
     * campo no validado
     */
    public boolean validarCampos() {
        if (txtUsuario.getText().length() == 0 || txtUsuario.getText().length() > 10) {
            MensajeFX.printTexto("El campo USUARIO debe tener un tamaño entre 1 a 10 carácteres", "WARNING", obtenPosicionX_Y());
            txtUsuario.requestFocus();
            return false;
        }

        if (txtPassword.getText().isEmpty() || txtPassword.getText().length() > 9) {
            MensajeFX.printTexto("El campo CLAVE debe tener un tamaño entre 1 a 9 carácteres", "WARNING", obtenPosicionX_Y());
            txtPassword.requestFocus();
            return false;
        }
        //comprobamos sino tiene seleccionado un checkBox
        if (!seleccionCheckRadio()) {
            MensajeFX.printTexto("Seleccione una forma de conexión", "WARNING", obtenPosicionX_Y());
            return false;
        }
        return true; //si llega aquí es que todo está correcto
    }

    /**
     * Este método se usa para saber si se ha seleccionado un radioButton.
     *
     * @return true si se ha seleccionado y false si no hay nada seleccionado
     */
    public boolean seleccionCheckRadio() {
        if (tg.getSelectedToggle() != null) {
            return true;
        } else {
            return false;
        }
    }

    //Con los datos de usuario y password comprobamos la conexión a la BD
    public void validarAcceso() {
        String textoRadioButton;
        String respuesta;
        try {
            //Instanciamos un objeto de tipo acceso DAO
            this.CONTROL = new PersonaNegocio();

            RadioButton rb = (RadioButton) tg.getSelectedToggle();
            textoRadioButton = rb.getText();

            switch (textoRadioButton) {
                case "Logearse":
                    System.out.println("Has elegido logearte");
                    respuesta = CONTROL.leer(txtUsuario.getText(), txtPassword.getText());
                    //validamos la respuesta
                    if ("OK".equals(respuesta)) {
                        //MensajeFX.printTexto("Logeo correcto", "INFO", obtenPosicionX_Y());
                        this.cargarPrincipalVista(); //iniciamos correctamente
                    } else {
                        MensajeFX.printTexto("Datos de acceso incorrectos", "ERROR", obtenPosicionX_Y());
                    }

                    break;

                case "Registrarse":
                    System.out.println("Has elegido registrarse");
                    if (!"OK".equals(CONTROL.leer(txtUsuario.getText(), txtPassword.getText()))) {
                        respuesta = CONTROL.insertar(txtUsuario.getText(), txtPassword.getText());
                        //validamos la respuesta
                        if ("OK".equals(respuesta)) {
                            //MensajeFX.printTexto("Acceso creado correctamente", "INFO", obtenPosicionX_Y());
                            this.cargarPrincipalVista(); //iniciamos correctamente
                        } else {
                            MensajeFX.printTexto("Error al crear el acceso", "ERROR", obtenPosicionX_Y());
                        }
                    } else {
                        MensajeFX.printTexto("Ese registro ya existe", "WARNING", obtenPosicionX_Y());
                    }
                    break;

            }

        } catch (RuntimeException ex) {
            System.err.println("Error validarAcceso " + ex);
        }

    }

    //Se ejecuta si los datos de acceso han sido correctos
    public void cargarPrincipalVista() {
        try {
            //cargamos la vista FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PrincipalVista.fxml"));
            //instanciamos y cargamos el FXML en el padre
            Parent root = loader.load();
            //instanciamos el controlador haciendo uso del nuevo método getController
            PrincipalVistaController ctrPrincipal = loader.getController();
            //creamos la nueva escena que viene del padre
            scene = new Scene(root);
            stage = new Stage();    //creamos la nueva ventana
            stage.setTitle("Menú principal JavaFX"); //ponemos un título
            stage.setScene(scene);
            //Cargamos el icono en la ventana
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/icons8_java_duke_50px.png")));
            //Cargamos el resto de componentes de la vista
            stage.setMinWidth(700);
            stage.setMinHeight(500);
            stage.show();   //mostramos la nueva ventana
            //Al activar la nueva ventana es prefirible cerrar la ventana anterior abierta, para lo cual vamos a realizar los siguientes pasos
            //******* Para cerrar la ventana "anterior" realizamos estos pasos *******
            Stage myStage = (Stage) this.btnAcceder.getScene().getWindow();  //creamos una nueva ventana temporal capturando de cualquier btn/txt la scena y ventana
            //se entiende que los btn o txt forman parte de la ventana que deseamos cerrar.
            myStage.close();  //cerramos la ventana y de esta forma solo veremos la nueva ventana
        } catch (IOException ex) {
            System.err.println("Error en el inicio validado " + ex);
        }
    }

    //este método obtiene la posición de la actual ventana en coordenadas x, y
    //vamos a usar estos datos para posicionar la ventana de mensajes en la pantalla correctamente
    public double[] obtenPosicionX_Y() {
        double[] posicionxy = new double[2];
        //creamos una nueva ventana temporal capturando de cualquier btn/lbl la escena y ventana
        //se entiende que los btn o lbl forman parte de la ventana que deseamos obtener datos
        Stage myStage = (Stage) this.txtUsuario.getScene().getWindow();
        int frmX = 460 / 2; //tamaño ancho componente FrmAlumno
        int frmY = 480 / 2; //tamaño alto componente FrmAlumno
        int x = (int) (myStage.getWidth() / 2);
        int y = (int) (myStage.getHeight() / 2);
        posicionxy[0] = myStage.getX() + (x - frmX);
        posicionxy[1] = myStage.getY() + (y - frmY);
        return posicionxy;
    }

}
