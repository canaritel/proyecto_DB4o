package entidades;

import java.util.Date;

public class PersonaClass {

    private String nombre;
    private String password;
    private Date fechaEntrada;

    public PersonaClass() {
    }

    public PersonaClass(String nombre, String password, Date fechaEntrada) {
        this.nombre = nombre;
        this.password = password;
        this.fechaEntrada = fechaEntrada;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    @Override
    public String toString() {
        return "PersonaClass{" + "nombre=" + nombre + ", password=" + password + ", fechaEntrada=" + fechaEntrada + '}';
    }

}
