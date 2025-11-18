package logic;

import data.UsuarioDAO;
import entities.Usuario;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Controlador de Usuario para Misterio en la Mansión.
 * Especificación de tabla:
 *   usuario(id, nombre, email, password(sha256 hex), rol, fecha_registro, activo)
 *
 * Nota: entities.Usuario NO tiene password en claro -> la contraseña se pasa por parámetro.
 */
public class CtrlUsuario {

    private final UsuarioDAO dao;

    public CtrlUsuario() {
        this.dao = new UsuarioDAO();
    }

    /* ==========================
       Consultas / lecturas
       ========================== */

    public Usuario getUserById(int id) throws SQLException {
        return dao.findById(id);
    }

    public Usuario getUserByEmail(String email) throws SQLException {
        return dao.findByEmail(email);
    }

    /**
     * Valida login con email y contraseña en claro.
     * Devuelve el Usuario (sin exponer password) si es válido y está activo; si no, null.
     */
    public Usuario validate(String email, String passwordPlain) throws SQLException {
        boolean ok = dao.validarLogin(email, passwordPlain);
        if (!ok) return null;
        return dao.findByEmail(email);
    }

    /**
     * Overload por compatibilidad: recibe un Usuario (con email seteado) + password en claro.
     * Ignora cualquier otro campo del Usuario.
     */
    public Usuario validate(Usuario u, String passwordPlain) throws SQLException {
        if (u == null || u.getEmail() == null) return null;
        return validate(u.getEmail(), passwordPlain);
    }

    /** Lista de usuarios activos (por compatibilidad: LinkedList). */
    public LinkedList<Usuario> getAll() throws SQLException {
        return getAll(true);
    }

    /** Lista usuarios; si soloActivos=true filtra activo=1. */
    public LinkedList<Usuario> getAll(boolean soloActivos) throws SQLException {
        List<Usuario> list = dao.getAll(soloActivos);
        return new LinkedList<>(list);
    }

    /* ==========================
       Altas / modificaciones
       ========================== */

    /** Alta de usuario. Devuelve el ID generado. */
    public int addUsuario(String nombre, String email, String rol, String passwordPlain) throws SQLException {
        return dao.create(nombre, email, rol, passwordPlain);
    }

    /** Actualiza perfil (no toca contraseña). */
    public boolean modificarUsuario(int id, String nombre, String email, String rol, boolean activo) throws SQLException {
        return dao.updatePerfil(id, nombre, email, rol, activo);
    }

    /** Cambia la contraseña (re-hash SHA-256). */
    public boolean cambiarPassword(int id, String nuevaPassword) throws SQLException {
        return dao.updatePassword(id, nuevaPassword);
    }

    /** Baja lógica (activo=0). */
    public boolean eliminarUsuario(int id) throws SQLException {
        return dao.softDelete(id);
    }

    /** Reactiva un usuario desactivado (activo=1). */
    public boolean reactivarUsuario(int id) throws SQLException {
        return dao.reactivar(id);
    }
}
