package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logicadeaccesodedatos.CuentaCRUD;
import logicadeaccesodedatos.OperacionCRUD;
import logicadenegocios.Cuenta;
import logicadenegocios.Operacion;
import serviciosexternos.TipoCambio;

/**
 *
 * @author Gustavo
 */
@WebServlet(name = "OperacionControlador", urlPatterns = {"/OperacionControlador"})
public class OperacionControlador extends HttpServlet {
	private static int intentosPin = 2;


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
				if (validarPin(cuenta, pinActual, nuevoPin, request)) {
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

		}else if (accion.equals("consultarTipoCambioCompra")){
			dispatcher = request.getRequestDispatcher("Operacion/tipoCambioCompra.jsp");
			TipoCambio tc = new TipoCambio();
			request.setAttribute("tipoCambioCompra", tc.getCompra());
			dispatcher.forward(request, response);
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

	
	private boolean validarPin(Cuenta cuenta, String pinActual, String nuevoPin, HttpServletRequest request) {
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
}
