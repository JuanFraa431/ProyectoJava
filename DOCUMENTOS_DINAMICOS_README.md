# 📝 Sistema de Documentos Dinámicos - Misterio en la Mansión

## 🎯 Descripción de la Funcionalidad

Este sistema permite que cada historia del juego tenga sus propios documentos dinámicos con códigos específicos, haciendo el juego completamente escalable y fácil de mantener.

### ✨ Características

- **Documentos por Historia**: Cada historia tiene sus propios documentos en la base de datos
- **Códigos Dinámicos**: El código correcto se lee automáticamente de la BD
- **Contenido HTML**: Los documentos pueden contener HTML rico para mejor presentación
- **Fácil Mantenimiento**: Agregar nuevas historias no requiere modificar código Java

---

## 📦 Archivos Creados/Modificados

### Nuevos Archivos

1. **`entities/Documento.java`** - Entidad que representa un documento
2. **`data/DocumentoDAO.java`** - Acceso a datos para documentos
3. **`sql/create_documentos.sql`** - Script SQL para crear tabla e insertar datos

### Archivos Modificados

1. **`logic/jugador/PartidaJuegoServlet.java`** - Carga documentos de la historia
2. **`logic/jugador/ChatServlet.java`** - Valida códigos dinámicamente
3. **`webapp/WEB-INF/views/jugador/partida/juego.jsp`** - Renderiza documentos dinámicamente

---

## 🚀 Instalación

### Paso 1: Ejecutar el Script SQL

Ejecutá el script SQL para crear la tabla y cargar los datos:

```sql
-- Desde tu cliente MySQL/MariaDB
source e:\eclipse workspace\MisterioEnLaMansion\sql\create_documentos.sql
```

O copiá y pegá el contenido del archivo en tu gestor de base de datos.

### Paso 2: Verificar la Instalación

Ejecutá estas consultas para verificar:

```sql
-- Ver documentos de Historia 1
SELECT * FROM documento WHERE historia_id = 1;

-- Ver documentos de Historia 2
SELECT * FROM documento WHERE historia_id = 2;
```

### Paso 3: Compilar el Proyecto

Compilá el proyecto en Eclipse para que reconozca las nuevas clases.

---

## 📊 Estructura de la Tabla `documento`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | INT | ID autoincremental |
| `historia_id` | INT | ID de la historia (FK) |
| `clave` | VARCHAR(50) | Identificador único del documento |
| `nombre` | VARCHAR(200) | Nombre visible del documento |
| `icono` | VARCHAR(100) | Clase de ícono Font Awesome |
| `contenido` | TEXT | Contenido HTML del documento |
| `codigo_correcto` | VARCHAR(20) | Código que debe ingresar el jugador (puede ser NULL) |
| `pista_nombre` | VARCHAR(100) | Nombre de la pista a registrar cuando se valida el código |

---

## 🎮 Datos Actuales

### Historia 1: Misterio en la Mansión Watson
- **Código correcto**: `7391`
- **Documentos**:
  1. Nota manuscrita con pista sobre cómo descifrar el código
  2. Correo sospechoso que menciona "Siete Tres Nueve Uno"
- **Pista registrada**: "Código de la computadora"

### Historia 2: El Enigma del Hotel Riverside
- **Código correcto**: `2580`
- **Documentos**:
  1. Diario de la víctima (menciona habitación 2580)
  2. Nota de recepción del hotel
  3. Mensaje en el celular bloqueado
- **Pista registrada**: "Código del celular"

---

## 📝 Cómo Agregar una Nueva Historia

### 1. Insertar Documentos en la Base de Datos

```sql
-- Historia 3: ejemplo
INSERT INTO documento (historia_id, clave, nombre, icono, contenido, codigo_correcto, pista_nombre) 
VALUES
-- Documento con el código correcto
(3, 'clave_principal', 'Documento con la clave', 'fa-regular fa-key',
 '<h3>La Clave Secreta</h3><p>El código es <strong>1234</strong></p>',
 '1234', 'Clave principal encontrada'),

-- Documentos adicionales sin código
(3, 'pista_1', 'Primera pista', 'fa-regular fa-lightbulb',
 '<h3>Primera Pista</h3><p>Buscá en el estudio...</p>',
 NULL, NULL),
 
(3, 'pista_2', 'Segunda pista', 'fa-regular fa-magnifying-glass',
 '<h3>Segunda Pista</h3><p>Revisá el escritorio...</p>',
 NULL, NULL);
```

### 2. Verificar el Nombre de la Pista

Asegurate de que `pista_nombre` coincida EXACTAMENTE con un registro en la tabla `pista`:

```sql
-- Ver pistas disponibles para la historia
SELECT id, nombre FROM pista WHERE historia_id = 3;
```

### 3. Probá el Juego

- Iniciá una partida con la nueva historia
- Abrí el panel de documentos
- Ingresá el código correcto
- Verificá que se registre la pista

---

## 🎨 Íconos Disponibles (Font Awesome 6.5)

Ejemplos de íconos que podés usar en el campo `icono`:

```
fa-regular fa-file-lines          # Archivo de texto
fa-regular fa-envelope            # Correo/sobre
fa-regular fa-note-sticky         # Post-it
fa-regular fa-book                # Libro/diario
fa-solid fa-mobile-screen-button  # Celular
fa-regular fa-folder-open         # Carpeta
fa-regular fa-key                 # Llave
fa-regular fa-lightbulb           # Bombilla/idea
fa-regular fa-magnifying-glass    # Lupa
fa-regular fa-newspaper           # Periódico
fa-regular fa-image               # Imagen/foto
fa-regular fa-map                 # Mapa
```

Explorá más en: https://fontawesome.com/search?o=r&m=free

---

## 🔧 Actualizar Contenido de Documentos

### Cambiar el contenido de un documento existente

```sql
UPDATE documento 
SET contenido = '<h3>Nuevo Título</h3><p>Nuevo contenido aquí...</p>'
WHERE id = 1;
```

### Cambiar el código correcto

```sql
UPDATE documento 
SET codigo_correcto = '9999'
WHERE id = 1;
```

### Agregar más documentos a una historia existente

```sql
INSERT INTO documento (historia_id, clave, nombre, icono, contenido) 
VALUES (1, 'nuevo_doc', 'Nuevo Documento', 'fa-regular fa-file', 
        '<h3>Título</h3><p>Contenido...</p>');
```

---

## 🐛 Solución de Problemas

### El código no se valida correctamente

**Verificá**:
1. Que el código en la BD sea exacto (sin espacios)
2. Que `pista_nombre` coincida con un nombre en la tabla `pista`

```sql
-- Verificar código correcto
SELECT codigo_correcto, pista_nombre FROM documento WHERE historia_id = 1;

-- Verificar que existe la pista
SELECT nombre FROM pista WHERE historia_id = 1 AND nombre = 'Código de la computadora';
```

### Los documentos no aparecen en el juego

**Verificá**:
1. Que el `historia_id` sea correcto
2. Que compilaste el proyecto después de agregar DocumentoDAO

```sql
-- Ver todos los documentos
SELECT d.id, h.titulo, d.nombre, d.clave 
FROM documento d 
JOIN historia h ON d.historia_id = h.id
ORDER BY d.historia_id, d.id;
```

### Error de compilación

Los errores de `jakarta.servlet` y `entities.Documento` son normales hasta que Eclipse compile completamente. 

**Solucioná**:
1. Click derecho en el proyecto → Refresh
2. Project → Clean... → Clean all projects
3. Project → Build Project

---

## 💡 Ventajas del Sistema

✅ **Escalable**: Agregá historias sin tocar código Java  
✅ **Mantenible**: Cambios de contenido solo en la BD  
✅ **Flexible**: HTML rico para documentos atractivos  
✅ **Consistente**: Mismo flujo para todas las historias  
✅ **Dinámico**: Los jugadores ven documentos específicos de su historia  

---

## 📚 Ejemplos de Uso

### Ejemplo 1: Historia de Detectives Clásica

```sql
INSERT INTO documento (historia_id, clave, nombre, icono, contenido, codigo_correcto, pista_nombre) 
VALUES
(4, 'testamento', 'Testamento de la víctima', 'fa-regular fa-file-contract',
 '<h3>Testamento</h3><p>Dejo todo a quien descubra mi asesino...</p>',
 NULL, NULL),
 
(4, 'huellas', 'Informe de huellas dactilares', 'fa-solid fa-fingerprint',
 '<h3>Análisis Forense</h3><p>Coinciden con el sospechoso #<strong>4567</strong></p>',
 '4567', 'Huellas del asesino');
```

### Ejemplo 2: Historia de Espionaje

```sql
INSERT INTO documento (historia_id, clave, nombre, icono, contenido, codigo_correcto, pista_nombre) 
VALUES
(5, 'mensaje_cifrado', 'Mensaje interceptado', 'fa-solid fa-lock',
 '<h3>Mensaje Cifrado</h3><p style="font-family:monospace;">ALFA-BRAVO-1-2-3-4</p>',
 'AB1234', 'Código de acceso secreto');
```

---

## 🤝 Contribuir

Si necesitás agregar funcionalidades como:
- Upload de imágenes para documentos
- Documentos con video/audio
- Documentos que se desbloquean progresivamente
- Sistema de pistas encadenadas

Podés extender las clases `Documento` y `DocumentoDAO` manteniendo la estructura base.

---

**¡Listo! Tu juego ahora es completamente dinámico y escalable! 🎉**
