package datos;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import database.Conexion;
import entidades.PersonaClass;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PersonaDAO {

    private final ObjectContainer CON;
    private boolean resp;

    public PersonaDAO() {
        CON = Conexion.getInstance();
    }

    public boolean crear(PersonaClass objeto) {
        resp = false;

        try {
            CON.store(objeto);
            CON.commit();
            resp = true;
        } catch (DatabaseClosedException | DatabaseReadOnlyException | Db4oIOException e) {
            System.err.println("Error crear objeto " + e);
        }

        return resp;
    }

    public boolean leer(PersonaClass objeto) {
        resp = false;

        ObjectSet<PersonaClass> result = CON.queryByExample(objeto);
        if (result.hasNext()) {
            PersonaClass temporal = result.next();
            resp = true;
        }

        return resp;
    }

    public boolean actualizar(PersonaClass nuevoObjeto, PersonaClass viejoObjeto) {
        resp = false;

        ObjectSet<PersonaClass> result = CON.queryByExample(viejoObjeto);
        if (result.hasNext()) {
            PersonaClass temporal = result.next();
            temporal.setNombre(nuevoObjeto.getNombre());
            temporal.setPassword(nuevoObjeto.getPassword());
            temporal.setFechaEntrada(viejoObjeto.getFechaEntrada());
            CON.store(temporal);
            CON.commit();
            resp = true;
        }

        return resp;
    }

    public boolean eliminar(PersonaClass viejoObjeto) {
        resp = false;

        ObjectSet<PersonaClass> result = CON.queryByExample(viejoObjeto);
        if (result.hasNext()) {
            PersonaClass temporal = result.next();
            CON.delete(temporal);
            CON.commit();
            resp = true;
        }

        return resp;
    }

    public ObservableList<PersonaClass> lista() {
        PersonaClass persona = new PersonaClass();
        //Creamos un ObservablearrayList donde guardar los datos de nuestra tabla
        ObservableList<PersonaClass> registros = FXCollections.observableArrayList(); //Especial para javaFX
        // Lista todos los objetos del tipo que se pasa como parámetro
        List<PersonaClass> personas = CON.query(PersonaClass.class);
        //Recorremos todo el List y grabamos en el ObservableList
        for (PersonaClass registro : personas) {
            registros.add(new PersonaClass(registro.getNombre(), registro.getPassword(), registro.getFechaEntrada()));
        }

        return registros;
    }

    public void cerrar() {
        CON.close();
    }

}
