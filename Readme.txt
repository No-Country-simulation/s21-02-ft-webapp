<h1 align="center">
  <a href=""><img src="https://media.discordapp.net/attachments/1338664601998594108/1346274826494545962/image.png?ex=67d02963&is=67ced7e3&hm=1db9744e90a883f87b2a31b25de1fc851053b31b5aaf66954c97660baae19f23&=&format=webp&quality=lossless&width=585&height=276" width="400" alt="Logo"></a>
</h1>

<h3 align="center">📌 Alcance del Proyecto: Wallex - Sistema de Gestión Financiera</h3>
<p align="center">
Wallex es un sistema de gestión financiera diseñado para proporcionar a los usuarios una plataforma segura y eficiente para administrar su dinero. A través de esta solución, los usuarios podrán realizar operaciones como transferencias, consulta de saldo, gestión de tarjetas y recibir notificaciones en tiempo real sobre sus movimientos financieros.
El objetivo principal del sistema es facilitar la gestión del dinero de manera intuitiva, segura y accesible, asegurando una experiencia óptima para los usuarios.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/spring%20boot-2.5.3-brightgreen">
  <img src="https://img.shields.io/badge/react-18.2.0-blue">
  <img src="https://img.shields.io/badge/tailwind%20css-latest-purple">
  <img src="https://img.shields.io/badge/figma-latest-orange">
  <img src="https://img.shields.io/badge/jasmine-3.8.0-yellow">
  <img src="https://img.shields.io/badge/git-latest-lightgrey">
  <img src="https://img.shields.io/badge/github-latest-darkblue">
</p>

<p align="center">
  <img src="https://i.postimg.cc/zvRyP3Mc/image0.png?ex=67c796dd&is=67c6455d&hm=c8b1adf57f2d7a2637bcd46cd70f5f8218c3b42624641a5163e44551348ea16f&=&format=webp&quality=lossless&width=353&height=614" alt="Vista de la aplicación" width="400">
</p>


## 📋 **Índice**
1. [🚀 Tecnologías Utilizadas](#-tecnologías-utilizadas)  
2. [🛠️ Instalación y Configuración](#️-instalación-y-configuración)  
3. [🗄️ Modelo de Datos](#️-modelo-de-datos)  
4. [📡 Documentación de la API](#-documentación-de-la-api)
5. [📌 Tablero de Trello](#-tablero-de-trello)  
6. [👥 Contribuidores](#-contribuidores)  

---  
## 🚀 **Tecnologías Utilizadas**  

- **Backend:** ☕ Java 21, Spring Boot, Spring Security, JWT  
- **Frontend:** 💻 React.js, Tailwind CSS, Figma  
- **Base de Datos:** 🗄️ MySQL, Hibernate, JPA  
- **QA:** 🔍 Jasmine  
- **Colaboración:** 🌐 GitHub  

## 🛠️ **Instalación y Configuración**  

### 🔧 **Requisitos Previos**
- Java 21+
- Maven
- MySQL 
- Node.js 
---

## ⚙️ **Configuración del Proyecto Backend**  

### **1️⃣ Clonar el Repositorio**
```bash
git clone https://github.com/No-Country-simulation/s21-02-ft-webapp.git
cd s21-02-ft-webapp/backend
```

## 2️⃣ **Configuración de la Base de Datos**
Para configurar la base de datos, necesitas editar el archivo `application.properties` y establecer las credenciales adecuadas dependiendo del motor de base de datos que utilices (H2 o MySQL).

### **Opciones de Configuración**
- **Principal**: Desde aquí configuras si se ejecutará con H2 o MySQL.
- **H2**: Base de datos en memoria, ideal para pruebas rápidas.
- **MySQL**: Base de datos relacional, recomendada para producción.

### **Configuración General**
Si decides usar H2 como base de datos en memoria o MySQL como base de datos relacional, debes editar el archivo application.properties para definir cuál se utilizará. La propiedad clave para cambiar entre H2 y MySQL es `spring.profiles.active`.
```properties
# Nombre de la aplicación
spring.application.name=financial-platform

# Perfil activo (cambiar entre 'mysql' y 'h2')
spring.profiles.active=mysql
debug=true

# Puerto del servidor
server.port=9091

# Configuración de salida y registro
spring.output.ansi.enabled=ALWAYS
logging.pattern.console=%clr(%d{yyyy-MM-dd HH:mm:ss}){magenta} %clr(%-5p) %clr(%c{1}){blue}:%clr(%L){cyan} - %m%n

# Configuración de correo electrónico (si es necesario)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.protocol=smtp
spring.mail.test-connection=true
```

#### **Configuración para H2**
Si deseas utilizar H2 como base de datos en memoria, usa la siguiente configuración:
```application-h2.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=${H2_DB_USERNAME}
spring.datasource.password=${H2_DB_PASSWORD}

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```
Una vez configurado, puedes acceder a la consola de H2 en `http://localhost:9091/h2-console` e ingresar las credenciales establecidas.

#### **Configuración para MySQL**
Si prefieres utilizar MySQL, asegúrate de que el servidor de MySQL esté corriendo y usa la siguiente configuración:
```application-mysql.properties
spring.datasource.url=${MYSQL_DB_URL}
spring.datasource.username=${MYSQL_DB_USERNAME}
spring.datasource.password=${MYSQL_DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```
También puedes configurar MySQL para que la base de datos se cree automáticamente si no existe agregando `?createDatabaseIfNotExist=true` a la URL de conexión.

### **3️⃣ Configurar las Variables de Entorno**
Crea un archivo `.env` en la raíz del proyecto y define las siguientes variables:
```env
MAIL_USERNAME=*******************
MAIL_PASSWORD=**** **** **** ****

JWT_SECRET_KEY=SECRETOMUYSEGURODEMASDE256BITS...........

MYSQL_DB_URL=jdbc:mysql://localhost:3306/wallex_db?createDatabaseIfNotExist=true&serverTimezone=UTC
MYSQL_DB_USERNAME=root
MYSQL_DB_PASSWORD=admin

H2_DB_USERNAME=sa
H2_DB_PASSWORD=

SERVER_PORT=9091
```
> 📌 **Nota**: `MAIL_USERNAME` y `MAIL_PASSWORD` son las credenciales del correo desde el cual se enviarán los emails de la aplicación. Si utilizas Gmail, debes ingresar tu dirección de correo en `MAIL_USERNAME` y en `MAIL_PASSWORD` debes usar una [contraseña de aplicación](https://support.google.com/accounts/answer/185833?hl=es), ya que Gmail no permite el uso de la contraseña normal por motivos de seguridad.

Asegúrate de que este archivo no se suba al repositorio agregándolo al `.gitignore`.

### **4️⃣ Configurar el IDE para Usar el Archivo .env**
1. Instala el plugin **"EnvFile"** en IntelliJ IDEA.
2. Ve a **Run/Debug Configurations**.
3. Selecciona tu configuración de ejecución.
4. Activa **"Enable EnvFile"**.
5. Añade el archivo `.env` a la lista.

### **5️⃣ Ejecutar el Backend desde IntelliJ IDEA**
- **Con atajo de teclado**: `Shift + F10`
- **Con el botón de ejecución**: Haz clic en el ícono de **play verde** en la esquina superior derecha.

### **6️⃣ Probar la API con Postman**
1. **Accede a la documentación de la API** en [Postman](https://documenter.getpostman.com/view/27409208/2sAYdeLBfe).
2. **Configura las variables `{{base_url}}` y `{{bearer_token}}`**:
   - `{{base_url}}`: Verifica que coincida con el puerto del backend (ej. `http://localhost:9091`).
   - `{{bearer_token}}`: Usa el token obtenido tras iniciar sesión.

3. **Iniciar sesión y obtener el token**:
   - Realiza una solicitud `POST` al endpoint de login según la documentación.

4. **Probar un endpoint protegido**:
   ```bash
   GET http://localhost:9091/api/users
   ```

## ⚙️ **Configuración del Proyecto Frontend**
### **1️⃣ Clonar el Repositorio**
```bash
git clone https://github.com/No-Country-simulation/s21-02-ft-webapp.git
cd s21-02-ft-webapp/frontend
```
### **2️⃣ Instalar Dependencias**
Una vez dentro del directorio `frontend`, instala las dependencias necesarias para ejecutar el proyecto:
```bash
npm install
```
### **3️⃣ Ejecutar el Proyecto**
Luego de instalar las dependencias, puedes ejecutar el proyecto en modo desarrollo con el siguiente comando:
```bash
npm run dev
```
### **4️⃣ Abrir la Web**
Después de ejecutar el proyecto, verás en la terminal un mensaje similar al siguiente:
```bash
VITE v6.1.0  ready in 1337 ms
  ➜  Local:   http://localhost:5173/
  ➜  Network: use --host to expose
  ➜  press h + enter to show help
 ```
Haz clic en el enlace Local: http://localhost:5173/ para abrir la aplicación en tu navegador.

---
## 🗄️ Modelo de Datos  

### 📌 **Diagrama Entidad-Relación (ER)**  
<p align="center">
  <img src="https://i.postimg.cc/9z5D3CHF/image.png?ex=67c79c0e&is=67c64a8e&hm=75f03ac965247e70526aacc9419c37efc97e908fc2385081d5b0eaee2afb3504&=&format=webp&quality=lossless&width=471&height=614" alt="Modelo de Datos" width="600">
</p>

### 📄 **Descripción de las Entidades**  
El modelo de datos está diseñado para gestionar usuarios, cuentas, transacciones y notificaciones en el sistema financiero. A continuación, se detallan las entidades principales:

- **Users:** Representa a los usuarios del sistema, almacenando información personal y credenciales de acceso.  
- **Accounts:** Cuentas bancarias asociadas a los usuarios, con saldo y datos clave como CBU y alias.  
- **Cards:** Tarjetas de los usuarios, incluyendo información encriptada para mayor seguridad.  
- **Transactions:** Registro de transacciones entre cuentas, con montos, estados y razones.  
- **Movements:** Historial de movimientos de cada cuenta, relacionado con transacciones.  
- **Reservations:** Monto reservado de una cuenta para futuras transacciones o pagos pendientes.  
- **Notifications:** Mensajes enviados a los usuarios, con estados y tipos definidos.  

### 🏷️ **Enums Importantes**  
Para asegurar consistencia en los estados y tipos de datos, se definen los siguientes **Enums**:  

- **Status:** Estados de transacciones, movimientos y reservas (`PENDING`, `COMPLETED`, `FAILED`).  
- **Type:** Tipos de transacción (`DEPOSIT`, `WITHDRAWAL`, `TRANSFER`).  
- **Currency:** Tipos de moneda manejados en las cuentas (`USD`, `ARS`, `EUR`).  
- **NotificationType:** Tipos de notificaciones (`ALERT`, `REMINDER`, `TRANSACTION`).  

---

## 📚 **Documentación de la API**  
La API está documentada con **Postman**. Accede en:  
🔗 [Postman](https://documenter.getpostman.com/view/27409208/2sAYdeLBfe)  

---

## 📌 **Tablero de Trello**  
Consulta el progreso de las tareas y el flujo de trabajo en el tablero de Trello:
🔗 [Trello](https://trello.com/b/hvlF7fCH/plataforma-web-de-servicios-financieros-wallex)  

---

## 👥 **Contribuidores**

| **Rol**   | **Nombre y Apellido**                | **Correo**                      | **Tecnologías**                              | **Github**                         |
|-----------|--------------------------------------|---------------------------------|----------------------------------------------|------------------------------------|
| Backend   | Delmer Rodríguez                     | jindrg@gmail.com                | Java Spring Boot, .NET                      | DelmerRo                           |
| Backend   | Gustavo Paz                          | gusti.paz@gmail.com             | Java Spring Boot, Node.js                   | guspaz0                            |
| Frontend  | Sebastián Tournier                   | sebastian.tournier@gmail.com    | React, Bootstrap                            | TournierSebastian                  |
| UX y UI   | Federico Merediz                     | fedemerediz@hotmail.com         | Figma                                       | Federico                           |
| Frontend  | Gastón Federico Nahuel Gómez         | gastongomez2014@hotmail.com     | React, Tailwindcss, Bootstrap               | Morfeo1997                         |

---
