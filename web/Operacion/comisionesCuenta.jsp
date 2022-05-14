<%-- 
    Document   : comisionesCuenta
    Created on : 13 may. 2022, 14:54:52
    Author     : Gustavo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
                    <a href="MenuControlador?accion=consultarTipoCambioCompra">Consultar tipo de cambio de compra</a>
                </li>
            </ul>
        </nav>

        <div class="contenedor">
            <h2>Ganancias por comisiones totalizado</h2>
            <br /> <br />

            <form class="reg-form" action="OperacionControlador?accion=verificarConsultaComisionesCuenta" method="POST" autocomplete="off">

                <div class="form-input-container">
                    <label class="form-label">Número de cuenta</label>
                    <input class="form-input"  id="numeroCuenta" name="numeroCuenta" type="text" required/>
                </div>

                <button id=verificarConsultaComisionesCuenta" name="verificarConsultaComisionesCuenta" type="submit">Consultar</button>

            </form>

            <h3>Total de comisiones: <%out.print(request.getAttribute("totalComisionesCuenta"));%> colones</h3>
            <h3>Comisiones por depósitos: <%out.print(request.getAttribute("comisionesDepositosCuenta"));%> colones</h3>
            <h3>Comisiones por retiros: <%out.print(request.getAttribute("comisionesRetirosCuenta"));%> colones</h3>

        </div>


</html>