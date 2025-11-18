package entities;

public class Pista {
    private int id;
    private int historiaId;
    private String nombre;
    private String descripcion;
    private String tipo;
    private int puntos;
    private boolean obligatoria;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getHistoriaId() { return historiaId; }
    public void setHistoriaId(int historiaId) { this.historiaId = historiaId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = puntos; }

    public boolean isObligatoria() { return obligatoria; }
    public void setObligatoria(boolean obligatoria) { this.obligatoria = obligatoria; }
}
