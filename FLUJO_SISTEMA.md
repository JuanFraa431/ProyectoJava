# 🔄 Flujo de Ejecución del Sistema de Documentos

## 📖 Diagrama de Flujo Completo

```
┌─────────────────────────────────────────────────────────────┐
│  1. JUGADOR INICIA PARTIDA                                  │
│     URL: /jugador/partidas/juego?pid=123                    │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│  2. PartidaJuegoServlet.doGet()                             │
│     ┌─────────────────────────────────────────┐             │
│     │ • Verifica sesión de usuario            │             │
│     │ • Obtiene partidaId del parámetro       │             │
│     │ • Carga la partida desde PartidaDAO     │             │
│     │ • Carga la historia desde HistoriaDAO   │             │
│     │ ✨ NUEVO: Carga documentos con:         │             │
│     │   documentoDAO.findByHistoriaId()       │             │
│     └─────────────────────────────────────────┘             │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│  3. juego.jsp Renderiza la Página                           │
│     ┌─────────────────────────────────────────┐             │
│     │ <%                                       │             │
│     │   List<Documento> docs =                │             │
│     │     request.getAttribute("documentos")  │             │
│     │                                          │             │
│     │   for (Documento doc : docs) {          │             │
│     │     // Renderiza botón                  │             │
│     │   }                                      │             │
│     │ %>                                       │             │
│     │                                          │             │
│     │ JavaScript:                              │             │
│     │   const CODE_OK = '2580'; // dinámico   │             │
│     │   const PISTA = 'Código del celular';   │             │
│     └─────────────────────────────────────────┘             │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│  4. JUGADOR INTERACTÚA                                      │
│     ┌───────────────────────┬───────────────────────────┐   │
│     │ Click "Documentos"    │ Ingresa código            │   │
│     │   ↓                   │   ↓                       │   │
│     │ Abre panel lateral    │ Submit form               │   │
│     │   ↓                   │   ↓                       │   │
│     │ Click documento       │ JavaScript valida         │   │
│     │   ↓                   │   ↓                       │   │
│     │ Muestra modal HTML    │ AJAX → ChatServlet        │   │
│     └───────────────────────┴───────────────────────────┘   │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│  5. ChatServlet.doPost()                                    │
│     ┌─────────────────────────────────────────┐             │
│     │ • Recibe: action="validate_code"        │             │
│     │ • Recibe: code="2580"                   │             │
│     │ • Obtiene historiaId de sesión          │             │
│     │                                          │             │
│     │ ✨ NUEVO: Validación dinámica           │             │
│     │   Documento doc =                       │             │
│     │     documentoDAO.validarCodigo(         │             │
│     │       historiaId, code                  │             │
│     │     );                                   │             │
│     │                                          │             │
│     │ if (doc != null) {                      │             │
│     │   // Código correcto                    │             │
│     │   pistaId = encontrar(doc.pistaNombre)  │             │
│     │   progDAO.registrarPista(pistaId)       │             │
│     │   return {ok:true, persisted:true}      │             │
│     │ } else {                                │             │
│     │   // Código incorrecto                  │             │
│     │   return {ok:false}                     │             │
│     │ }                                       │             │
│     └─────────────────────────────────────────┘             │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│  6. RESPUESTA AL JUGADOR                                    │
│     ┌────────────────────┬──────────────────────────────┐   │
│     │ Código CORRECTO    │ Código INCORRECTO            │   │
│     │   ↓                │   ↓                          │   │
│     │ Bot: "¡Excelente!" │ Bot: "Ese código no es..."   │   │
│     │ Pista registrada   │ Volver a intentar            │   │
│     │ Partida gana       │                              │   │
│     │ Botón finalizar    │                              │   │
│     └────────────────────┴──────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

---

## 🎬 Ejemplo Concreto: Historia 2

### Paso 1: Carga Inicial
```java
// PartidaJuegoServlet.java
List<Documento> documentos = documentoDAO.findByHistoriaId(2);
// Resultado:
// - Diario de la víctima
// - Nota de recepción
// - Mensaje celular bloqueado
```

### Paso 2: Renderizado JSP
```jsp
<!-- juego.jsp -->
<% for (entities.Documento doc : documentos) { %>
  <button class="doc" data-doc="<%= doc.getClave() %>">
    <i class="<%= doc.getIcono() %>"></i>
    <%= doc.getNombre() %>
  </button>
<% } %>

<!-- Resultado HTML: -->
<button class="doc" data-doc="diario_victima">
  <i class="fa-regular fa-book"></i>
  Diario de la víctima
</button>
<button class="doc" data-doc="nota_recepcion">
  <i class="fa-regular fa-note-sticky"></i>
  Nota de recepción del hotel
</button>
<button class="doc" data-doc="mensaje_celular">
  <i class="fa-solid fa-mobile-screen-button"></i>
  Mensaje en el celular bloqueado
</button>
```

### Paso 3: Código Dinámico
```javascript
// JavaScript generado dinámicamente
const CODE_OK = '2580';  // ← Viene de la BD
const PISTA_NOMBRE = 'Código del celular';  // ← Viene de la BD

// Ya no es hardcoded "7391"
```

### Paso 4: Validación
```java
// ChatServlet.java
Documento doc = documentoDAO.validarCodigo(2, "2580");
// Query SQL:
// SELECT * FROM documento 
// WHERE historia_id = 2 AND codigo_correcto = '2580'

// Resultado:
// doc.id = 4
// doc.pistaNombre = "Código del celular"
// doc.historiaId = 2
```

---

## 🔄 Comparación ANTES vs DESPUÉS

### ❌ ANTES (Sistema Antiguo)

```java
// ChatServlet.java - HARDCODED
private static final String CODIGO_ESPERADO = "7391";
private static final String NOMBRE_PISTA = "Código de la computadora";

// PROBLEMA: Todas las historias usan el mismo código
if (CODIGO_ESPERADO.equals(code)) {
    // ✓ Historia 1 → funciona
    // ✗ Historia 2 → usa código incorrecto (7391 en vez de 2580)
    // ✗ Historia 3 → imposible agregar
}
```

```jsp
<!-- juego.jsp - HTML ESTÁTICO -->
<button class="doc" data-doc="nota">
  <i class="fa-regular fa-file-lines"></i>
  Nota manuscrita: "Cómo descifrar el código"
</button>
<button class="doc" data-doc="correo">
  <i class="fa-regular fa-envelope"></i>
  Correo sospechoso: "El código está oculto"
</button>

<!-- PROBLEMA: Siempre muestra los mismos documentos -->
```

### ✅ DESPUÉS (Sistema Nuevo)

```java
// ChatServlet.java - DINÁMICO
Documento doc = documentoDAO.validarCodigo(historiaId, code);

// BENEFICIO: Cada historia tiene su código
if (doc != null) {
    // ✓ Historia 1 → valida "7391"
    // ✓ Historia 2 → valida "2580"
    // ✓ Historia 3 → valida lo que esté en la BD
    String pistaNombre = doc.getPistaNombre();
}
```

```jsp
<!-- juego.jsp - RENDERIZADO DINÁMICO -->
<% for (entities.Documento doc : documentos) { %>
  <button class="doc" data-doc="<%= doc.getClave() %>">
    <i class="<%= doc.getIcono() %>"></i>
    <%= doc.getNombre() %>
  </button>
<% } %>

<!-- BENEFICIO: Cada historia muestra sus propios documentos -->
```

---

## 🗄️ Queries SQL Ejecutadas

### Durante la Carga de Partida
```sql
-- 1. Obtener documentos de la historia
SELECT id, historia_id, clave, nombre, icono, contenido, 
       codigo_correcto, pista_nombre 
FROM documento 
WHERE historia_id = 2 
ORDER BY id;

-- Resultado (Historia 2):
-- id=4: Diario de la víctima (código: 2580)
-- id=5: Nota de recepción (sin código)
-- id=6: Mensaje celular (sin código)
```

### Durante la Validación de Código
```sql
-- 2. Validar código ingresado
SELECT id, historia_id, clave, nombre, icono, contenido, 
       codigo_correcto, pista_nombre 
FROM documento 
WHERE historia_id = 2 AND codigo_correcto = '2580';

-- Si coincide:
-- → Retorna el documento con pista_nombre
-- → Registra la pista en progreso_pista
```

### Registrar Pista Encontrada
```sql
-- 3. Buscar ID de pista por nombre
SELECT id FROM pista 
WHERE historia_id = 2 AND nombre = 'Código del celular';

-- 4. Registrar progreso
INSERT INTO progreso_pista (partida_id, pista_id, fecha_encontrada)
VALUES (123, 16, NOW());
```

---

## 🎯 Casos de Uso

### Caso 1: Jugador Encuentra el Código Correcto
```
1. Jugador abre documento "Diario de la víctima"
   → Lee: "habitación 2580"
   
2. Jugador abre documento "Nota de recepción"
   → Lee: "cambió su PIN por el número de habitación"
   
3. Jugador deduce: código = 2580

4. Jugador en chat: "Descubrí algo importante"
   → Bot: "¿Cuál es el código?"
   → Jugador ingresa: 2580
   
5. JavaScript envía AJAX → ChatServlet
   POST /jugador/partidas/chat
   {action: "validate_code", code: "2580"}
   
6. ChatServlet consulta BD:
   documentoDAO.validarCodigo(2, "2580")
   
7. BD retorna documento:
   {codigo_correcto: "2580", pista_nombre: "Código del celular"}
   
8. Servlet registra pista en progreso_pista
   
9. Respuesta JSON:
   {ok: true, message: "¡Excelente! Registré la pista...", persisted: true}
   
10. Bot muestra: "¡Código correcto! Partida ganada!"
```

### Caso 2: Código Incorrecto
```
1. Jugador intenta: 1234

2. AJAX → ChatServlet
   
3. ChatServlet:
   documentoDAO.validarCodigo(2, "1234")
   
4. BD retorna: null (no existe)
   
5. Respuesta JSON:
   {ok: false, message: "Ese código no es..."}
   
6. Bot: "Mmm... ese código no es. Revisá las pistas."
```

### Caso 3: Aislamiento Entre Historias
```
Jugador en Historia 2 intenta código de Historia 1:

1. Jugador ingresa: 7391

2. Query SQL:
   WHERE historia_id = 2 AND codigo_correcto = '7391'
   
3. Resultado: ninguno (7391 pertenece a historia_id = 1)

4. Validación falla → Código incorrecto

✅ Las historias están aisladas correctamente
```

---

## 📊 Estructura de Datos en Memoria

### Objeto Documento (Historia 2, Diario)
```java
Documento {
    id: 4,
    historiaId: 2,
    clave: "diario_victima",
    nombre: "Diario de la víctima",
    icono: "fa-regular fa-book",
    contenido: "<h3>Diario...</h3><p>...2580...</p>",
    codigoCorrecto: "2580",
    pistaNombre: "Código del celular"
}
```

### Lista de Documentos en Request
```java
List<Documento> documentos = [
    Documento{id:4, clave:"diario_victima", codigo:"2580", ...},
    Documento{id:5, clave:"nota_recepcion", codigo:null, ...},
    Documento{id:6, clave:"mensaje_celular", codigo:null, ...}
]

// Se pasa al JSP:
request.setAttribute("documentos", documentos);
```

### Variables JavaScript Generadas
```javascript
// En juego.jsp después del rendering:
const documentos = [
    {
        clave: 'diario_victima',
        nombre: 'Diario de la víctima',
        icono: 'fa-regular fa-book',
        contenido: '<h3>Diario...</h3>...'
    },
    {
        clave: 'nota_recepcion',
        nombre: 'Nota de recepción del hotel',
        icono: 'fa-regular fa-note-sticky',
        contenido: '<h3>Nota...</h3>...'
    },
    {
        clave: 'mensaje_celular',
        nombre: 'Mensaje en el celular bloqueado',
        icono: 'fa-solid fa-mobile-screen-button',
        contenido: '<h3>Mensaje...</h3>...'
    }
];

const CODE_OK = '2580';
const PISTA_NOMBRE = 'Código del celular';
```

---

## 🔍 Debugging Tips

### Verificar qué documentos se cargan
```java
// En PartidaJuegoServlet.java (línea ~63)
List<Documento> documentos = documentoDAO.findByHistoriaId(historia.getId());
System.out.println("=== DOCUMENTOS CARGADOS ===");
for (Documento d : documentos) {
    System.out.println(d.getId() + " - " + d.getNombre() + 
                       " (código: " + d.getCodigoCorrecto() + ")");
}
```

### Verificar validación de código
```java
// En ChatServlet.java (línea ~34)
Documento doc = documentoDAO.validarCodigo(historiaId, code);
System.out.println("=== VALIDACIÓN ===");
System.out.println("Historia: " + historiaId);
System.out.println("Código ingresado: " + code);
System.out.println("Resultado: " + (doc != null ? "✓ VÁLIDO" : "✗ INVÁLIDO"));
if (doc != null) {
    System.out.println("Pista a registrar: " + doc.getPistaNombre());
}
```

---

**Sistema completamente dinámico y listo para producción! 🚀**
