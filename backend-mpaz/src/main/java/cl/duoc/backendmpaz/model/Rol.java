package cl.duoc.backendmpaz.model;

import jakarta.persistence.Column;
import jakarta.persistence.*;

@Entity
@Table(name = "rol")
public class Rol {
    @Id
    private Integer id; 

    @Column(nullable = false, unique = true)
    private String nombre;

    public Rol() {}

    public Rol(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}

