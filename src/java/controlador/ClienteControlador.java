package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logicadeaccesodedatos.ClienteCRUD;
import logicadenegocios.Cliente;
import logicadevalidacion.ValidacionCliente;

/**
 *
 * @author Gustavo
 */
@WebServlet(name = "ClienteControlador", urlPatterns = {"/ClienteControlador"})
public class ClienteControlador extends HttpServlet {


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
		
		if (accion == null || accion.isEmpty()){
			dispatcher = request.getRequestDispatcher("Cliente/registrar.jsp");		
			dispatcher.forward(request, response);
			
		} else if (accion.equals("guardar")) {
			String primerApellido = request.getParameter("primerApellido");
			String segundoApellido = request.getParameter("segundoApellido");
			String nombre = request.getParameter("nombre");
			String identificacion = request.getParameter("identificacion");
			String fechaNacimiento = request.getParameter("fechaNacimiento");
			String numeroTelefono = request.getParameter("numeroTelefono");
			String correoElectronico = request.getParameter("correoElectronico");

			Cliente cliente = new Cliente(identificacion, primerApellido, segundoApellido, nombre, convertirStringADate(fechaNacimiento), numeroTelefono, correoElectronico);

			//NOTA: QUIZ??S HAYA QUE A??ADIRLO A UN ARRAY POR AC??
			ClienteCRUD clienteCrud = new ClienteCRUD();
			if (clienteCrud.registrarCliente(cliente)) { //////////////////////VALIDAR NUMERO
				
				String mensaje = "Cliente registrado con ??xito"; ////////////////////////////////////////////ARREGLAR
				request.getSession().setAttribute("mensaje", mensaje);

			} else {
				String mensaje = "No se pudo registrar el cliente";////////////////////////////////////////ARREGLAR
				request.getSession().setAttribute("mensaje", mensaje);
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
	
	
	private Date convertirStringADate(String pFecha){
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(pFecha);
		} catch (ParseException ex) {
			Logger.getLogger(ClienteControlador.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}
}