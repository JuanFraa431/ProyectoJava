# 🔧 Solución al Error: ClassNotFoundException: HttpServletRequest

## 📋 Problema

```
Caused by: java.lang.ClassNotFoundException: HttpServletRequest
```

Este error ocurre porque **Tomcat 10** requiere `jakarta.servlet-api` en lugar de `javax.servlet-api`.

---

## ✅ Solución Rápida

### Opción 1: Descargar la librería manualmente

1. **Descargá la librería:**
   - URL: https://repo1.maven.org/maven2/jakarta/servlet/jakarta.servlet-api/6.0.0/jakarta.servlet-api-6.0.0.jar

2. **Reemplazá la librería antigua:**
   ```powershell
   # En PowerShell, desde el directorio del proyecto:
   cd "e:\eclipse workspace\MisterioEnLaMansion\src\main\webapp\WEB-INF\lib"
   
   # Eliminar servlet-api.jar antigua
   Remove-Item servlet-api.jar
   
   # Copiar la nueva (ajustar la ruta donde descargaste)
   Copy-Item "C:\Users\tuusuario\Downloads\jakarta.servlet-api-6.0.0.jar" .
   ```

3. **Refrescar el proyecto en Eclipse:**
   - Click derecho en el proyecto → **Refresh** (F5)
   - **Project** → **Clean...** → Clean all projects
   - **Project** → **Build Project**

4. **Reiniciar Tomcat**

---

### Opción 2: Usar Maven/Gradle (si tu proyecto lo soporta)

Si tu proyecto usa Maven, agregá esta dependencia en `pom.xml`:

```xml
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.0.0</version>
    <scope>provided</scope>
</dependency>
```

---

### Opción 3: Descargar con PowerShell

Ejecutá este comando en PowerShell:

```powershell
# Navegar al directorio lib
cd "e:\eclipse workspace\MisterioEnLaMansion\src\main\webapp\WEB-INF\lib"

# Descargar jakarta.servlet-api
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/jakarta/servlet/jakarta.servlet-api/6.0.0/jakarta.servlet-api-6.0.0.jar" -OutFile "jakarta.servlet-api-6.0.0.jar"

# Eliminar la antigua
Remove-Item servlet-api.jar -ErrorAction SilentlyContinue

Write-Host "✅ Librería descargada exitosamente" -ForegroundColor Green
```

---

## 🔍 Verificación

Después de instalar la librería correcta:

1. **Verificá que el archivo exista:**
   ```powershell
   ls "e:\eclipse workspace\MisterioEnLaMansion\src\main\webapp\WEB-INF\lib\jakarta.servlet-api-6.0.0.jar"
   ```

2. **Compilá el proyecto:**
   - Eclipse: **Project** → **Build Project**
   - Deberías ver que los errores de compilación desaparecen

3. **Iniciá Tomcat:**
   - No debería haber más errores de `ClassNotFoundException`

---

## 📦 Librerías Actuales vs Correctas

### ❌ ANTES (Incorrecto para Tomcat 10)
```
WEB-INF/lib/
  ├── servlet-api.jar          ← javax.servlet (antigua)
  └── mysql-connector-j-9.4.0.jar
```

### ✅ DESPUÉS (Correcto para Tomcat 10)
```
WEB-INF/lib/
  ├── jakarta.servlet-api-6.0.0.jar  ← jakarta.servlet (nueva)
  └── mysql-connector-j-9.4.0.jar
```

---

## 🎯 ¿Por qué ocurre esto?

| Versión Tomcat | Namespace | Librería Requerida |
|----------------|-----------|-------------------|
| Tomcat 9 y anteriores | `javax.servlet.*` | servlet-api.jar |
| **Tomcat 10+** | **`jakarta.servlet.*`** | **jakarta.servlet-api.jar** |

Tu proyecto ya usa `import jakarta.servlet.*` (correcto para Tomcat 10), pero faltaba la librería en `WEB-INF/lib`.

---

## 🚀 Después de Solucionar

Una vez instalada la librería correcta:

1. ✅ El servidor Tomcat arrancará sin errores
2. ✅ ChatServlet funcionará correctamente
3. ✅ La Historia 2 guardará todos los datos (pistas, puntuación, solución)

---

## 💡 Alternativa: Downgrade a Tomcat 9

Si no querés cambiar la librería, podés usar Tomcat 9:

1. Descargá Tomcat 9.x
2. Cambiá todos los `import jakarta.servlet.*` por `import javax.servlet.*`
3. Usá `servlet-api.jar` (la que ya tenés)

**No recomendado**: Es mejor usar Tomcat 10+ con Jakarta EE.

---

## 📞 ¿Sigue sin funcionar?

Verificá estos puntos:

1. **Build Path en Eclipse:**
   - Click derecho en el proyecto → **Properties**
   - **Java Build Path** → **Libraries**
   - Debe aparecer `jakarta.servlet-api-6.0.0.jar`

2. **Server Runtime:**
   - **Window** → **Preferences** → **Server** → **Runtime Environments**
   - Verificá que esté configurado Tomcat 10.1

3. **Clean Workspace:**
   - **Project** → **Clean...**
   - Seleccioná "Clean all projects"
   - Reiniciá Eclipse

---

**¡Seguí estos pasos y tu servidor funcionará correctamente!** 🎉
