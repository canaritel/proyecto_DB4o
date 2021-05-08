package controlador;

import datos.PersonaDAO;
import entidades.PersonaClass;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import negocio.PersonaNegocio;

/**
 * FXML Controller class
 *
 * @author telev
 */
public class PrincipalVistaController implements Initializable {

    @FXML
    private TableView<PersonaClass> tablaPersonas;
    @FXML
    private TableColumn<PersonaClass, String> colUser;
    @FXML
    private TableColumn<PersonaClass, String> colPassword;
    @FXML
    private TableColumn<PersonaClass, Date> colFecha;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtUsuario;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnCancelar;

    private ObservableList<PersonaClass> items; //instanciamos un objeto tipo ObservableList especial para tablas en JavaFX
    private PersonaDAO datosTabla;   //instanciamos la clase PersonaDAO la cual gestiona las acciones hacia nuestra BD
    private PersonaNegocio CONTROL;  //instacioamos la clase PersonaNegocio para gestionar el CRUD desde una capa superior
    private PersonaClass persona;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datosTabla = new PersonaDAO();  //instanciamos un objeto para listae la tabla
        CONTROL = new PersonaNegocio(); //instaciamos un objeto para realizar CRUD
        this.cargarTabla(""); //cargamos la tabla de Personas
    }

    @FXML
    private void cerrarAplicacion(ActionEvent event) {
        if (MensajeFX.printTexto("¿Desea salir de la aplicación?", "CONFIRM", obtenPosicionX_Y())) {
            Platform.exit(); //cuando se cierra la aplicación se ejecuta el proceso stop() de App.java
        }
    }

    @FXML
    private void posicionRatonTabla(MouseEvent event) {
        //cuando pulsamos con el ratón en algún registro de la tabla capturamos la información de la fila
        persona = new PersonaClass();
        persona = tablaPersonas.getSelectionModel().getSelectedItem();
        if (persona != null) {  //si no es NULL capturamos los datos de la fila
            txtUsuario.setText(persona.getNombre());
            txtPassword.setText(persona.getPassword());
        }
    }

    @FXML
    private void botonPulsado(ActionEvent event) {
        Object evt = event.getSource();
        String respuesta;
        //Comprobamos que hayan datos en los campos textfield
        if (comprobarCampos()) {
            try {
                //Si pulsamos en Cancelar
                if (evt.equals(btnCancelar)) {
                    this.limpiarDatos();
                }
                //Si pulsamos en Editar
                if (evt.equals(btnEditar) && validarCampos()) {
                    respuesta = CONTROL.actualizar(txtUsuario.getText(), txtPassword.getText(), persona);

                    //validamos la respuesta
                    if ("OK".equals(respuesta)) {
                        MensajeFX.printTexto("Se ha edidato correctamente", "INFO", obtenPosicionX_Y());
                        this.cargarTabla("");
                        this.limpiarDatos();
                    } else {
                        MensajeFX.printTexto("Fallo al editar el registro", "ERROR", obtenPosicionX_Y());
                    }
                }
                //Si pulsamos en Eliminar
                if (evt.equals(btnEliminar)) {
                    if ("OK".equals(CONTROL.leer(txtUsuario.getText(), txtPassword.getText()))) {
                        respuesta = CONTROL.eliminar(persona);
                        //validamos la respuesta
                        if ("OK".equals(respuesta)) {
                            MensajeFX.printTexto("Se ha eliminado correctamente", "INFO", obtenPosicionX_Y());
                            this.cargarTabla("");
                            this.limpiarDatos();
                        } else {
                            MensajeFX.printTexto("Fallo al eliminar el registro", "ERROR", obtenPosicionX_Y());
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    @FXML
    private void teclaPulsada(KeyEvent event) {
        //No permitimos que se introduzcan espacios en los campos textfield
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

    private void cargarTabla(String filtro) {
        //asignamos a cada columna de la tabla el valor de su campo referenciado en PersonaClass
        PropertyValueFactory<PersonaClass, String> valorCol1 = new PropertyValueFactory<>("nombre");
        this.colUser.setCellValueFactory(valorCol1);
        PropertyValueFactory<PersonaClass, String> valorCol2 = new PropertyValueFactory<>("password");
        this.colPassword.setCellValueFactory(valorCol2);
        PropertyValueFactory<PersonaClass, Date> valorCol3 = new PropertyValueFactory<>("fechaEntrada");
        this.colFecha.setCellValueFactory(valorCol3);

        items = datosTabla.lista();  //llamamos al método "listar" dentro de la clase PesonaDAO
        this.tablaPersonas.refresh();  //refrescamos los datosTabla de la tabla (sobre todo es interesante cuando actualizamos)
        this.tablaPersonas.setItems(items); //mostramos las columnas de la tabla
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

    private void limpiarDatos() {
        txtUsuario.setText("");
        txtPassword.setText("");
    }

    private boolean comprobarCampos() {
        boolean resp = true;
        if (txtUsuario.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            resp = false;
        }
        return resp;
    }

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
        return true; //si llega aquí es que todo está correcto
    }

}
