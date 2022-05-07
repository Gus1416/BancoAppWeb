<%-- 
    Document   : listaClientes
    Created on : 7 may. 2022, 13:43:37
    Author     : Gustavo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="/BancoAppWeb/CSS/estilos.css"/>
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
                    <a href="">Listar clientes</a>
                </li>
                <li class="nav-li">
                    <a href="">Listar cuentas</a>
                </li>
            </ul>
        </nav>
        
        <div class="contenedor">
            <h2>Clientes registrados</h2>
            <br /> <br />

            <table class="listas_registros">

                <thead>           
                    <tr>
                        <th>Primer Apellido</th>
                        <th>Segundo Apellido</th>
                        <th>Nombre</th>
                        <th>Identificación</th>
                    </tr>        
                </thead>

                <tbody>

                    <c:forEach var="cliente" items="${clientes}">

                        <tr>
                            <td><c:out value="${cliente.primerApellido}"/></td>
                            <td><c:out value="${cliente.segundoApellido}"/></td>
                            <td><c:out value="${cliente.nombre}"/></td>
                            <td><c:out value="${cliente.identificacion}"/></td>        
                        </tr>

                    </c:forEach>

                </tbody>

            </table>

        </div>

    </body>
</html>
