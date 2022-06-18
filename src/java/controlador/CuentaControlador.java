package controlador;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logicacreacional.BitacoraSingleton;
import logicadeaccesodedatos.ClienteCRUD;
import logicadeaccesodedatos.CuentaCRUD;
import logicadeaccesodedatos.OperacionCRUD;
import logicadenegocios.Bitacora;
import logicadenegocios.Cliente;
import logicadenegocios.Cuenta;
import logicadenegocios.Ordenacion;

/**
 *
 * @author Gustavo
 */
@WebServlet(name = "CuentaControlador", urlPatterns = {"/CuentaControlador"})
public class CuentaControlador extends HttpServlet {
	ArrayList<Cliente> clientes = new ClienteCRUD().consultarClientes();
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
		
		if (accion.equals("registrarCuenta")){
			dispatcher = request.getRequestDispatcher("Cuenta/registroCuenta.jsp");
			dispatcher.forward(request, response);
			
		} else if (accion.equals("guardar")){
			String identificacion = request.getParameter("identificacion");
			String pin = request.getParameter("pin");
			double montoInicial = Double.parseDouble(request.getParameter("montoInicial"));
			registrarCuenta(identificacion, pin, montoInicial);
			registrarEnBitacora("Creación de Cuenta Bancaria", "WEB");
			response.sendRedirect("MenuControlador");
			
		} else if (accion.equals("listarCuentas")){
			dispatcher = request.getRequestDispatcher("Cuenta/listaCuentas.jsp");
			listarCuentas();
			registrarEnBitacora("Listar Cuentas Ordenadas", "WEB");
			dispatcher.forward(request, response);
			
		} else if (accion.equals("verDetallesCuenta")){
			dispatcher = request.getRequestDispatcher("Cuenta/detallesCuenta.jsp");
			String numeroCuenta = request.getParameter("numeroCuenta");
			verDetallesCuenta(numeroCuenta);
			registrarEnBitacora("Consulta Detalle Cuenta", "WEB");
			dispatcher.forward(request, response);
			
		} else if (accion.equals("cambiarPin")){
			dispatcher = request.getRequestDispatcher("Cuenta/cambioPin.jsp");
			request.getSession().setAttribute("intentos", 2);
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

	private Date obtenerFechaHoraSistema() {
		return new Date(System.currentTimeMillis());
	}
	
	private void registrarCuenta(String pIdentificacion, String pPin, double pMontoInicial) {
		 int numeroCuentas = new CuentaCRUD().obtenerCantidadCuentas();
		Cuenta cuenta = new Cuenta(pPin, pMontoInicial, numeroCuentas);
		Cliente cliente = new ClienteCRUD().consultarCliente(pIdentificacion);

		if (new CuentaCRUD().registrarCuenta(cuenta, pIdentificacion)) {
			
			String mensaje = "Se ha creado una nueva cuenta en el sistema, los datos de la cuenta son: "
							+ "<br></br>Número de cuenta: " + cuenta.getNumeroCuenta()
							+ "<br></br>Estatus de la cuenta: " + cuenta.getEstatus()
							+ "<br></br>Saldo actual. " + String.format("%,.2f", cuenta.getSaldo())
							+ "<br></br>---"
							+ "<br></br>Nombre del dueño de la cuenta: " + cliente.getNombre() + " " + cliente.getPrimerApellido() + " " + cliente.getSegundoApellido()
							+ "<br></br>Número de teléfono asociado a la cuenta: " + cliente.getNumeroTelefono()
							+ "<br></br>Dirección de correo electrónico asociada a la cuenta. " + cliente.getCorreoElectronico();
			request.getSession().setAttribute("mensaje", mensaje);

		} else {
			String mensaje = "No se pudo registrar la cuenta";
			request.getSession().setAttribute("mensaje", mensaje);
		}
	}
	
	private void listarCuentas() {
		ArrayList<Cuenta> cuentas = new CuentaCRUD().consultarCuentas();
		Cuenta[] arregloCuentas = convertirCuentasEnArreglo(cuentas);
		Ordenacion ordenacion = new Ordenacion();
		ordenacion.insercion(arregloCuentas);
		request.setAttribute("cuentas", arregloCuentas);
		ArrayList<Cliente> clientes = new ClienteCRUD().consultarClientes();
		request.setAttribute("clientes", clientes);
	}
	
	public static Cuenta[] convertirCuentasEnArreglo(ArrayList<Cuenta> cuentas) {
		Cuenta[] arregloCuentas = new Cuenta[cuentas.size()];
		for (int i = 0; i < cuentas.size(); i++) {
			arregloCuentas[i] = cuentas.get(i);
		}
		return arregloCuentas;
	}
	
	private void verDetallesCuenta(String pNumeroCuenta) {
		Cuenta cuenta = new CuentaCRUD().consultarCuenta(pNumeroCuenta);
		String detallesCuenta = "Número de cuenta: " + cuenta.getNumeroCuenta()
							+ "<br></br>Fecha de creación: " + new SimpleDateFormat("dd-MM-yyyy").format(cuenta.getFechaCreacion())
							+ "<br></br>Estatus de la cuenta: " + cuenta.getEstatus()
							+ "<br></br>Saldo actual. " + String.format("%,.2f", cuenta.getSaldo());
		request.setAttribute("detallesCuenta", detallesCuenta);
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
}
