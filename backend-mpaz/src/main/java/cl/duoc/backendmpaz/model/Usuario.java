package cl.duoc.backendmpaz.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String clave;

    @Column(nullable = true)
    private String rut;

    @Column(nullable = true)
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    public Usuario() {}

    public Usuario(String nombre, String correo, String clave, Rol rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.clave = clave;
        this.rol = rol;
    }

    public Usuario(String nombre, String correo, String clave, String rut, String telefono, Rol rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.clave = clave;
        this.rut = rut;
        this.telefono = telefono;
        this.rol = rol;
    }

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getNombre() { 
        return nombre; 
    }
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    public String getCorreo() { 
        return correo; 
    }
    public void setCorreo(String correo) { 
        this.correo = correo; 
    }

    public String getClave() { 
        return clave; 
    }
    public void setClave(String clave) { 
        this.clave = clave; 
    }

    public String getRut() { 
        return rut; 
    }
    public void setRut(String rut) { 
        this.rut = rut; 
    }

    public String getTelefono() { 
        return telefono; 
    }
    public void setTelefono(String telefono) { 
        this.telefono = telefono; 
    }

    public Rol getRol() { 
        return rol; 
    }
    public void setRol(Rol rol) { 
        this.rol = rol; 
    }
}
