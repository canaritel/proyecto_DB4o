package database;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.ta.TransparentActivationSupport;

public class Conexion {

    private static Conexion INSTANCE = null;

    private final String PATH = "./datos.db4o";
    private static ObjectContainer db;

    //El patrón Singleton se implementa mediante una clase con un constructor privado.
    private Conexion() {
    }

    //Establecemos un método sincronizado para evitar problemnas datos en la DB y que cree la conexión
    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Conexion();
            INSTANCE.performConnection();
        }
    }

    // Conecta con la Base de Datos (si el fichero no existe lo creará)
    public void performConnection() {
        EmbeddedConfiguration configuracion = Db4oEmbedded.newConfiguration();
        configuracion.common().add(new TransparentActivationSupport());
        //db = Db4oEmbedded.openFile(configuracion, PATH);
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), PATH);
    }

    public static ObjectContainer getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return db;

    }

    public static void closeConnection() {
        db.close();
    }

}
