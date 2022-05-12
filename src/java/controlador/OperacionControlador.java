package controlador;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logicadeaccesodedatos.ClienteCRUD;
import logicadeaccesodedatos.CuentaCRUD;
import logicadeaccesodedatos.OperacionCRUD;
import logicadenegocios.Cliente;
import logicadenegocios.Cuenta;
import logicadenegocios.Operacion;
import logicadenegocios.PalabraSecreta;
import logicadevalidacion.FondosInsuficientesExcepcion;
import serviciosexternos.Sms;
import serviciosexternos.TipoCambio;


@WebServlet(name = "OperacionControlador", urlPatterns = {"/OperacionControlador"})
public class OperacionControlador extends HttpServlet {
	private int intentos = 2;
	private static String palabraSecreta = "";
	private RequestDispatcher dispatcher = null;
	HttpServletRequest request;
	
	Cuenta cuenta = new Cuenta();
	CuentaCRUD cuentaCrud = new CuentaCRUD();


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.request = request;
		String accion = request.getParameter("accion");
		
		if (accion.equals("cambiarPin")) {
			enviarSolicitud(request, response, "Operacion/cambioPin.jsp");
			
			
		} else if (accion.equals("actualizarPin")) {
			String numeroCuenta = request.getParameter("numeroCuenta");
			cuenta = cuentaCrud.consultarCuenta(numeroCuenta);
			String pinActual = request.getParameter("pinActual");
			String nuevoPin = request.getParameter("nuevoPin");
			
			if (sePermiteOperacion(cuenta) && validarPin(cuenta.getPin(), pinActual)) {
				cuenta.cambiarPin(nuevoPin);
				cuentaCrud.cambiarPin(cuenta);
				request.getSession().setAttribute("mensaje", "El pin de la cuenta ha sido actualizado");
			}
			response.sendRedirect("MenuControlador");
		
		} else if (accion.equals("depositarColones")) {
			enviarSolicitud(request, response, "Operacion/depositoColones.jsp");

			
		} else if (accion.equals("realizarDepositoColones")){
			String numeroCuenta = request.getParameter("numeroCuenta");
			String montoDeposito = request.getParameter("montoDeposito");
			cuenta = cuentaCrud.consultarCuenta(numeroCuenta);
			
			if (sePermiteOperacion(cuenta)) {
				cuenta.depositarColones(Double.parseDouble(montoDeposito));
				cuentaCrud.actualizarSaldo(cuenta);
				Operacion operacion = cuenta.getOperaciones().get(cuenta.getOperaciones().size() - 1);
				new OperacionCRUD().registrarOperacion(operacion, numeroCuenta);
				request.getSession().setAttribute("mensaje", "El depósito ha sido realizado");
			}
			response.sendRedirect("MenuControlador");
						
		} else if (accion.equals("depositarDolares")) {
			enviarSolicitud(request, response, "Operacion/depositoDolares.jsp");
			
			
		} else if (accion.equals("realizarDepositoDolares")){
			String numeroCuenta = request.getParameter("numeroCuenta");
			String montoDeposito = request.getParameter("montoDeposito");
			cuenta = cuentaCrud.consultarCuenta(numeroCuenta);
			
			if (sePermiteOperacion(cuenta)) {
				cuenta.depositarDolares(Double.parseDouble(montoDeposito));
				cuentaCrud.actualizarSaldo(cuenta);
				Operacion operacion = cuenta.getOperaciones().get(cuenta.getOperaciones().size() - 1);
				new OperacionCRUD().registrarOperacion(operacion, numeroCuenta);
				request.getSession().setAttribute("mensaje", "El depósito en dólares ha sido realizado");
			}
			response.sendRedirect("MenuControlador");
				
			
		} else if (accion.equals("retirarColones")) {	
			enviarSolicitud(request, response, "Operacion/retiroColones.jsp");
			
			
		} else if (accion.equals("realizarRetiroColones")){	
			String numeroCuenta = request.getParameter("numeroCuenta");
			String pin = request.getParameter("pin");
			String montoRetiro = request.getParameter("montoRetiro");
			
			cuenta = cuentaCrud.consultarCuenta(numeroCuenta);
			Cliente cliente = new ClienteCRUD().consultarPropietarioCuenta(numeroCuenta);
			
			if (sePermiteOperacion(cuenta) && validarPin(cuenta.getPin(), pin)) {
				palabraSecreta = PalabraSecreta.generarPalabraSecreta();
				Sms.enviarSms("Su palabra secreta es: " + palabraSecreta, cliente.getNumeroTelefono());
				request.getSession().setAttribute("cuenta", cuenta.getNumeroCuenta());
				request.getSession().setAttribute("cliente", cliente.getIdentificacion());
				request.getSession().setAttribute("monto", montoRetiro);
				request.getSession().setAttribute("mensajeTexto", "Estimado usuario se ha enviado una palabra por mensaje de texto, por favor revise sus mensajes y proceda a digitar la palabra enviada");
			}
			response.sendRedirect("MenuControlador?accion=retirarColones");

			
		} else if (accion.equals("verificarPalabraSecreta")){
			String palabraSecretaDigitada = request.getParameter("palabraSecreta");
			cuenta = cuentaCrud.consultarCuenta(request.getParameter("cuenta"));
			Cliente cliente = new ClienteCRUD().consultarCliente(request.getParameter("cliente"));
			String montoRetiro = request.getParameter("monto");

			if (validarPalabraSecreta(palabraSecreta, palabraSecretaDigitada, cuenta)) {
				try {
					cuenta.retirarColones(Double.parseDouble(montoRetiro));
					cuentaCrud.actualizarSaldo(cuenta);
					Operacion operacion = cuenta.getOperaciones().get(cuenta.getOperaciones().size() - 1);
					new OperacionCRUD().registrarOperacion(operacion, cuenta.getNumeroCuenta());
					request.getSession().setAttribute("mensaje", "Estimado usuario, el monto de este retiro es " + montoRetiro + " colones."
									+ "<br></br> [El monto cobrado por concepto de comisión fue de " + operacion.getMontoComision() + " colones"
									+ ", que fueron rebajados automáticamente de su saldo actual]");
					response.sendRedirect("MenuControlador");

				} catch (FondosInsuficientesExcepcion ex) {
					request.getSession().setAttribute("mensaje", "No hay suficientes fondos para realizar la operación");
					response.sendRedirect("MenuControlador");
				}

			} else {
				request.getSession().setAttribute("mensaje", "La palabra secreta es incorrecta");
				response.sendRedirect("MenuControlador");
			}
			
	///////////////////////
	} else if (accion.equals("retirarDolares")) {	
			enviarSolicitud(request, response, "Operacion/retiroDolares.jsp");
			

		} else if (accion.equals("realizarRetiroDolares")){	
			String numeroCuenta = request.getParameter("numeroCuenta");
			String pin = request.getParameter("pin");
			String montoRetiro = request.getParameter("montoRetiro");
			
			cuenta = cuentaCrud.consultarCuenta(numeroCuenta);
			Cliente cliente = new ClienteCRUD().consultarPropietarioCuenta(numeroCuenta);
			
			if (sePermiteOperacion(cuenta) && validarPin(cuenta.getPin(), pin)) {
				palabraSecreta = PalabraSecreta.generarPalabraSecreta();
				Sms.enviarSms("Su palabra secreta es: " + palabraSecreta, cliente.getNumeroTelefono());
				request.getSession().setAttribute("cuenta", cuenta.getNumeroCuenta());
				request.getSession().setAttribute("cliente", cliente.getIdentificacion());
				request.getSession().setAttribute("monto", montoRetiro);
				request.setAttribute("mensaje", "Estimado usuario se ha enviado una palabra por mensaje de texto, por favor revise sus mensajes y proceda a digitar la palabra enviada");
			}
			response.sendRedirect("MenuControlador?accion=retirarDolares");

			
		} else if (accion.equals("verificarPalabraSecretaDolares")){
			String palabraSecretaDigitada = request.getParameter("palabraSecreta");
			cuenta = cuentaCrud.consultarCuenta(request.getParameter("cuenta"));
			Cliente cliente = new ClienteCRUD().consultarCliente(request.getParameter("cliente"));
			String montoRetiro = request.getParameter("monto");
			TipoCambio tc = new TipoCambio();

			if (validarPalabraSecreta(palabraSecreta, palabraSecretaDigitada, cuenta)) {
				try {
					cuenta.retirarDolares(Double.parseDouble(montoRetiro));
					cuentaCrud.actualizarSaldo(cuenta);
					Operacion operacion = cuenta.getOperaciones().get(cuenta.getOperaciones().size() - 1);
					System.out.println("fasdfasdfasfdasfasdfasfda" + operacion.getMoneda());
					new OperacionCRUD().registrarOperacion(operacion, cuenta.getNumeroCuenta());
					request.getSession().setAttribute("mensaje", "Estimado usuario, el monto de este retiro es " + montoRetiro + " dólares."
									+ "<br></br>  [Según el BCCR, el tipo de cambio de venta del dólar de hoy es: " + tc.getVenta() + "]"
									+ "<br></br> [El monto equivalente de su retiro es " + tc.convertirAColones(Double.parseDouble(montoRetiro)) + "colones]"
									+ "<br></br> [El monto cobrado por concepto de comisión fue de " + operacion.getMontoComision() + " colones"
									+ ", que fueron rebajados automáticamente de su saldo actual]");
					response.sendRedirect("MenuControlador");

				} catch (FondosInsuficientesExcepcion ex) {
					request.getSession().setAttribute("mensaje", "No hay suficientes fondos para realizar la operación");
					response.sendRedirect("MenuControlador");
				}

			} else {
				request.getSession().setAttribute("mensaje", "La palabra secreta es incorrecta");
				response.sendRedirect("MenuControlador");
			}
	//////////////////////

			
		}else if (accion.equals("consultarTipoCambioCompra")){
			TipoCambio tc = new TipoCambio();
			request.setAttribute("tipoCambioCompra", tc.getCompra());	
			enviarSolicitud(request, response, "Operacion/tipoCambioCompra.jsp");
			
			
		}else if (accion.equals("consultarTipoCambioVenta")){
			TipoCambio tc = new TipoCambio();
			request.setAttribute("tipoCambioVenta", tc.getVenta());
			enviarSolicitud(request, response, "Operacion/tipoCambioVenta.jsp");
			
			
		} else if (accion.equals("consultarSaldoActual")){
			enviarSolicitud(request, response, "Operacion/consultaSaldoActual.jsp");
			
			
		} else if (accion.equals("verificarConsultaSaldoActual")){
			String numeroCuenta = request.getParameter("numeroCuenta");
			String pin = request.getParameter("pin");
			cuenta = cuentaCrud.consultarCuenta(numeroCuenta);

			if (sePermiteOperacion(cuenta) && validarPin(cuenta.getPin(), pin)) {
				request.getSession().setAttribute("mensaje", "Estimado usuario el saldo actual de su cuenta es " + cuenta.consultarSaldoActual() + " colones");
			}
			response.sendRedirect("MenuControlador");


		} else if (accion.equals("consultarSaldoActualDolares")) {
			enviarSolicitud(request, response, "Operacion/consultaSaldoActualDolares.jsp");
					
			
		} else if (accion.equals("verificarConsultaSaldoActualDolares")) {
			String numeroCuenta = request.getParameter("numeroCuenta");
			String pin = request.getParameter("pin");
			cuenta = cuentaCrud.consultarCuenta(numeroCuenta);

			if (sePermiteOperacion(cuenta) && validarPin(cuenta.getPin(), pin)) {
					TipoCambio tc = new TipoCambio();
					request.getSession().setAttribute("mensaje", "Estimado usuario el saldo actual de su cuenta es " + String.format("%,.2f", tc.convertirADolares(cuenta.consultarSaldoActual())) + " dólares"
									+ "<br></br>Para esta conversión se utilizó el tipo de cambio del dólar, precio de compra."
									+ "<br></br>[Según el BCCR, el tipo de cambio de compra del dólar de hoy es: " + tc.getCompra());
			}
			response.sendRedirect("MenuControlador");
			
			
		} else if (accion.equals("consultarEstadoCuenta")){
			enviarSolicitud(request, response, "Operacion/estadoCuenta.jsp");
					
			
		} else if (accion.equals("verificarConsultaEstadoCuenta")){
			String numeroCuenta = request.getParameter("numeroCuenta");
			String pin = request.getParameter("pin");
			cuenta = cuentaCrud.consultarCuenta(numeroCuenta);

			if (sePermiteOperacion(cuenta) && validarPin(cuenta.getPin(), pin)) {
				request.getSession().setAttribute("mensaje", cambiarSaltosLinea(cuenta.consultarEstadoCuenta()));
			}
			response.sendRedirect("MenuControlador");
					
			
		} else if (accion.equals("consultarEstatus")){
			enviarSolicitud(request, response, "Operacion/consultaEstatus.jsp");
			
			
		} else if (accion.equals("verificarConsultaEstatus")) {
			String numeroCuenta = request.getParameter("numeroCuenta");
			cuenta = cuentaCrud.consultarCuenta(numeroCuenta);
			
			if (esCuentaRegistrada(cuenta)) {
				request.getSession().setAttribute("mensaje", "La cuenta número " + cuenta.getNumeroCuenta() + " tiene estatus de  " + cuenta.getEstatus());
			}else {
				request.getSession().setAttribute("mensaje", "El número de la cuenta es incorrecto");
			}
			response.sendRedirect("MenuControlador");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	public String getServletInfo() {
		return "Short description";
	}
	
	
	private boolean sePermiteOperacion(Cuenta pCuenta){
		if (!esCuentaRegistrada(pCuenta)) {
			request.getSession().setAttribute("mensaje", "El número de cuenta no corresponde a ninguna cuenta registrada");
			return false;
		}
		if (!esCuentaActiva(pCuenta)) {
			request.getSession().setAttribute("mensaje", "La cuenta se encuenta inactiva");
			return false;
		}	
		return true;
	}
	
	private boolean esCuentaRegistrada(Cuenta pCuenta){
		return pCuenta != null;
	}
	
	private boolean esCuentaActiva(Cuenta pCuenta){
		return pCuenta.getEstatus().equals("activa");
	}
	
	private void enviarSolicitud (HttpServletRequest request, HttpServletResponse response, String pRuta) throws ServletException, IOException {
		dispatcher = request.getRequestDispatcher(pRuta);
		dispatcher.forward(request, response);
	}
	
	private boolean validarPin(String pinCuenta, String pinAComparar) {
		if (pinCuenta.equals(pinAComparar)) {
			gestionarIntentosFallidos(true);
			return true;
		} else {	
			request.getSession().setAttribute("mensaje", "El pin ingresado es incorrecto");
			gestionarIntentosFallidos(false);
			return false;
		}
	}

	private boolean validarPalabraSecreta(String pPalabraEnviada, String pPalabraDigitada, Cuenta cuenta){
		if (pPalabraEnviada.equals(pPalabraDigitada)) {
			this.intentos= 2;
			return true;
		} else {
			this.intentos--;
			if (this.intentos == 0) {
				cuenta.inactivarCuenta();
				cuentaCrud.cambiarEstatus(cuenta);
				this.intentos = 2;
			}
			return false;
		}
	}
	
	private void gestionarIntentosFallidos(boolean pIntentoCorrecto){
		if (!pIntentoCorrecto){
			this.intentos--;
			if (this.intentos == 0){
				inactivarCuentaPorIntentosFallidos();
			}
		} else {
			this.intentos = 2;
		}
	}
	
	private void inactivarCuentaPorIntentosFallidos() {
		cuenta.inactivarCuenta();
		cuentaCrud.cambiarEstatus(cuenta);
	}

	private String cambiarSaltosLinea(String pTexto) {
		String textoCambiado = pTexto.replace("\n", "<br></br>");
		return textoCambiado;
	}
}