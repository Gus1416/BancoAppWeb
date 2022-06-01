<%-- 
    Document   : listaClientes
    Created on : 7 may. 2022, 13:43:37
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
                <li class="nav-li">
                    <a href="MenuControlador?accion=depositarColones">Depositar en colones</a>
                </li>
                <li class="nav-li">
                    <a href="MenuControlador?accion=depositarDolares">Depositar en dólares</a>
                </li>
                <li class="nav-li">
                    <a href="MenuControlador?accion=retirarColones">Retirar en colones</a>
                </li>
                <li class="nav-li">
                    <a href="MenuControlador?accion=retirarDolares">Retirar en dólares</a>
                </li>
                <li class="nav-li">
                    <a href="MenuControlador?accion=transferir">Transferir a otra cuenta</a>
                </li>
                <li class="nav-li">
                    <a href="MenuControlador?accion=consultarTipoCambioCompra">Consultar tipo de cambio de compra</a>
                </li>
                <li class="nav-li">
                    <a href="MenuControlador?accion=consultarTipoCambioVenta">Consultar tipo de cambio de venta</a>
                </li>
                <li class="nav-li">
                    <a href="MenuControlador?accion=consultarSaldoActual">Consultar saldo actual</a>
                </li>
                <li class="nav-li">
                    <a href="MenuControlador?accion=consultarSaldoActualDolares">Consultar saldo actual en dólares</a>
                </li>
                <li class="nav-li">
                    <a href="MenuControlador?accion=consultarEstadoCuenta">Consultar estado de cuenta</a>
                </li>
                <li class="nav-li">
                    <a href="MenuControlador?accion=consultarEstadoCuentaDolares">Consultar estado de cuenta en dólares</a>
                </li>
                <li class="nav-li">
                    <a href="MenuControlador?accion=consultarEstatus">Consultar estatus de cuenta</a>
                </li>
                <li class="nav-li">
                    <a href="MenuControlador?accion=consultarTotalComisiones">Consultar ganancias por cobro total de comisiones</a>
                </li>
                <li class="nav-li">
                    <a href="MenuControlador?accion=consultarComisionesCuenta">Consultar ganancias por cobro de comisiones a una cuenta</a>
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
                        <th></th>
                    </tr>        
                </thead>

                <tbody>

                    <c:forEach var="cliente" items="${clientes}">

                        <tr>
                            <td><c:out value="${cliente.primerApellido}"/></td>
                            <td><c:out value="${cliente.segundoApellido}"/></td>
                            <td><c:out value="${cliente.nombre}"/></td>
                            <td><c:out value="${cliente.identificacion}"/></td>   
                            <td class="td_vermas"><a href="ClienteControlador?accion=verDetallesCliente&cliente=${cliente.identificacion}" class="btn-vermas">Ver más</a></td>
                        </tr>

                    </c:forEach>

                </tbody>

            </table>

        </div>

    </body>
</html>
