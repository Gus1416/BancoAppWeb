package controlador;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logicacreacional.BitacoraSingleton;
import logicadeaccesodedatos.ClienteCRUD;
import logicadeaccesodedatos.OperacionCRUD;
import logicadenegocios.Bitacora;
import logicadenegocios.Cliente;
import logicadenegocios.Ordenacion;

/**
 *
 * @author Gustavo
 */
@WebServlet(name = "ClienteControlador", urlPatterns = {"/ClienteControlador"})
public class ClienteControlador extends HttpServlet {
	HttpServletRequest request;
	
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
		this.request = request;
		String accion;
		RequestDispatcher dispatcher = null;
		accion = request.getParameter("accion");

		if (accion.equals("registrarCliente")) {
			dispatcher = request.getRequestDispatcher("Cliente/registroCliente.jsp");
			dispatcher.forward(request, response);

		} else if (accion.equals("guardar")) {
			String primerApellido = request.getParameter("primerApellido");
			String segundoApellido = request.getParameter("segundoApellido");
			String nombre = request.getParameter("nombre");
			String identificacion = request.getParameter("identificacion");
			String fechaNacimiento = request.getParameter("fechaNacimiento");
			String numeroTelefono = request.getParameter("numeroTelefono");
			String correoElectronico = request.getParameter("correoElectronico");
			registrarCliente(primerApellido, segundoApellido, nombre, identificacion, convertirStringADate(fechaNacimiento), numeroTelefono, correoElectronico);
			
			registrarEnBitacora("Registro de Cliente", "WEB");
			
			response.sendRedirect("MenuControlador");

		} else if (accion.equals("listarClientes")) {
			dispatcher = request.getRequestDispatcher("Cliente/listaClientes.jsp");
			listarClientes();
			registrarEnBitacora("Listar Clientes Ordenados", "WEB");
			dispatcher.forward(request, response);
			
		} else if (accion.equals("verDetallesCliente")){		
			dispatcher = request.getRequestDispatcher("Cliente/detallesCliente.jsp");		
			String identificacion = request.getParameter("cliente");
			Cliente cliente = new ClienteCRUD().consultarCliente(identificacion);
			String detallesCliente = cambiarSaltosLinea(cliente.toString() + "Cuentas: <br></br>" + cliente.mostrarNumerosCuentaCliente());
			registrarEnBitacora("Consulta Detalles Cliente", "WEB");
			request.setAttribute("detallesCliente", detallesCliente);
			dispatcher.forward(request, response);
		}
	}
	
	private void registrarEnBitacora(String pTipoOperacion, String pVista) {
		Date pFechaHora = obtenerFechaHoraSistema();
		SimpleDateFormat formatterFecha = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatterHora = new SimpleDateFormat("HH:mm:ss");
		String[] registro = {formatterFecha.format(pFechaHora), formatterHora.format(pFechaHora), pTipoOperacion, pVista};	
		Bitacora bitacora = BitacoraSingleton.getInstance();
		new OperacionCRUD().registrarEnBitacora(registro);
		bitacora.setRegistro(registro);
	}
	
	private Date obtenerFechaHoraSistema(){
		return new Date(System.currentTimeMillis());
	}

	private void registrarCliente(String pPrimerApellido, String pSegundoApellido, String pNombre, String pIdentificacion, Date pFechaNacimiento, String pNumeroTelefono, String pCorreoElectronico) {
		 int cantidadClientes = new ClienteCRUD().obtenerCantidadClientes();
		Cliente cliente = new Cliente(pIdentificacion, pPrimerApellido, pSegundoApellido, pNombre, pFechaNacimiento,  pNumeroTelefono, pCorreoElectronico, cantidadClientes);

		if (new ClienteCRUD().registrarCliente(cliente)) { //////////////////////VALIDAR NUMERO
			String mensaje = "Se ha creado un nuevo cliente en el sistema, los datos que fueron almacenados son:"
							+ "<br></br>Código: " + cliente.getCodigoCliente()
							+ "<br></br>Nombre. " + cliente.getPrimerApellido() + " " + cliente.getSegundoApellido() + " " + cliente.getNombre()
							+ "<br></br>Identificación: " + cliente.getIdentificacion()
							+ "<br></br>Fecha de Nacimiento: " + new SimpleDateFormat("dd-MM-yyyy").format(cliente.getFechaNacimiento())
							+ "<br></br>Número telefónico: " + cliente.getNumeroTelefono();
			request.getSession().setAttribute("mensaje", mensaje);
			
		} else {
			String mensaje = "No se pudo registrar el cliente";
			request.getSession().setAttribute("mensaje", mensaje);
		}
	}
	
	private void listarClientes() {
		ArrayList<Cliente> clientes = new ClienteCRUD().consultarClientes();
		Cliente[] arregloClientes = convertirClientesEnArreglo(clientes);
		Ordenacion ordenacion = new Ordenacion();
		ordenacion.insercion(arregloClientes);
		request.setAttribute("clientes", arregloClientes);
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
	
	private Date convertirStringADate(String pFecha){
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(pFecha);
		} catch (ParseException ex) {
			Logger.getLogger(ClienteControlador.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}
	
	private String cambiarSaltosLinea(String pTexto){
		String textoCambiado = pTexto.replace("\n", "<br></br>");
		return textoCambiado;
	}
	
	public static Cliente[] convertirClientesEnArreglo(ArrayList<Cliente> clientela) {
		Cliente[] arregloClientes = new Cliente[clientela.size()];
		for (int i = 0; i < clientela.size(); i++) {
			arregloClientes[i] = clientela.get(i);
		}
		return arregloClientes;
	}
}