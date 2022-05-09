<%-- 
    Document   : tipoCambioCompra
    Created on : 8 may. 2022, 21:49:50
    Author     : Gustavo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
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
            <h2>Tipo de cambio actual para la compra de dólares</h2>
            <br /> <br />
            
            <h3>Compra: CRC <%out.print(request.getAttribute("tipoCambioCompra"));%></h3>
            
        </div>

    </body>
</html>
