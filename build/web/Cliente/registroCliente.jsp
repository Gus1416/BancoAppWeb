<%-- 
    Document   : registrar
    Created on : 5 may. 2022, 22:23:24
    Author     : Gustavo
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="/BancoAppWeb/CSS/estilos.css"/>
<!--        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">-->
    </head>
    <body>
        <nav class="barnav">
            <h1 id="titulo">iBanco</h1>
            
            <ul class="nav-ul">
                <li class="nav-li">
                    <a href="MenuControlador?accion=registrarCliente">Registrar cliente</a>
                </li>
                <li class="nav-li">
                    <a href="MenuControlador?accion=registrarCuenta">Crear cuenta</a>
                </li>
                <li class="nav-li">
                    <a href="MenuControlador?accion=listarClientes">Listar clientes</a>
                </li>
                <li class="nav-li">
                    <a href="MenuControlador?accion=listarCuentas">Listar cuentas</a>
                </li>
                <li class="nav-li">
                    <a href="MenuControlador?accion=cambiarPin">Cambiar pin</a>
                </li>
            </ul>
        </nav>
        
        
        <div class="contenedor">
            
            <h2>Registrar cliente</h2>

            <form class="reg-form" action="ClienteControlador?accion=guardar" method="POST" autocomplete="off">

                <div class="form-input-container">
                    <label class="form-label">Primer apellido</label>
                    <input class="form-input" id="primerApellido" name="primerApellido" type="text" required/>
                </div>

                <div class="form-input-container">
                    <label class="form-label">Segundo apellido</label>
                    <input class="form-input"  id="segundoApellido" name="segundoApellido" type="text" required/>
                </div>

                <div class="form-input-container">
                    <label class="form-label">Nombre</label>
                    <input class="form-input"  id="nombre" name="nombre" type="text" required/>
                </div>

                <div class="form-input-container">
                    <label class="form-label">Identificación</label>
                    <input class="form-input"  id="identificacion" name="identificacion" type="text" pattern="^(?=.*[0-9]).{9,9}$" title="Indique la cédula usando ceros (sin guiones)" required/>
                </div>

                <div class="form-input-container">
                    <label class="form-label">Fecha de nacimiento</label>
                    <input class="form-input"  id="fechaNacimiento" name="fechaNacimiento" type="date" required/>      
                </div>

                <div class="form-input-container">
                    <label class="form-label">Número de teléfono</label>
                    <input class="form-input"  id="numeroTelefono" name="numeroTelefono" type="tel" required/>   
                </div>

                <div class="form-input-container">
                    <label class="form-label">Correo electrónico</label>
                    <input class="form-input"  id="correoElectronico" name="correoElectronico" type="email" required/>    
                </div>

                <button id="guardar" name="guardar" type="submit">Registrar</button>

            </form>

        </div>
    </body>
</html>
