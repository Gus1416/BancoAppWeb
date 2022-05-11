<%-- 
    Document   : retiroColones
    Created on : 9 may. 2022, 14:00:31
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
            
            <h2>Retiro en colones</h2>

            <form class="reg-form" action="OperacionControlador?accion=realizarRetiroColones" method="POST" autocomplete="off">

                <div class="form-input-container">
                    <label class="form-label">Número de cuenta</label>
                    <input class="form-input"  id="numeroCuenta" name="numeroCuenta" type="text" required/>
                </div>

                <div class="form-input-container">
                    <label class="form-label">Pin </label>
                    <input class="form-input"  id="pin" name="pin" type="password" 
                           pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=]).{6,6}$" 
                           title="El pin debe tener máximo 6 caracteres y debe contener al menos un letra mayúscula, un número y un caracter especial: @#$%^&-+=" required/>
                </div>
                
                <div class="form-input-container">
                    <label class="form-label">Monto de depósito</label>
                    <input class="form-input"  id="montoRetiro" name="montoRetiro" type="number" required/>
                </div>

                <button id="realizarDepositoColones" name="enviarPalabraSecreta" type="submit">Enviar palabra secreta</button>

            </form>
            
            <h3><br><br><br><%out.print(session.getAttribute("mensaje"));%></h3>
            
            <form class="reg-form" action="OperacionControlador?accion=verificarPalabraSecreta&cuenta=<%out.print(session.getAttribute("cuenta"));%>&cliente=<%out.print(session.getAttribute("cliente"));%>&monto=<%out.print(session.getAttribute("monto"));%>" method="POST" autocomplete="off">

                <div class="form-input-container">
                    <label class="form-label">Palabra secreta</label>
                    <input class="form-input"  id="palabraSecreta" name="palabraSecreta" type="text" required/>
                </div>

                <button id="verificarPalabraSecreta" name="verificarPalabraSecreta" type="submit">Verificar palabra secreta</button>

            </form>

            

        </div>
    </body>
</html>
