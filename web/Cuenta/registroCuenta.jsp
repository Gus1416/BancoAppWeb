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
<!--        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">-->
    </head>
    <body>
        <nav class="barnav">
            <h1 id="titulo">iBanco</h1>
            <ul class="nav-ul">
                <li class="nav-li">
                    <a class="nav-a" href="MenuControlador?accion=registrarCliente">Registrar cliente</a>
                </li>
                <li class="nav-li">
                    <a class="nav-a" href="MenuControlador?accion=registrarCuenta">Crear cuenta</a>
                </li>
                <li class="nav-li">
                    <a class="nav-a" href="">Listar clientes</a>
                </li>
                <li class="nav-li">
                    <a class="nav-a" href="">Listar cuentas</a>
                </li>
            </ul>
        </nav>
        
        <div class="contenedor">
            
            <h2>Registrar cuenta</h2>

            <form class="reg-form" action="CuentaControlador?accion=guardar" method="POST" autocomplete="off">

                <div class="form-input-container">
                    <label class="form-label">Identificación del cliente</label>
                    <input class="form-input"  id="identificacion" name="identificacion" type="text" required/>
                </div>

                <div class="form-input-container">
                    <label class="form-label">Pin</label>
                    <input class="form-input"  id="pin" name="pin" type="password" 
                           pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=]).{6,6}$" 
                           title="El pin debe tener máximo 6 caracteres y debe contener al menos un letra mayúscula, un número y un caracter especial: @#$%^&-+=" required/>
                </div>

                <div class="form-input-container">
                    <label class="form-label">Monto de depósito incial</label>
                    <input class="form-input"  id="montoInicial" name="montoInicial" type="number" required/>
                </div>

                <button id="guardar" name="guardar" type="submit">Registrar</button>

            </form>

        </div>
    </body>
</html>

