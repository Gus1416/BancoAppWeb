<%-- 
    Document   : registrar
    Created on : 7 may. 2022, 10:21:37
    Author     : Gustavo
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
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
            
            <h2>Registrar cuenta</h2>

            <form class="reg-form" action="CuentaControlador?accion=guardar" method="POST" autocomplete="off">

                <div class="form-input-container">
                    <label class="form-label">Identificación del cliente</label>
                    <input class="form-input"  id="identificacion" name="identificacion" type="text" pattern="([0-9]{9,9})" title="Indique la identificación en formato #0###0###" required/>
                </div>

                <div class="form-input-container">
                    <label class="form-label">Pin</label>
                    <input class="form-input"  id="pin" name="pin" type="password" 
                           pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=]).{6,6}$" 
                           title="El pin debe tener máximo 6 caracteres y debe contener al menos un letra mayúscula, un número y un caracter especial: @#$%^&-+=" required/>
                </div>

                <div class="form-input-container">
                    <label class="form-label">Monto de depósito incial</label>
                    <input class="form-input"  id="montoInicial" name="montoInicial" type="number" pattern="[0-9]" title="Ingrese un número sin decimales" required/>
                </div>

                <button id="guardar" name="guardar" type="submit">Registrar</button>

            </form>

        </div>
    </body>
</html>

