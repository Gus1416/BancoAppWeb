<%-- 
    Document   : consultaBitacoras
    Created on : 3 jun. 2022, 08:47:09
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
                    <a href="MenuControlador?accion=cosultarBitacora">Consultar bitácora de actividad</a>
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

            <h2>Consultar bitácora de actividad</h2>

            <form class="reg-form" action="UsuarioControlador?accion=consultarBitacora" method="POST" autocomplete="off">

                <div class="form-radio-container">
                    
                     <label class="form-label">Seleccione el formato con el que desea visualizar la bitácora</label>
                    <div>
                        <input type="radio" id="formatoCSV" name="formato" value="csv">
                        <label for="formatoCSV">CSV</label>
                    </div>
                    
                    <div>
                        <input type="radio" id="formatoXML" name="formato" value="xml">
                        <label for="formatoXML">XML</label>
                    </div>
                    
                    <div>
                        <input type="radio" id="formatoTramaPlana" name="formato" value="tramaPlana">
                        <label for="formatoTramaPlana">Trama Plana</label>
                    </div>
                    
                </div>
                
                
                <div class="form-radio-container">
                    
                     <label class="form-label">Seleccione un filtro de búsqueda para la bitácora</label>
                     
                    <div>
                        <input type="radio" id="registrosHoy" name="filtro" value="hoy">
                        <label for="registrosHoy">Registros de hoy</label>
                    </div>
                    
                    <div>
                        <input type="radio" id="registrosCLI" name="filtro" value="cli">
                        <label for="registrosCLI">Registros en aplicación CLI</label>
                    </div>
                    
                    <div>
                        <input type="radio" id="registrosGUI" name="filtro" value="gui">
                        <label for="registrosGUI">Registros en aplicación GUI</label>
                    </div>
                     
                    <div>
                        <input type="radio" id="registrosWEB" name="filtro" value="web">
                        <label for="registrosWEB">Registros en aplicación WEB</label>
                    </div>
                     
                    <div>
                        <input type="radio" id="registrosTodos" name="filtro" value="todos">
                        <label for="registrosTodos">Todos los registros</label>
                    </div>
                    
                </div>
                
     
                <button id="consultar" name="consultar" type="submit">Consultar</button>

            </form>

        </div>
    </body>
</html>
