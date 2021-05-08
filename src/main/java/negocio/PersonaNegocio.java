package negocio;

import datos.PersonaDAO;

import entidades.PersonaClass;
import java.time.Instant;
import java.util.Date;

public class PersonaNegocio {

    private final PersonaDAO DATOS;
    private final PersonaClass nuevoObjeto;

    public PersonaNegocio() {
        this.DATOS = new PersonaDAO();
        this.nuevoObjeto = new PersonaClass();
    }

    public String actualizar(String usuario, String clave, PersonaClass viejoObjeto) {
        nuevoObjeto.setNombre(usuario);
        nuevoObjeto.setPassword(clave);
        nuevoObjeto.setFechaEntrada(viejoObjeto.getFechaEntrada());
        if (DATOS.actualizar(nuevoObjeto, viejoObjeto)) {
            return "OK";
        } else {
            return "Error en la actualización";
        }
    }

    public String insertar(String usuario, String clave) {
        nuevoObjeto.setNombre(usuario);
        nuevoObjeto.setPassword(clave);
        Date date = Date.from(Instant.now());
        nuevoObjeto.setFechaEntrada(date);

        if (DATOS.crear(nuevoObjeto)) {
            return "OK";
        } else {
            return "Error en el registro";
        }
    }

    public String eliminar(PersonaClass viejoObjeto) {
        if (DATOS.eliminar(viejoObjeto)) {
            return "OK";
        } else {
            return "Error al eliminar registro";
        }
    }

    public String leer(String usuario, String clave) {
        nuevoObjeto.setNombre(usuario);
        nuevoObjeto.setPassword(clave);
        if (DATOS.leer(nuevoObjeto)) {
            return "OK";
        } else {
            return "No existe ese registro";
        }
    }

}
