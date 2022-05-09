<%-- 
    Document   : consultaEstatus
    Created on : 8 may. 2022, 23:45:14
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
            </ul>
        </nav>
        
        <div class="contenedor">
            
            <h2>Consultar estatus de cuenta</h2>

            <form class="reg-form" action="OperacionControlador?accion=verificarConsultaEstatus" method="POST" autocomplete="off">

                <div class="form-input-container">
                    <label class="form-label">Número de cuenta</label>
                    <input class="form-input"  id="numeroCuenta" name="numeroCuenta" type="text" required/>
                </div>

                <button id="verificarConsultaEstatus" name="verificarConsultaEstatus" type="submit">Consultar</button>

            </form>


        </div>
    </body>
</html>
