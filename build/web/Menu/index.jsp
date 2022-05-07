<%-- 
    Document   : index
    Created on : 5 may. 2022, 20:26:17
    Author     : Gustavo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>iBanco</title>
        <link rel="stylesheet" type="text/css" href="/BancoAppWeb/CSS/estilos.css"/>
    </head>
    <body>
        <nav class="barnav">
            <h1 id="titulo">iBanco</h1>
            <ul class="nav-ul">
                <li class="nav-li">
                    <a href="MenuControlador?accion=registrar">Registrar cliente</a>
                </li>
                <li class="nav-li">
                    <a href="">Crear cuenta</a>
                </li>
                <li class="nav-li">
                    <a href="">Listar clientes</a>
                </li>
                <li class="nav-li">
                    <a href="">Listar cuentas</a>
                </li>
            </ul>
        </nav>
        <div class="contenedor">
            <h2>Seleccione la acción que desee realizar en el sistema</h2>
           
            <h3 id="mensaje-exito">Último mensaje del sistema:<br><br><br><%out.print(session.getAttribute("mensaje"));%></h3>
        </div>

    </body>
</html>
