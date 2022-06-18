<%-- 
    Document   : index
    Created on : 5 may. 2022, 20:26:17
    Author     : Gustavo
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>iBanco</title>
        <link rel="stylesheet" type="text/css" href="/BancoAppWeb/CSS/estilos.css"/>
    </head>
    <body>
        <nav class="barnav">
            <h1 id="titulo">iBanco</h1>
            <ul class="nav-ul">
                <li class="nav-li">
                    <a href="MenuControlador?accion=iniciarSesionUsuario">Consultar bitácora de actividad</a>
                </li>
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
            <h2>Seleccione la acción que desee realizar en el sistema</h2>
           
            <h3 id="mensaje-exito">Último mensaje del sistema:<br><br><br><%out.print(session.getAttribute("mensaje"));%></h3>
        </div>

    </body>
</html>
