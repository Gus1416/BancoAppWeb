package controlador;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Gustavo
 */
@WebServlet(name = "MenuControlador", urlPatterns = {"/MenuControlador"})
public class MenuControlador extends HttpServlet {


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

		if (accion == null || accion.isEmpty()) {
			if (request.getSession().getAttribute("mensaje") == null) {
				request.getSession().setAttribute("mensaje", "");
			}
			dispatcher = request.getRequestDispatcher("Menu/index.jsp");
			dispatcher.forward(request, response);

		} else if (accion.equals("registrarCliente") || accion.equals("listarClientes")){
			dispatcher = request.getRequestDispatcher("/ClienteControlador");
			dispatcher.forward(request, response);
			
		} else if (accion.equals("registrarCuenta") || accion.equals("listarCuentas")){
			dispatcher = request.getRequestDispatcher("/CuentaControlador");
			dispatcher.forward(request, response);
			
		} else if (accion.equals("cambiarPin") || accion.equals("depositarColones") || accion.equals("depositarDolares")){
			dispatcher = request.getRequestDispatcher("/OperacionControlador");
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

}
