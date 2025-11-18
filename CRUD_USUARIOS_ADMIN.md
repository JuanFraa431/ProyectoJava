# 👥 CRUD de Usuarios - Guía Completa para Administradores

## 📋 Tabla de Contenidos
1. [Visión General](#visión-general)
2. [Acceso al Sistema](#acceso-al-sistema)
3. [Listar Usuarios](#listar-usuarios)
4. [Crear Usuario](#crear-usuario)
5. [Editar Usuario](#editar-usuario)
6. [Desactivar Usuario](#desactivar-usuario)
7. [Reactivar Usuario](#reactivar-usuario)
8. [Buscar Usuarios](#buscar-usuarios)

---

## 🎯 Visión General

El sistema CRUD de usuarios permite a los **administradores** gestionar completamente las cuentas de usuarios del juego "Misterio en la Mansión".

### Operaciones Disponibles

| Operación | Descripción | URL |
|-----------|-------------|-----|
| **Listar** | Ver todos los usuarios (activos e inactivos) | `/admin/usuarios` |
| **Crear** | Dar de alta un nuevo usuario | `/admin/usuarios/form` |
| **Editar** | Modificar datos de un usuario existente | `/admin/usuarios/form?id=X` |
| **Desactivar** | Baja lógica (soft delete) | `/admin/usuarios/delete` |
| **Reactivar** | Reactivar usuario desactivado | `/admin/usuarios/reactivar` |
| **Buscar** | Buscar por nombre o email | `/admin/usuarios?q=texto` |

### Arquitectura del Sistema

```
┌─────────────────────────────────────────────┐
│         CAPA DE PRESENTACIÓN                │
│  ✓ list.jsp - Listado de usuarios          │
│  ✓ form.jsp - Formulario crear/editar      │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│         CAPA DE LÓGICA (Servlets)           │
│  ✓ AdminUsuariosServlet (Listar)            │
│  ✓ AdminUsuarioFormServlet (Formulario)     │
│  ✓ AdminUsuarioSaveServlet (Crear/Editar)   │
│  ✓ AdminUsuarioReactivateServlet (Reactivar)│
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│         CAPA DE DATOS                       │
│  ✓ CtrlUsuario - Controlador principal      │
│  ✓ UsuarioDAO - Acceso a base de datos      │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│         BASE DE DATOS                       │
│  ✓ Tabla: usuario                           │
│    - id, nombre, email, password_hash       │
│    - rol, activo, en_partida               │
└─────────────────────────────────────────────┘
```

---

## 🔐 Acceso al Sistema

### Paso 1: Iniciar Sesión como Admin

1. Abrí el navegador en: `http://localhost:8080/MisterioEnLaMansion/login`

2. Ingresá credenciales de administrador:
   ```
   Email: admin@ejemplo.com
   Password: (tu contraseña de admin)
   ```

3. El sistema verificará:
   - ✅ Email y password correctos
   - ✅ Usuario activo
   - ✅ Rol = "ADMIN"

4. Serás redirigido al **Dashboard de Administrador**

### Paso 2: Navegar a Usuarios

Desde el Dashboard, hacé click en:
```
📊 Dashboard → 👥 Usuarios
```

O accedé directamente:
```
http://localhost:8080/MisterioEnLaMansion/admin/usuarios
```

---

## 📋 Listar Usuarios

### Flujo de Ejecución

```mermaid
Usuario → Click "Usuarios" → AdminUsuariosServlet.doGet()
         ↓
    UsuarioDAO.getAll(false)
         ↓
    Query SQL: SELECT * FROM usuario
         ↓
    Lista de usuarios cargada
         ↓
    Renderiza list.jsp
         ↓
    Muestra tabla con usuarios
```

### Paso a Paso

1. **El admin accede a `/admin/usuarios`**

2. **El servidor ejecuta:**
   ```java
   // AdminUsuariosServlet.java
   List<Usuario> usuarios = dao.getAll(false);
   // false = incluye usuarios inactivos también
   ```

3. **Query SQL ejecutada:**
   ```sql
   SELECT id, nombre, email, rol, activo, en_partida, fecha_registro
   FROM usuario
   ORDER BY id DESC
   ```

4. **La vista muestra una tabla con:**

| ID | Nombre | Email | Rol | Estado | En Partida | Acciones |
|----|--------|-------|-----|--------|-----------|----------|
| 6 | Juan Pérez | juan@mail.com | JUGADOR | ✅ Activo | No | [Editar] [Desactivar] |
| 4 | María López | maria@mail.com | ADMIN | ✅ Activo | No | [Editar] [Desactivar] |
| 1 | Pedro Gómez | pedro@mail.com | JUGADOR | ❌ Inactivo | No | [Editar] [Reactivar] |

### Elementos de la Interfaz

```
┌────────────────────────────────────────────────────┐
│  Usuarios                                          │
│  Administrá altas, ediciones y bajas lógicas       │
│                                                    │
│  🔍 [Buscar por nombre o email...]  [+ Nuevo usuario] [📊 Dashboard]
│                                                    │
│  ┌──────────────────────────────────────────────┐ │
│  │ ID │ Nombre │ Email │ Rol │ Estado │ Acciones │ │
│  ├────┼────────┼───────┼─────┼────────┼──────────┤ │
│  │ 6  │ Juan   │ ...   │ JUG │ ✅     │ [✏️] [🗑️]│ │
│  │ 4  │ María  │ ...   │ ADM │ ✅     │ [✏️] [🗑️]│ │
│  │ 1  │ Pedro  │ ...   │ JUG │ ❌     │ [✏️] [♻️]│ │
│  └──────────────────────────────────────────────┘ │
└────────────────────────────────────────────────────┘
```

---

## ➕ Crear Usuario

### Flujo Completo

```
Admin → Click "Nuevo usuario" → GET /admin/usuarios/form
       ↓
   AdminUsuarioFormServlet carga formulario vacío
       ↓
   Admin completa el formulario
       ↓
   Click "Guardar" → POST /admin/usuarios/save
       ↓
   AdminUsuarioSaveServlet valida datos
       ↓
   CtrlUsuario.addUsuario(nombre, email, rol, password)
       ↓
   1. Hashea la contraseña (SHA-256)
   2. INSERT INTO usuario
       ↓
   Redirige a /admin/usuarios con mensaje de éxito
```

### Paso a Paso

#### 1. Acceder al Formulario

Hacé click en **"+ Nuevo usuario"** o accedé a:
```
http://localhost:8080/MisterioEnLaMansion/admin/usuarios/form
```

#### 2. Completar el Formulario

El formulario muestra:

```
┌─────────────────────────────────────────┐
│  Nuevo Usuario                          │
│                                         │
│  Nombre: [________________________]     │
│                                         │
│  Email:  [________________________]     │
│                                         │
│  Rol:    ( ) ADMIN  (•) JUGADOR         │
│                                         │
│  Contraseña: [____________________]     │
│                                         │
│  [✓] Usuario activo                     │
│                                         │
│  [Guardar]  [Cancelar]                  │
└─────────────────────────────────────────┘
```

**Campos obligatorios:**
- ✅ **Nombre**: Nombre completo del usuario
- ✅ **Email**: Debe ser único en la base de datos
- ✅ **Rol**: ADMIN o JUGADOR
- ✅ **Contraseña**: Mínimo recomendado 6 caracteres

#### 3. Enviar el Formulario

Al hacer click en **"Guardar"**, se ejecuta:

```java
// AdminUsuarioSaveServlet.java
if (idStr == null || idStr.isBlank()) {
    // Alta de nuevo usuario
    int newId = ctrl.addUsuario(nombre, email, rol, pass);
    req.getSession().setAttribute("flash_ok", "Usuario creado (ID " + newId + ").");
}
```

#### 4. Validaciones Realizadas

```java
// CtrlUsuario.java
public int addUsuario(String nombre, String email, String rol, String password) {
    // 1. Validar que todos los campos estén completos
    if (nombre == null || email == null || rol == null || password == null) {
        throw new IllegalArgumentException("Campos obligatorios faltantes");
    }
    
    // 2. Verificar que el email no exista
    Usuario existe = dao.findByEmail(email);
    if (existe != null) {
        throw new IllegalArgumentException("El email ya está registrado");
    }
    
    // 3. Hashear la contraseña
    String hash = HashUtil.hashPassword(password);
    
    // 4. Insertar en la base de datos
    return dao.insert(nombre, email, rol, hash);
}
```

#### 5. Query SQL Ejecutada

```sql
INSERT INTO usuario (nombre, email, rol, password_hash, activo, en_partida)
VALUES ('Juan Pérez', 'juan@mail.com', 'JUGADOR', 'a3f8d7e...', 1, 0)
```

#### 6. Resultado

Si todo es exitoso:
- ✅ Usuario creado en la base de datos
- ✅ Mensaje verde: "Usuario creado (ID 7)."
- ✅ Redirige a `/admin/usuarios`
- ✅ El nuevo usuario aparece en la lista

Si hay error:
- ❌ Mensaje rojo: "Completá todos los campos para el alta." o "Email duplicado"
- ❌ Permanece en el formulario

---

## ✏️ Editar Usuario

### Flujo Completo

```
Admin → Click "Editar" en la fila del usuario
       ↓
   GET /admin/usuarios/form?id=6
       ↓
   AdminUsuarioFormServlet carga datos del usuario
       ↓
   CtrlUsuario.getUserById(6)
       ↓
   Formulario precargado con datos actuales
       ↓
   Admin modifica campos
       ↓
   Click "Guardar" → POST /admin/usuarios/save (con id=6)
       ↓
   AdminUsuarioSaveServlet detecta que es una edición
       ↓
   CtrlUsuario.modificarUsuario(id, nombre, email, rol, activo)
       ↓
   UPDATE usuario WHERE id = 6
       ↓
   Redirige con mensaje de éxito
```

### Paso a Paso

#### 1. Acceder al Formulario de Edición

En la lista de usuarios, hacé click en el ícono **"✏️ Editar"** del usuario deseado.

URL generada:
```
http://localhost:8080/MisterioEnLaMansion/admin/usuarios/form?id=6
```

#### 2. Cargar Datos del Usuario

```java
// AdminUsuarioFormServlet.java
String idStr = req.getParameter("id");
if (idStr != null && !idStr.isBlank()) {
    int id = Integer.parseInt(idStr);
    Usuario u = new CtrlUsuario().getUserById(id);
    req.setAttribute("usuario", u);
}
```

Query SQL:
```sql
SELECT id, nombre, email, rol, activo, en_partida, fecha_registro
FROM usuario
WHERE id = 6
```

#### 3. Formulario Precargado

```
┌─────────────────────────────────────────┐
│  Editar Usuario #6                      │
│                                         │
│  Nombre: [Juan Pérez________________]   │
│                                         │
│  Email:  [juan@mail.com_____________]   │
│                                         │
│  Rol:    ( ) ADMIN  (•) JUGADOR         │
│                                         │
│  Contraseña: [____________________]     │
│  (Dejá en blanco para no cambiarla)     │
│                                         │
│  [✓] Usuario activo                     │
│                                         │
│  [Guardar]  [Cancelar]                  │
└─────────────────────────────────────────┘
```

#### 4. Modificar Campos

El admin puede cambiar:
- ✅ **Nombre**
- ✅ **Email** (validará que no esté duplicado)
- ✅ **Rol** (ADMIN ↔ JUGADOR)
- ✅ **Estado** (Activo/Inactivo)
- ⚠️ **Contraseña** (opcional - solo si se completa el campo)

#### 5. Guardar Cambios

Al hacer click en **"Guardar"**:

```java
// AdminUsuarioSaveServlet.java
else {
    // Update (porque idStr tiene valor)
    int id = Integer.parseInt(idStr);
    boolean ok = ctrl.modificarUsuario(id, nombre, email, rol, activo);
    
    // Cambio de contraseña opcional
    if (pass != null && !pass.isBlank()) {
        ctrl.cambiarPassword(id, pass);
    }
    
    req.getSession().setAttribute("flash_ok", "Usuario actualizado.");
}
```

#### 6. Queries SQL Ejecutadas

**Actualizar datos básicos:**
```sql
UPDATE usuario 
SET nombre = 'Juan Carlos Pérez',
    email = 'juanc@mail.com',
    rol = 'ADMIN',
    activo = 1
WHERE id = 6
```

**Si se cambió la contraseña:**
```sql
UPDATE usuario 
SET password_hash = 'nuevo_hash_sha256...'
WHERE id = 6
```

#### 7. Resultado

- ✅ Mensaje verde: "Usuario actualizado."
- ✅ Cambios reflejados en la lista
- ✅ Si se cambió el email, no puede duplicarse con otro usuario

---

## 🗑️ Desactivar Usuario (Baja Lógica)

### ¿Qué es una Baja Lógica?

- ❌ **NO** elimina el registro de la base de datos
- ✅ Marca el campo `activo = 0`
- ✅ El usuario **NO** podrá iniciar sesión
- ✅ Se conserva todo su historial (partidas, pistas, etc.)
- ✅ Puede ser **reactivado** posteriormente

### Flujo Completo

```
Admin → Click "🗑️ Desactivar" en la fila del usuario
       ↓
   Confirmación JavaScript: "¿Desactivar a Juan Pérez?"
       ↓
   Si acepta → POST /admin/usuarios/delete?id=6
       ↓
   AdminUsuarioSaveServlet procesa la baja
       ↓
   CtrlUsuario.eliminarUsuario(6)
       ↓
   UPDATE usuario SET activo = 0 WHERE id = 6
       ↓
   Mensaje: "Usuario desactivado."
       ↓
   El usuario aparece con estado ❌ Inactivo
```

### Paso a Paso

#### 1. Hacer Click en "Desactivar"

En la lista, localizar el usuario y hacer click en **"🗑️ Desactivar"**

#### 2. Confirmar la Acción

Aparece un cuadro de confirmación JavaScript:
```
┌───────────────────────────────────┐
│  ¿Desactivar a Juan Pérez?        │
│                                   │
│  [Cancelar]  [Aceptar]            │
└───────────────────────────────────┘
```

#### 3. Procesar la Baja

```java
// AdminUsuarioSaveServlet.java
if (path.endsWith("/delete")) {
    String idStr = req.getParameter("id");
    int id = Integer.parseInt(idStr);
    boolean ok = ctrl.eliminarUsuario(id); // soft delete
    
    req.getSession().setAttribute(
        ok ? "flash_ok" : "flash_error",
        ok ? "Usuario desactivado." : "No se pudo desactivar el usuario."
    );
}
```

```java
// CtrlUsuario.java
public boolean eliminarUsuario(int userId) throws SQLException {
    return dao.setActivo(userId, false);
}
```

#### 4. Query SQL Ejecutada

```sql
UPDATE usuario 
SET activo = 0 
WHERE id = 6
```

#### 5. Resultado

En la lista, el usuario ahora aparece:

| ID | Nombre | Email | Rol | Estado | Acciones |
|----|--------|-------|-----|--------|----------|
| 6 | Juan Pérez | juan@mail.com | JUGADOR | ❌ **Inactivo** | [✏️ Editar] [♻️ **Reactivar**] |

**Efectos:**
- ❌ No puede iniciar sesión
- ❌ No aparece en listados de jugadores activos
- ✅ Su historial se conserva
- ✅ Puede ser reactivado

---

## ♻️ Reactivar Usuario

### Flujo Completo

```
Admin → Click "♻️ Reactivar" en usuario inactivo
       ↓
   POST /admin/usuarios/reactivar?id=6
       ↓
   AdminUsuarioReactivateServlet procesa
       ↓
   UsuarioDAO.reactivar(6)
       ↓
   UPDATE usuario SET activo = 1 WHERE id = 6
       ↓
   Mensaje: "Usuario reactivado."
       ↓
   El usuario vuelve a estado ✅ Activo
```

### Paso a Paso

#### 1. Localizar Usuario Inactivo

En la lista, identificar usuarios con estado **❌ Inactivo**

#### 2. Click en "Reactivar"

Hacer click en el botón **"♻️ Reactivar"**

#### 3. Procesar Reactivación

```java
// AdminUsuarioReactivateServlet.java
int id = Integer.parseInt(idStr);
boolean ok = dao.reactivar(id);

if (ok) {
    req.getSession().setAttribute("flash_ok", "Usuario reactivado.");
}
```

```java
// UsuarioDAO.java
public boolean reactivar(int id) throws SQLException {
    String sql = "UPDATE usuario SET activo = 1 WHERE id = ?";
    try (Connection con = DbConn.getInstancia().getConn();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, id);
        return ps.executeUpdate() > 0;
    }
}
```

#### 4. Query SQL Ejecutada

```sql
UPDATE usuario 
SET activo = 1 
WHERE id = 6
```

#### 5. Resultado

- ✅ Estado cambia a **Activo**
- ✅ El usuario puede iniciar sesión nuevamente
- ✅ Conserva todo su historial anterior
- ✅ El botón cambia a **"🗑️ Desactivar"**

---

## 🔍 Buscar Usuarios

### Funcionalidad

Permite buscar usuarios por:
- 📝 Nombre (búsqueda parcial)
- 📧 Email (búsqueda parcial)

### Flujo Completo

```
Admin → Escribe "juan" en el buscador
       ↓
   Submit automático o Enter
       ↓
   GET /admin/usuarios?q=juan
       ↓
   AdminUsuariosServlet detecta parámetro "q"
       ↓
   UsuarioDAO.search("juan", true)
       ↓
   Query SQL con LIKE
       ↓
   Lista filtrada de usuarios
```

### Paso a Paso

#### 1. Ingresar Texto en el Buscador

```
┌────────────────────────────────────────┐
│  🔍 [juan___________________]  [Buscar]│
└────────────────────────────────────────┘
```

#### 2. Procesar Búsqueda

```java
// AdminUsuariosServlet.java
String q = req.getParameter("q");
if (q != null && !q.trim().isEmpty()) {
    req.setAttribute("q", q.trim());
    usuarios = dao.search(q.trim(), true /* incluir inactivos */);
}
```

#### 3. Query SQL Ejecutada

```sql
SELECT id, nombre, email, rol, activo, en_partida, fecha_registro
FROM usuario
WHERE (nombre LIKE '%juan%' OR email LIKE '%juan%')
ORDER BY id DESC
```

#### 4. Resultado

Solo se muestran usuarios que coincidan con "juan":

| ID | Nombre | Email | Rol | Estado | Acciones |
|----|--------|-------|-----|--------|----------|
| 6 | **Juan** Pérez | **juan**@mail.com | JUGADOR | ✅ Activo | [✏️] [🗑️] |
| 3 | **Juan**a López | **juan**a@mail.com | JUGADOR | ✅ Activo | [✏️] [🗑️] |

#### 5. Limpiar Búsqueda

Para volver a ver todos los usuarios:
- Borrar el texto del buscador
- Presionar Enter o click en el ícono de lupa
- O navegar directamente a `/admin/usuarios`

---

## 📊 Casos de Uso Completos

### Caso 1: Alta de Nuevo Jugador

**Objetivo:** Crear cuenta para un nuevo jugador del juego

```
1. Admin accede a /admin/usuarios
2. Click en "+ Nuevo usuario"
3. Completa formulario:
   - Nombre: "Carlos Ramírez"
   - Email: "carlos@mail.com"
   - Rol: JUGADOR
   - Contraseña: "carlos123"
   - Activo: ✓
4. Click "Guardar"
5. Sistema valida:
   ✓ Todos los campos completos
   ✓ Email no duplicado
6. Hashea contraseña: SHA-256("carlos123")
7. INSERT en base de datos
8. Mensaje: "Usuario creado (ID 8)."
9. El jugador puede iniciar sesión en /login
```

### Caso 2: Cambiar Usuario de Jugador a Admin

**Objetivo:** Promover un jugador a administrador

```
1. Admin busca al usuario: "maria"
2. Click en "✏️ Editar" en María López
3. Cambia:
   - Rol: JUGADOR → ADMIN
4. Click "Guardar"
5. UPDATE rol = 'ADMIN' WHERE id = 4
6. María López ahora tiene acceso al panel de admin
7. Puede gestionar historias, usuarios y ver dashboard
```

### Caso 3: Resetear Contraseña de Usuario

**Objetivo:** Usuario olvidó su contraseña

```
1. Admin busca al usuario por email
2. Click "✏️ Editar"
3. En campo "Contraseña" ingresa: "nueva123"
4. (Deja los demás campos sin cambiar)
5. Click "Guardar"
6. Sistema hashea "nueva123"
7. UPDATE password_hash = '...' WHERE id = X
8. Usuario puede iniciar sesión con "nueva123"
```

### Caso 4: Desactivar Usuario Problemático

**Objetivo:** Usuario reportado por conducta inapropiada

```
1. Admin localiza al usuario en la lista
2. Click "🗑️ Desactivar"
3. Confirma la acción
4. UPDATE activo = 0
5. Usuario bloqueado:
   ❌ No puede iniciar sesión
   ❌ Partidas en curso se finalizan
6. Historial conservado para auditoría
7. Puede ser reactivado si se resuelve el problema
```

### Caso 5: Auditoría de Usuarios Inactivos

**Objetivo:** Revisar usuarios que no se usan

```
1. Admin accede a /admin/usuarios
2. Observa columna "Estado"
3. Identifica usuarios ❌ Inactivos
4. Opciones:
   a) Reactivar si es necesario
   b) Mantener inactivo (conserva historial)
   c) Eliminar físicamente (fuera del CRUD, requiere SQL)
5. Decisión basada en:
   - Fecha de último acceso
   - Partidas jugadas
   - Motivo de desactivación
```

---

## 🔒 Seguridad Implementada

### 1. Autenticación

```java
// En cada servlet
HttpSession s = req.getSession(false);
String rol = (s != null) ? (String) s.getAttribute("rol") : null;
if (rol == null || !rol.equalsIgnoreCase("ADMIN")) {
    resp.sendRedirect(req.getContextPath() + "/jugador/home");
    return;
}
```

- ✅ Solo usuarios con `rol = "ADMIN"` pueden acceder
- ✅ Si no hay sesión → redirige a login
- ✅ Si no es admin → redirige a home de jugador

### 2. Hash de Contraseñas

```java
// HashUtil.java
public static String hashPassword(String password) {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
    return bytesToHex(hash);
}
```

- ✅ Contraseñas **NUNCA** se guardan en texto plano
- ✅ Se usa SHA-256 para hashear
- ✅ Imposible recuperar contraseña original

### 3. Validación de Emails

```java
// CtrlUsuario.java
Usuario existe = dao.findByEmail(email);
if (existe != null && existe.getId() != userId) {
    throw new IllegalArgumentException("El email ya está registrado");
}
```

- ✅ No permite emails duplicados
- ✅ Valida en alta y en edición
- ✅ Permite al mismo usuario mantener su email

### 4. SQL Injection Prevention

```java
// UsuarioDAO.java
String sql = "UPDATE usuario SET activo = ? WHERE id = ?";
PreparedStatement ps = con.prepareStatement(sql);
ps.setInt(1, activo ? 1 : 0);
ps.setInt(2, userId);
```

- ✅ Uso de **PreparedStatement**
- ✅ Parámetros sanitizados automáticamente
- ✅ No se concatenan strings en queries

---

## 📈 Diagrama de Estados del Usuario

```
  ┌─────────────┐
  │   CREADO    │
  │  (Alta)     │
  └──────┬──────┘
         │
         ▼
  ┌─────────────┐       ┌──────────────┐
  │   ACTIVO    │◄──────│ REACTIVADO   │
  │ activo = 1  │       │ (Reactivar)  │
  └──────┬──────┘       └──────▲───────┘
         │                     │
         │ Desactivar          │
         ▼                     │
  ┌─────────────┐              │
  │  INACTIVO   │──────────────┘
  │ activo = 0  │
  └─────────────┘
         │
         │ (Opcional, fuera del CRUD)
         ▼
  ┌─────────────┐
  │  ELIMINADO  │
  │  (físico)   │
  └─────────────┘
```

---

## 🎯 Buenas Prácticas

### Para Administradores

1. **No eliminar físicamente usuarios**
   - Usar siempre baja lógica (desactivar)
   - Conserva historial para auditorías

2. **Contraseñas seguras**
   - Mínimo 8 caracteres
   - Combinación de letras y números
   - No usar contraseñas obvias

3. **Verificar antes de desactivar**
   - Revisar si el usuario está en partida
   - Verificar motivo de desactivación

4. **Auditoría regular**
   - Revisar usuarios inactivos mensualmente
   - Verificar cambios de rol

### Para Desarrolladores

1. **Nunca guardar contraseñas en texto plano**
2. **Siempre usar PreparedStatement**
3. **Validar en backend y frontend**
4. **Loggear cambios importantes**
5. **Mantener historial de modificaciones**

---

## 🐛 Resolución de Problemas

### Error: "Email duplicado"

**Causa:** Intentando crear/editar usuario con email que ya existe

**Solución:**
```sql
-- Verificar emails existentes
SELECT email FROM usuario WHERE email = 'juan@mail.com';
```

### Error: "Usuario no encontrado"

**Causa:** ID no existe en la base de datos

**Solución:**
```sql
-- Verificar que el usuario existe
SELECT * FROM usuario WHERE id = 6;
```

### Usuario no puede iniciar sesión después de crear

**Causa posible:** Usuario creado como inactivo

**Solución:**
```sql
-- Verificar estado
SELECT id, nombre, activo FROM usuario WHERE email = 'usuario@mail.com';

-- Activar si está inactivo
UPDATE usuario SET activo = 1 WHERE id = X;
```

---

## 📚 Resumen de Endpoints

| Método | URL | Parámetros | Función |
|--------|-----|------------|---------|
| GET | `/admin/usuarios` | `q` (opcional) | Listar/Buscar usuarios |
| GET | `/admin/usuarios/form` | `id` (opcional) | Formulario crear/editar |
| POST | `/admin/usuarios/save` | `id`, `nombre`, `email`, `rol`, `password`, `activo` | Crear/Editar usuario |
| POST | `/admin/usuarios/delete` | `id` | Desactivar usuario |
| POST | `/admin/usuarios/reactivar` | `id` | Reactivar usuario |

---

## 🎓 Conclusión

El CRUD de usuarios permite una gestión completa y segura de las cuentas del sistema. Con operaciones de alta, baja lógica, modificación y búsqueda, el administrador tiene control total sobre los usuarios manteniendo la integridad de los datos históricos.

**Características clave:**
- ✅ Baja lógica (soft delete)
- ✅ Hash de contraseñas (SHA-256)
- ✅ Validación de emails duplicados
- ✅ Búsqueda flexible
- ✅ Interfaz intuitiva
- ✅ Seguridad por roles

---

**Documentación generada para: Misterio en la Mansión v1.0**  
**Fecha: 18 de noviembre de 2025**
