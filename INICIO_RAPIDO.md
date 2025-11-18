# ⚡ INICIO RÁPIDO - 5 Minutos

## 🎯 Para empezar en menos de 5 minutos

### ✅ Checklist Rápido

```
□ Paso 1: Ejecutar SQL            (1 min)
□ Paso 2: Verificar datos          (1 min)
□ Paso 3: Compilar proyecto        (2 min)
□ Paso 4: Probar en navegador      (1 min)
```

---

## 📋 Paso 1: Ejecutar SQL (1 minuto)

### Opción A: Desde MySQL Workbench
1. Abrí MySQL Workbench
2. Conectate a tu servidor local
3. Seleccioná la base de datos: `USE misterio_mansion;`
4. Abrí el archivo: `sql/create_documentos.sql`
5. Ejecutá todo (⚡ botón rayo)

### Opción B: Desde línea de comandos
```bash
mysql -u root -p misterio_mansion < "sql/create_documentos.sql"
```

### Opción C: Copiar y Pegar
1. Abrí `sql/create_documentos.sql`
2. Copiá todo el contenido (Ctrl+A, Ctrl+C)
3. Pegalo en tu cliente SQL favorito
4. Ejecutá

---

## 🔍 Paso 2: Verificar (1 minuto)

Ejecutá estos comandos rápidos:

```sql
-- ¿Se creó la tabla?
SELECT COUNT(*) AS total FROM documento;
-- Esperado: 5 (2 de historia 1 + 3 de historia 2)

-- ¿Historia 1 tiene código 7391?
SELECT codigo_correcto FROM documento WHERE historia_id = 1 AND codigo_correcto IS NOT NULL;
-- Esperado: 7391

-- ¿Historia 2 tiene código 2580?
SELECT codigo_correcto FROM documento WHERE historia_id = 2 AND codigo_correcto IS NOT NULL;
-- Esperado: 2580
```

**Si ves los resultados esperados: ✅ ¡Perfecto!**

---

## 🔨 Paso 3: Compilar (2 minutos)

### En Eclipse:
```
1. Click derecho en el proyecto "MisterioEnLaMansion"
2. Refresh (F5)
3. Project → Clean...
4. Seleccioná el proyecto
5. Click "Clean"
6. Esperá la compilación automática
```

### Verificar compilación exitosa:
- Mirá la consola de Eclipse
- No debe haber errores rojos en `Documento.java` ni `DocumentoDAO.java`
- Si hay warnings de "Incorrect Package", ignoralos (son falsos positivos)

---

## 🌐 Paso 4: Probar (1 minuto)

### 1. Iniciar servidor
- En Eclipse: Click en el botón "Start Server" (▶️)
- Esperá que diga "Server started"

### 2. Probar Historia 1
1. Abrí navegador: `http://localhost:8080/MisterioEnLaMansion/`
2. Logueate
3. Iniciá nueva partida → Historia 1
4. Click en botón "Documentos" 📂
   - **Deberías ver**: 2 documentos
   - Nota manuscrita
   - Correo sospechoso
5. En el chat: "Descubrí algo importante"
6. Ingresá código: `7391`
7. **Resultado esperado**: ✅ "¡Excelente! Código correcto"

### 3. Probar Historia 2
1. Nueva partida → Historia 2
2. Click "Documentos" 📂
   - **Deberías ver**: 3 documentos
   - Diario de la víctima
   - Nota de recepción
   - Mensaje celular
3. Chat → "Descubrí algo importante"
4. Ingresá código: `2580`
5. **Resultado esperado**: ✅ "¡Excelente! Código correcto"

---

## 🎉 ¡Listo!

Si todo funcionó correctamente, ahora tenés:

✅ Sistema completamente dinámico  
✅ Cada historia con sus propios documentos  
✅ Códigos diferentes por historia  
✅ Fácil de agregar nuevas historias  

---

## 🐛 Problemas Comunes

### ❌ Error: "Table 'documento' doesn't exist"
**Solución**: Ejecutá `sql/create_documentos.sql`

### ❌ Los documentos no aparecen en el juego
**Solución**: 
1. Verificá que la tabla tenga datos: `SELECT * FROM documento;`
2. Refrescá el proyecto en Eclipse (F5)
3. Limpiá y compilá: Project → Clean

### ❌ Código no valida correctamente
**Solución**:
1. Verificá en la BD: 
   ```sql
   SELECT codigo_correcto, pista_nombre 
   FROM documento 
   WHERE historia_id = 1;
   ```
2. Verificá que la pista exista:
   ```sql
   SELECT nombre FROM pista WHERE historia_id = 1;
   ```

### ❌ Errores de compilación en Eclipse
**Solución**:
1. Project → Clean
2. Project → Build Project
3. Si persisten, reiniciá Eclipse

---

## 📚 Documentación Completa

Para más detalles, consultá:

- **RESUMEN_EJECUTIVO.md** - Visión general del sistema
- **DOCUMENTOS_DINAMICOS_README.md** - Guía completa (15 páginas)
- **FLUJO_SISTEMA.md** - Diagramas de flujo detallados
- **sql/verificar_documentos.sql** - Diagnóstico completo

---

## 🚀 Próximos Pasos

### Agregar Historia 3 (5 minutos)

```sql
-- Copiar y pegar en tu cliente SQL
INSERT INTO documento 
(historia_id, clave, nombre, icono, contenido, codigo_correcto, pista_nombre) 
VALUES
(3, 'pista_principal', 'Documento Clave', 'fa-regular fa-key',
 '<h3>La Clave Secreta</h3><p>El código está en la página <strong>1-2-3-4</strong></p>',
 '1234', 'Código secreto'),
 
(3, 'pista_extra', 'Pista Adicional', 'fa-regular fa-lightbulb',
 '<h3>Pista Extra</h3><p>Buscá en el libro antiguo...</p>',
 NULL, NULL);
```

¡Y listo! Historia 3 funcionando sin tocar código.

---

## 💡 Tips de Productividad

### Cambiar código rápido
```sql
UPDATE documento SET codigo_correcto = '9999' WHERE id = 1;
```

### Ver todos los códigos
```sql
SELECT h.titulo, d.codigo_correcto, d.pista_nombre
FROM documento d
JOIN historia h ON d.historia_id = h.id
WHERE d.codigo_correcto IS NOT NULL;
```

### Editar contenido de documento
```sql
UPDATE documento 
SET contenido = '<h3>Nuevo Título</h3><p>Nuevo contenido...</p>'
WHERE id = 4;
```

---

**¡Empezá a jugar! 🎮**

*Cualquier duda, revisá los otros archivos de documentación.*
