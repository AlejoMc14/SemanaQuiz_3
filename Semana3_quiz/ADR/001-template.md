ADR-001: Refactorización del módulo de autenticación por vulnerabilidades de seguridad y violaciones a Clean Code
Contexto

El sistema actual implementa un módulo básico de autenticación que permite registrar usuarios y realizar login contra una base de datos PostgreSQL. Funcionalmente el sistema responde correctamente a los endpoints /login, /register y /health. Sin embargo, durante la auditoría de código se identificaron múltiples problemas graves de seguridad y diseño.

Entre los hallazgos más críticos se encuentran vulnerabilidades de SQL Injection debido al uso de Statement con concatenación directa de strings, uso del algoritmo MD5 para hashing de contraseñas (considerado inseguro y obsoleto), exposición del hash en la respuesta del login, credenciales de base de datos hardcodeadas en el repositorio, y una validación extremadamente débil de contraseñas. Además, el modelo User tiene atributos públicos, violando encapsulamiento, y existen problemas de naming que afectan la legibilidad.

Esta situación es urgente porque compromete directamente la seguridad de los usuarios y la integridad de la base de datos. En un entorno productivo, estas vulnerabilidades podrían permitir acceso no autorizado, robo de información o manipulación de datos. Los principales afectados serían los usuarios finales (exposición de credenciales), el equipo de desarrollo (aumento de deuda técnica) y el negocio (riesgo reputacional y legal).

Por lo tanto, es necesario tomar una decisión arquitectónica formal para refactorizar el módulo antes de continuar evolucionando el sistema.

Decisión

Se decide refactorizar el módulo de autenticación aplicando principios de Clean Code, SOLID y buenas prácticas de seguridad.

1️⃣ Reemplazar Statement por PreparedStatement

Se eliminará la concatenación de strings en consultas SQL y se utilizará PreparedStatement para prevenir SQL Injection.
Esta decisión se basa en el principio de seguridad por diseño y en la necesidad de eliminar vulnerabilidades críticas detectadas en la auditoría.

Esto garantiza que los parámetros enviados por el usuario no puedan alterar la estructura de la consulta SQL.

2️⃣ Reemplazar MD5 por BCrypt

Se eliminará el uso de MD5 y se implementará BCrypt como algoritmo de hashing seguro con salt incorporado.

Se elige BCrypt porque:

Está diseñado específicamente para hashing de contraseñas

Es resistente a ataques de fuerza bruta

Es estándar en aplicaciones modernas

Esto mejora significativamente la seguridad de las credenciales almacenadas.

3️⃣ Aplicar encapsulamiento y mejorar el modelo de dominio

La clase User será modificada para:

Hacer atributos privados

Agregar getters y setters

Evitar exposición directa de datos sensibles

Se aplica el principio de Encapsulamiento y SRP, permitiendo mayor control sobre los datos del dominio.

4️⃣ Eliminar exposición de datos sensibles en respuestas

Se eliminará el campo "hash" de la respuesta del login, ya que exponer hashes de contraseña viola el principio de mínima exposición.

La respuesta solo deberá indicar:

Si la autenticación fue exitosa

Información no sensible del usuario

5️⃣ Externalizar credenciales de base de datos

Las credenciales hardcodeadas en UserRepository serán movidas a variables de entorno o archivo de configuración (application.properties).

Esto mejora la seguridad y sigue buenas prácticas DevOps.

Nueva arquitectura resultante

Controller → solo maneja HTTP

Service → lógica de negocio limpia y segura

Repository → acceso a datos seguro con consultas parametrizadas

Modelo → encapsulado

Seguridad → hashing robusto y validación fuerte

Se mantiene la estructura en capas, pero con responsabilidades claramente definidas y mayor seguridad.

Consecuencias
✅ Consecuencias positivas

Eliminación de vulnerabilidades críticas (SQL Injection)

Protección real de contraseñas

Mejor mantenibilidad y legibilidad

Reducción de deuda técnica

Cumplimiento de buenas prácticas de seguridad

⚠️ Consecuencias o riesgos

Tiempo adicional de refactorización

Posibles errores durante la migración

Necesidad de re-hashear contraseñas existentes

Curva de aprendizaje si el equipo no domina BCrypt

Sin embargo, estos riesgos son menores comparados con el impacto de mantener el sistema vulnerable.

Alternativas consideradas
1️⃣ Reescribir completamente el módulo desde cero

Fue considerada como opción, pero se descartó porque:

Aumenta el tiempo de entrega

Puede introducir nuevos errores

No es necesario si se puede refactorizar progresivamente

2️⃣ Corregir solo la vulnerabilidad de SQL Injection

Se descartó porque:

Dejaría el sistema usando MD5 (inseguro)

No resolvería problemas de diseño

Mantendría deuda técnica acumulada

La decisión final fue realizar una refactorización integral controlada, abordando tanto seguridad como calidad de código.