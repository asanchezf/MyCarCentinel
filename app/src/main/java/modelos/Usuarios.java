package modelos;

/**
 * Created by Susana on 23/09/2016.
 */

public class Usuarios {

    private int Id;
    private String Username,Password,Email,ID_Android,Telefono,FechaCreacion,FechaModificacion,Observaciones;
    //private long FechaCreacion;
    /*{"Id":"1","Username":"Antonio","Password":"1","Email":"antoniom.sanchezf@gmail.com",
    "ID_Android":"644961f49d160c93","Telefono":"659355808","FechaCreacion":"2016-05-10 00:00:00"}*/

    public String getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        FechaCreacion = fechaCreacion;
    }

    public String getFechaModificacion() {
        return FechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        FechaModificacion = fechaModificacion;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }

    public String getUsername() {
        return Username;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getID_Android() {
        return ID_Android;
    }

    public void setID_Android(String ID_Android) {
        this.ID_Android = ID_Android;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }



    @Override
    public String toString() {
        return "Usuarios{" +
                "Id=" + Id +
                ", Username='" + Username + '\'' +
                ", Password='" + Password + '\'' +
                ", Email='" + Email + '\'' +
                ", ID_Android='" + ID_Android + '\'' +
                ", Telefono='" + Telefono + '\'' +
                ", FechaCreacion='" + FechaCreacion + '\'' +
                '}';
    }
}
