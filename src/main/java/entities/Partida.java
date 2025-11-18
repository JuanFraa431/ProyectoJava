package entities;

public class Partida {
    private int id;
    private int usuarioId;
    private int historiaId;
    private String fechaInicio;
    private String fechaFin;
    private String estado; // EN_PROGRESO, FINALIZADA
    private int pistasEncontradas;
    private int ubicacionesExploradas;
    private int puntuacion;
    private String solucionPropuesta;
    private boolean casoResuelto;
    private int intentosRestantes;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public int getHistoriaId() { return historiaId; }
    public void setHistoriaId(int historiaId) { this.historiaId = historiaId; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getPistasEncontradas() { return pistasEncontradas; }
    public void setPistasEncontradas(int pistasEncontradas) { this.pistasEncontradas = pistasEncontradas; }

    public int getUbicacionesExploradas() { return ubicacionesExploradas; }
    public void setUbicacionesExploradas(int ubicacionesExploradas) { this.ubicacionesExploradas = ubicacionesExploradas; }

    public int getPuntuacion() { return puntuacion; }
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }

    public String getSolucionPropuesta() { return solucionPropuesta; }
    public void setSolucionPropuesta(String solucionPropuesta) { this.solucionPropuesta = solucionPropuesta; }

    public boolean isCasoResuelto() { return casoResuelto; }
    public void setCasoResuelto(boolean casoResuelto) { this.casoResuelto = casoResuelto; }

    public int getIntentosRestantes() { return intentosRestantes; }
    public void setIntentosRestantes(int intentosRestantes) { this.intentosRestantes = intentosRestantes; }
}
