package controlador;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logicadeaccesodedatos.ClienteCRUD;
import logicadeaccesodedatos.CuentaCRUD;
import logicadenegocios.Busqueda;
import logicadenegocios.Cliente;
import logicadenegocios.Cuenta;
import logicadenegocios.Persona;

/**
 *
 * @author Gustavo
 */
@WebServlet(name = "CuentaControlador", urlPatterns = {"/CuentaControlador"})
public class CuentaControlador extends HttpServlet {
	ArrayList<Cliente> clientes = new ClienteCRUD().consultarClientes();

	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String accion;
		RequestDispatcher dispatcher = null;
		accion = request.getParameter("accion");
		
		if (accion.equals("registrarCuenta")){
			dispatcher = request.getRequestDispatcher("Cuenta/registroCuenta.jsp");
			dispatcher.forward(request, response);
			
		} else if (accion.equals("guardar")){
			String identificacion = request.getParameter("identificacion");
			String pin = request.getParameter("pin");
			double montoInicial = Double.parseDouble(request.getParameter("montoInicial"));
			
			Cuenta cuenta = new Cuenta(pin, montoInicial);
			
			if (new CuentaCRUD().registrarCuenta(cuenta, identificacion)){
				String mensaje = "Cuenta registrada con éxito"; ////////////////////////////////////////////ARREGLAR
				request.getSession().setAttribute("mensaje", mensaje);

			} else {
				String mensaje = "No se pudo registrar la cuenta";////////////////////////////////////////ARREGLAR
				request.getSession().setAttribute("mensaje", mensaje);
			}
			
			response.sendRedirect("MenuControlador");
			
		} else if (accion.equals("listarCuentas")){
			dispatcher = request.getRequestDispatcher("Cuenta/listaCuentas.jsp");
			ArrayList<Cuenta> cuentas = new CuentaCRUD().consultarCuentas();
			request.setAttribute("cuentas", cuentas);
			ArrayList<Cliente> clientes = new ClienteCRUD().consultarClientes();
			request.setAttribute("clientes", clientes);
			dispatcher.forward(request, response);
			
		} else if (accion.equals("verDetallesCuenta")){
			dispatcher = request.getRequestDispatcher("Cuenta/detallesCuenta.jsp");
			String numeroCuenta = request.getParameter("numeroCuenta");
			Cuenta cuenta = new CuentaCRUD().consultarCuenta(numeroCuenta);
			String detallesCuenta = cuenta.toString();
			request.setAttribute("detallesCuenta", detallesCuenta);
			dispatcher.forward(request, response);
			
		} else if (accion.equals("cambiarPin")){
			dispatcher = request.getRequestDispatcher("Cuenta/cambioPin.jsp");
			request.getSession().setAttribute("intentos", 2);
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
