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
import logicadenegocios.Cuenta;

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
		
		if (accion.equals("cambiarPin")){
			dispatcher = request.getRequestDispatcher("Operacion/cambioPin.jsp");		
			dispatcher.forward(request, response);
			
		}else if (accion.equals("actualizarPin")){
			String numeroCuenta = request.getParameter("numeroCuenta");
			Cuenta cuenta = new CuentaCRUD().consultarCuenta(numeroCuenta);
			String pinActual = request.getParameter("pinActual");
			String nuevoPin = request.getParameter("nuevoPin");
			
			if (cuenta != null && cuenta.getEstatus().equals("activa")) {
				
				if (cuenta.getPin().equals(pinActual)) {		
					cuenta.cambiarPin(nuevoPin);
					new CuentaCRUD().cambiarPin(cuenta);
					request.getSession().setAttribute("mensaje", "El pin de la cuenta ha sido actualizado");
					this.intentosPin = 2;
					
				} else {
					this.intentosPin--;
					if (this.intentosPin == 0) {
						cuenta.inactivarCuenta();
						new CuentaCRUD().cambiarEstatus(cuenta);
						request.getSession().setAttribute("mensaje", "La cuenta ha sido inactivada");
						
					} else {
						request.getSession().setAttribute("mensaje", "El pin de la cuenta no coincide");
					}
				}
			} else {
				request.getSession().setAttribute("mensaje", "El número de la cuenta es incorrecto o la cuenta se encuentra inactiva");
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

}
