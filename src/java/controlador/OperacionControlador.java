package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 *
 * @author Gustavo
 */
@WebServlet(name = "OperacionControlador", urlPatterns = {"/OperacionControlador"})
public class OperacionControlador extends HttpServlet {
	private static int intentosPin = 2;
	private static int intentosPalabra = 2;
	private String palabraSecreta = "";


	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {

		String accion;
		RequestDispatcher dispatcher = null;
		accion = request.getParameter("accion");

		if (accion.equals("cambiarPin")) {
			dispatcher = request.getRequestDispatcher("Operacion/cambioPin.jsp");
			dispatcher.forward(request, response);

		} else if (accion.equals("actualizarPin")) {
			String numeroCuenta = request.getParameter("numeroCuenta");
			Cuenta cuenta = new CuentaCRUD().consultarCuenta(numeroCuenta);
			String pinActual = request.getParameter("pinActual");
			String nuevoPin = request.getParameter("nuevoPin");

			if (cuenta != null && cuenta.getEstatus().equals("activa")) {
				if (validarPin(cuenta, pinActual)) {
					cuenta.cambiarPin(nuevoPin);
					new CuentaCRUD().cambiarPin(cuenta);
					request.getSession().setAttribute("mensaje", "El pin de la cuenta ha sido actualizado");				
				} else {			
					request.getSession().setAttribute("mensaje", "El pin de la cuenta es incorrecto");
				}
			} else {
				request.getSession().setAttribute("mensaje", "El número de la cuenta es incorrecto o la cuenta se encuentra inactiva");
			}
			response.sendRedirect("MenuControlador");

		} else if (accion.equals("depositarColones")){
			dispatcher = request.getRequestDispatcher("Operacion/depositoColones.jsp");
			dispatcher.forward(request, response);
			
		} else if (accion.equals("realizarDepositoColones")){
			String numeroCuenta = request.getParameter("numeroCuenta");
			String montoDeposito = request.getParameter("montoDeposito");
			Cuenta cuenta = new CuentaCRUD().consultarCuenta(numeroCuenta);
			
			if (cuenta != null && cuenta.getEstatus().equals("activa")) {
				cuenta.depositarColones(Double.parseDouble(montoDeposito));
				new CuentaCRUD().actualizarSaldo(cuenta);
				Operacion operacion = cuenta.getOperaciones().get(cuenta.getOperaciones().size() - 1);
				new OperacionCRUD().registrarOperacion(operacion, numeroCuenta);
				request.getSession().setAttribute("mensaje", "El depósito ha sido realizado");

			} else {
				request.getSession().setAttribute("mensaje", "El número de la cuenta es incorrecto o la cuenta se encuentra inactiva");
			}
			response.sendRedirect("MenuControlador");
			
		} else if (accion.equals("depositarDolares")){
			dispatcher = request.getRequestDispatcher("Operacion/depositoDolares.jsp");
			dispatcher.forward(request, response);		
			
		} else if (accion.equals("realizarDepositoDolares")){
			String numeroCuenta = request.getParameter("numeroCuenta");
			String montoDeposito = request.getParameter("montoDeposito");
			Cuenta cuenta = new CuentaCRUD().consultarCuenta(numeroCuenta);
			
			if (cuenta != null && cuenta.getEstatus().equals("activa")) {
				cuenta.depositarDolares(Double.parseDouble(montoDeposito));
				new CuentaCRUD().actualizarSaldo(cuenta);
				Operacion operacion = cuenta.getOperaciones().get(cuenta.getOperaciones().size() - 1);
				new OperacionCRUD().registrarOperacion(operacion, numeroCuenta);
				request.getSession().setAttribute("mensaje", "El depósito en dólares ha sido realizado");

			} else {
				request.getSession().setAttribute("mensaje", "El número de la cuenta es incorrecto o la cuenta se encuentra inactiva");
			}
			response.sendRedirect("MenuControlador");
			
				
		} else if (accion.equals("retirarColones")) {

			dispatcher = request.getRequestDispatcher("Operacion/retiroColones.jsp");
			
				//request.getSession().setAttribute("mensaje", "");

			dispatcher.forward(request, response);

			
		} else if (accion.equals("realizarRetiroColones")){
			
			String numeroCuenta = request.getParameter("numeroCuenta");
			String pin = request.getParameter("pin");
			String montoRetiro = request.getParameter("montoRetiro");
			
			Cuenta cuenta = new CuentaCRUD().consultarCuenta(numeroCuenta);
			Cliente cliente = new ClienteCRUD().consultarPropietarioCuenta(numeroCuenta);
			
			if (cuenta != null && cuenta.getEstatus().equals("activa")){
				if (validarPin(cuenta, pin)) {		
					palabraSecreta = PalabraSecreta.generarPalabraSecreta();
					Sms.enviarSms("Su palabra secreta es: " + palabraSecreta, cliente.getNumeroTelefono());
					request.getSession().setAttribute("cuenta", cuenta.getNumeroCuenta());
					request.getSession().setAttribute("cliente", cliente.getIdentificacion());
					request.getSession().setAttribute("monto", montoRetiro);
					request.getSession().setAttribute("mensaje", "Estimado usuario se ha enviado una palabra por mensaje de texto, por favor revise sus mensajes y proceda a digitar la palabra enviada");
					response.sendRedirect("MenuControlador?accion=retirarColones");
				} else {
					request.getSession().setAttribute("mensaje", "El pin de la cuenta es incorrecto");
					response.sendRedirect("MenuControlador");
				}
			} else {
				request.getSession().setAttribute("mensaje", "El número de la cuenta es incorrecto o la cuenta se encuentra inactiva");
				response.sendRedirect("MenuControlador");
			}

		} else if (accion.equals("verificarPalabraSecreta")){
			String palabraSecretaDigitada = request.getParameter("palabraSecreta");
			Cuenta cuenta = new CuentaCRUD().consultarCuenta(request.getParameter("cuenta"));
			Cliente cliente = new ClienteCRUD().consultarCliente(request.getParameter("cliente"));
			String montoRetiro = request.getParameter("monto");

			if (validarPalabraSecreta(palabraSecreta, palabraSecretaDigitada, cuenta)) {
				try {
					cuenta.retirarColones(Double.parseDouble(montoRetiro));
					new CuentaCRUD().actualizarSaldo(cuenta);
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



		}else if (accion.equals("consultarTipoCambioCompra")){
			dispatcher = request.getRequestDispatcher("Operacion/tipoCambioCompra.jsp");
			TipoCambio tc = new TipoCambio();
			request.setAttribute("tipoCambioCompra", tc.getCompra());
			dispatcher.forward(request, response);
			
		}else if (accion.equals("consultarTipoCambioVenta")){
			dispatcher = request.getRequestDispatcher("Operacion/tipoCambioVenta.jsp");
			TipoCambio tc = new TipoCambio();
			request.setAttribute("tipoCambioVenta", tc.getVenta());
			dispatcher.forward(request, response);
			
		} else if (accion.equals("consultarSaldoActual")){
			dispatcher = request.getRequestDispatcher("Operacion/consultaSaldoActual.jsp");
			dispatcher.forward(request, response);
			
		} else if (accion.equals("verificarConsultaSaldoActual")){
			String numeroCuenta = request.getParameter("numeroCuenta");
			String pin = request.getParameter("pin");
			Cuenta cuenta = new CuentaCRUD().consultarCuenta(numeroCuenta);
			
			if (cuenta != null && cuenta.getEstatus().equals("activa")){
				if (validarPin(cuenta, pin)) {			
					request.getSession().setAttribute("mensaje", "Estimado usuario el saldo actual de su cuenta es " + cuenta.consultarSaldoActual() + " colones");				
				} else {			
					request.getSession().setAttribute("mensaje", "El pin de la cuenta es incorrecto");
				}
			} else {
				request.getSession().setAttribute("mensaje", "El número de la cuenta es incorrecto o la cuenta se encuentra inactiva");
			}
			response.sendRedirect("MenuControlador");

		} else if (accion.equals("consultarSaldoActualDolares")) {
			dispatcher = request.getRequestDispatcher("Operacion/consultaSaldoActualDolares.jsp");
			dispatcher.forward(request, response);

		} else if (accion.equals("verificarConsultaSaldoActualDolares")) {
			String numeroCuenta = request.getParameter("numeroCuenta");
			String pin = request.getParameter("pin");
			Cuenta cuenta = new CuentaCRUD().consultarCuenta(numeroCuenta);

			if (cuenta != null && cuenta.getEstatus().equals("activa")) {
				if (validarPin(cuenta, pin)) {
					TipoCambio tc = new TipoCambio();
					request.getSession().setAttribute("mensaje", "Estimado usuario el saldo actual de su cuenta es " + String.format("%,.2f", tc.convertirADolares(cuenta.consultarSaldoActual())) + " dólares"
									+ "<br></br>Para esta conversión se utilizó el tipo de cambio del dólar, precio de compra."
									+ "<br></br>[Según el BCCR, el tipo de cambio de compra del dólar de hoy es: " + tc.getCompra());
				} else {
					request.getSession().setAttribute("mensaje", "El pin de la cuenta es incorrecto");
				}
			} else {
				request.getSession().setAttribute("mensaje", "El número de la cuenta es incorrecto o la cuenta se encuentra inactiva");
			}
			response.sendRedirect("MenuControlador");
			
		} else if (accion.equals("consultarEstadoCuenta")){
			dispatcher = request.getRequestDispatcher("Operacion/estadoCuenta.jsp");
			dispatcher.forward(request, response);
			
		} else if (accion.equals("verificarConsultaEstadoCuenta")){
			String numeroCuenta = request.getParameter("numeroCuenta");
			String pin = request.getParameter("pin");
			Cuenta cuenta = new CuentaCRUD().consultarCuenta(numeroCuenta);
			
			if (cuenta != null && cuenta.getEstatus().equals("activa")) {
				if (validarPin(cuenta, pin)) {
					request.getSession().setAttribute("mensaje", cambiarSaltosLinea(cuenta.consultarEstadoCuenta()));
				} else {
					request.getSession().setAttribute("mensaje", "El pin de la cuenta es incorrecto");
				}
			} else {
				request.getSession().setAttribute("mensaje", "El número de la cuenta es incorrecto o la cuenta se encuentra inactiva");
			}
			response.sendRedirect("MenuControlador");
			
		} else if (accion.equals("consultarEstatus")){
			dispatcher = request.getRequestDispatcher("Operacion/consultaEstatus.jsp");
			dispatcher.forward(request, response);

		} else if (accion.equals("verificarConsultaEstatus")) {
			String numeroCuenta = request.getParameter("numeroCuenta");
			Cuenta cuenta = new CuentaCRUD().consultarCuenta(numeroCuenta);
			
			if (cuenta != null) {
				request.getSession().setAttribute("mensaje", "La cuenta número " + cuenta.getNumeroCuenta() + " tiene estatus de  " + cuenta.getEstatus());
			}else {
				request.getSession().setAttribute("mensaje", "El número de la cuenta es incorrecto");
			}
			response.sendRedirect("MenuControlador");
		}
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

	
	private boolean validarPin(Cuenta cuenta, String pinActual) {
		if (cuenta.getPin().equals(pinActual)) {
			this.intentosPin = 2;
			return true;
		} else {
			this.intentosPin--;
			if (this.intentosPin == 0) {
				cuenta.inactivarCuenta();
				new CuentaCRUD().cambiarEstatus(cuenta);
				this.intentosPin = 2;
			}
			return false;
		}
	}
	
	private boolean validarPalabraSecreta(String pPalabraEnviada, String pPalabraDigitada, Cuenta cuenta){
		if (pPalabraEnviada.equals(pPalabraDigitada)) {
			this.intentosPalabra = 2;
			return true;
		} else {
			this.intentosPalabra--;
			if (this.intentosPalabra == 0) {
				cuenta.inactivarCuenta();
				new CuentaCRUD().cambiarEstatus(cuenta);
				this.intentosPalabra = 2;
			}
			return false;
		}
	}

	private String cambiarSaltosLinea(String pTexto) {
		String textoCambiado = pTexto.replace("\n", "<br></br>");
		return textoCambiado;
	}
}
