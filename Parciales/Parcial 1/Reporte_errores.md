# Reporte de Errores y Correcciones - 1er Examen Práctico SIS-258

**Materia:** Sistemas Distribuidos  
**Ejercicio:** Venta de Tours (Migración, Operadora, Banco, Antifraude)

---

## 1. Errores Encontrados durante la Compilación (Maven Build Failure)

Durante el desarrollo y la integración del código en el proyecto `Venta_Tours`, surgieron 13 errores de compilación reportados por el plugin de Maven. Estos errores no fueron de lógica de negocio, sino de **desincronización entre el esqueleto del proyecto y la implementación final**. 

Los problemas principales fueron:

*   **Incompatibilidad de Constructores (`Pago` y `Voucher`):**  
    *Error de consola:* `constructor Pago cannot be applied to given types; required: no arguments; found: boolean, String, String`  
    *Causa:* Las clases base originales estaban vacías o tenían constructores por defecto (sin parámetros), mientras que los servidores RMI intentaban instanciarlas enviando los datos requeridos por el diagrama del examen (ej. estado de aprobación, código, motivo).
*   **Firmas de Métodos Incorrectas en Interfaces RMI:**  
    *Error de consola:* `method does not override or implement a method from a supertype` y `double cannot be converted to java.lang.String`.  
    *Causa:* En la interfaz `IBanco`, el método original esperaba recibir el monto como `String`, pero el cálculo del 50% de descuento en la `Operadora` obligaba a enviar un `double`. En `IOperadora`, la firma original no coincidía con los tres parámetros exigidos `(String pasaporte, String codigoTour, int personas)`.
*   **Conflicto de Archivos Duplicados:**  
    Se generó una colisión en el compilador al tener dos archivos intentando definir al cliente: `Cliente_turista.java` y `Clienteturista.java`.

## 2. Correcciones Realizadas

Para solucionar los problemas y obtener un `BUILD SUCCESS`, se aplicaron las siguientes refactorizaciones:

1.  **Actualización de Modelos Base:** Se sobrescribieron las clases `Pago.java` y `Voucher.java` asegurando que implementen `Serializable` y tengan los atributos exactos exigidos en la hoja del examen. Se crearon los constructores parametrizados correspondientes.
2.  **Refactorización de Interfaces:** Se ajustaron los tipos de datos en las interfaces RMI. Se cambió el monto a `double` en `IBanco.java` y se especificaron los tres parámetros correctos en `IOperadora.java`, lo que eliminó los errores de `@Override` en las clases servidoras.
3.  **Limpieza del Proyecto:** Se eliminó el archivo redundante `Cliente_turista.java` para mantener una única clase de ejecución final (`Clienteturista.java`).
