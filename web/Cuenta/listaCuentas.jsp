<%-- 
    Document   : listarCuentas
    Created on : 7 may. 2022, 20:13:19
    Author     : Gustavo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
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
            <h2>Cuentas registradas</h2>
            <br /> <br />

            <table class="listas_registros">

                <thead>           
                    <tr>
                        <th>Número de cuenta</th>
                        <th>Estatus</th>
                        <th>Saldo</th>
                        <th>Identificación del dueño</th>
                        <th>Nombre del dueño</th>
                        <th></th>
                    </tr>        
                </thead>

                <tbody>

                    <c:forEach var="cuenta" items="${cuentas}">

                        <tr>
                            <td><c:out value="${cuenta.numeroCuenta}"/></td>
                            <td><c:out value="${cuenta.estatus}"/></td>
                            <td><c:out value="${cuenta.saldo}"/></td>

                            <c:forEach var="cliente" items="${clientes}">
                                <c:forEach var="cuentaCliente" items="${cliente.cuentas}">
                                    <c:if test="${cuentaCliente.numeroCuenta eq cuenta.numeroCuenta}">
                                        <td><c:out value="${cliente.identificacion}"/></td>   
                                        <td><c:out value="${cliente.primerApellido} ${cliente.segundoApellido} ${cliente.nombre}"/></td>   
                                    </c:if>
                                </c:forEach>
                            </c:forEach>
                                        
                            <td class="td_vermas"><a href="CuentaControlador?accion=verDetallesCuenta&numeroCuenta=${cuenta.numeroCuenta}" class="btn-vermas">Ver más</a></td>
                        </tr>

                    </c:forEach>

                </tbody>

            </table>



        </div>

    </body>
</html>
