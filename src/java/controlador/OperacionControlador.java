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
import logicacreacional.OperacionCRUDSingleton;
import logicadeaccesodedatos.ClienteCRUD;
import logicadeaccesodedatos.CuentaCRUD;
import logicadeaccesodedatos.OperacionCRUD;
import logicadenegocios.Bitacora;
import logicadenegocios.Cliente;
import logicadenegocios.Cuenta;
import logicadenegocios.Operacion;
import logicadenegocios.PalabraSecreta;
import logicadevalidacion.FondosInsuficientesExcepcion;
import serviciosexternos.Correo;
import serviciosexternos.CorreoLenguajeDecorator;
import serviciosexternos.ICorreo;
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
	OperacionCRUD operacionCrud = OperacionCRUDSingleton.getInstance();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.request = request;
		String accion = request.getParameter("accion");
		
		if (accion.equals("cambiarPin")) {
			enviarSolicitud(request, response, "Operacion/cambioPin.jsp");
			
		} else if (accion.equals("actualizarPin")) {
			String numeroCuenta = request.getParameter("numeroCuenta");
			String pinActual = request.getParameter("pinActual");
			String nuevoPin = request.getParameter("nuevoPin");
			actualizarPin(numeroCuenta, pinActual, nuevoPin);
			registrarEnBitacora("Cambio de PIN", "WEB");
			response.sendRedirect("MenuControlador");
		
		} else if (accion.equals("depositarColones")) {
			enviarSolicitud(request, response, "Operacion/depositoColones.jsp");
		
		} else if (accion.equals("realizarDepositoColones")){
			String numeroCuenta = request.getParameter("numeroCuenta");
			String montoDeposito = request.getParameter("montoDeposito");
			realizarDepositoColones(numeroCuenta, Double.parseDouble(montoDeposito));
			registrarEnBitacora("Depósito en Colones", "WEB");
			response.sendRedirect("MenuControlador");
						
		} else if (accion.equals("depositarDolares")) {
			enviarSolicitud(request, response, "Operacion/depositoDolares.jsp");
				
		} else if (accion.equals("realizarDepositoDolares")){
			String numeroCuenta = request.getParameter("numeroCuenta");
			String montoDeposito = request.getParameter("montoDeposito");
			realizarDepositoDolares(numeroCuenta, Double.parseDouble(montoDeposito));
			registrarEnBitacora("Depósito en Dólares", "WEB");
			response.sendRedirect("MenuControlador");
						
		} else if (accion.equals("retirarColones")) {	
			enviarSolicitud(request, response, "Operacion/retiroColones.jsp");
					
		} else if (accion.equals("realizarRetiroColones")){	
			String numeroCuenta = request.getParameter("numeroCuenta");
			String pin = request.getParameter("pin");
			String montoRetiro = request.getParameter("montoRetiro");		
			realizarRetiroColones(numeroCuenta, pin, Double.parseDouble(montoRetiro));
			registrarEnBitacora("Retiro en Colones", "WEB");
			response.sendRedirect("MenuControlador?accion=retirarColones");

		} else if (accion.equals("verificarPalabraSecreta")) {
			String palabraSecretaDigitada = request.getParameter("palabraSecreta");
			String numeroCuenta = request.getParameter("cuenta");
			String montoRetiro = request.getParameter("monto");
			verificarPalabraSecreta(palabraSecretaDigitada, numeroCuenta,Double.parseDouble(montoRetiro));
			response.sendRedirect("MenuControlador");

		} else if (accion.equals("retirarDolares")) {
			enviarSolicitud(request, response, "Operacion/retiroDolares.jsp");

		} else if (accion.equals("realizarRetiroDolares")) {
			String numeroCuenta = request.getParameter("numeroCuenta");
			String pin = request.getParameter("pin");
			String montoRetiro = request.getParameter("montoRetiro");
			realizarRetiroDolares(numeroCuenta, pin, Double.parseDouble(montoRetiro));
			registrarEnBitacora("Retiro en Dólares", "WEB");
			response.sendRedirect("MenuControlador?accion=retirarDolares");
	
		} else if (accion.equals("verificarPalabraSecretaDolares")){
			String numeroCuenta = request.getParameter("cuenta");
			String palabraSecretaDigitada = request.getParameter("palabraSecreta");
			String montoRetiro = request.getParameter("monto");
			verificarPalabraSecretaDolares(palabraSecretaDigitada, numeroCuenta, Double.parseDouble(montoRetiro));
			response.sendRedirect("MenuControlador");
			
		}else if (accion.equals("transferir")){
			enviarSolicitud(request, response, "Operacion/transferencia.jsp");
			
		}else if (accion.equals("realizarTransferencia")){
			String numeroCuenta = request.getParameter("numeroCuenta");
			String pin = request.getParameter("pin");
			String montoTransferencia = request.getParameter("montoTransferencia");
			String numeroCuentaDestino = request.getParameter("numeroCuentaDestino");
			realizarTransferencia(numeroCuenta, pin, Double.parseDouble(montoTransferencia), numeroCuentaDestino);
			registrarEnBitacora("Transferencia Bancaria (CRC)", "WEB");
		    response.sendRedirect("MenuControlador?accion=transferir");		

		}else if (accion.equals("verificarPalabraSecretaTransferencia")){
			String numeroCuenta = request.getParameter("cuenta");
			String palabraSecretaDigitada = request.getParameter("palabraSecreta");
			String montoTransferencia = request.getParameter("monto");
			String numeroCuentaDestino = request.getParameter("cuentaDestino");
			verificarPalabraSecretaTransferencia(palabraSecretaDigitada, numeroCuenta, Double.parseDouble(montoTransferencia), numeroCuentaDestino);
			response.sendRedirect("MenuControlador");
				
		}else if (accion.equals("consultarTipoCambioCompra")){
			TipoCambio tc = new TipoCambio();
			registrarEnBitacora("Obtener TC Compra", "WEB");
			request.setAttribute("tipoCambioCompra", tc.getCompra());	
			enviarSolicitud(request, response, "Operacion/tipoCambioCompra.jsp");
				
		}else if (accion.equals("consultarTipoCambioVenta")){
			TipoCambio tc = new TipoCambio();
			registrarEnBitacora("Obtener TC Venta", "WEB");
			request.setAttribute("tipoCambioVenta", tc.getVenta());
			enviarSolicitud(request, response, "Operacion/tipoCambioVenta.jsp");			
			
		} else if (accion.equals("consultarSaldoActual")){
			enviarSolicitud(request, response, "Operacion/consultaSaldoActual.jsp");
					
		} else if (accion.equals("verificarConsultaSaldoActual")){
			String numeroCuenta = request.getParameter("numeroCuenta");
			String pin = request.getParameter("pin");	
			verificarConsultaSaldoActual(numeroCuenta, pin);
			registrarEnBitacora("Obtener Saldo Actual (CRC)", "WEB");
			response.sendRedirect("MenuControlador");

		} else if (accion.equals("consultarSaldoActualDolares")) {
			enviarSolicitud(request, response, "Operacion/consultaSaldoActualDolares.jsp");					
			
		} else if (accion.equals("verificarConsultaSaldoActualDolares")) {
			String numeroCuenta = request.getParameter("numeroCuenta");
			String pin = request.getParameter("pin");
			verificarConsultaSaldoActualDolares(numeroCuenta, pin);
			registrarEnBitacora("Obtener Saldo Actual (USD)", "WEB");
			response.sendRedirect("MenuControlador");
					
		} else if (accion.equals("consultarEstadoCuenta")){
			enviarSolicitud(request, response, "Operacion/estadoCuenta.jsp");
							
		} else if (accion.equals("verificarConsultaEstadoCuenta")){
			String numeroCuenta = request.getParameter("numeroCuenta");
			String pin = request.getParameter("pin");			
			verificarConsultaEstadoCuenta(numeroCuenta, pin);
			registrarEnBitacora("Consulta Estado de Cuenta (CRC)", "WEB");
			response.sendRedirect("MenuControlador");
			
		}else if (accion.equals("consultarEstadoCuentaDolares")){
			enviarSolicitud(request, response, "Operacion/estadoCuentaDolares.jsp");
	
		} else if (accion.equals("verificarConsultaEstadoCuentaDolares")) {
			String numeroCuenta = request.getParameter("numeroCuenta");
			String pin = request.getParameter("pin");
			verificarConsultaEstadoCuentaDolares(numeroCuenta, pin);
			registrarEnBitacora("Consulta Estado de Cuenta (USD)", "WEB");
			response.sendRedirect("MenuControlador");
		
		} else if (accion.equals("consultarEstatus")){
			enviarSolicitud(request, response, "Operacion/consultaEstatus.jsp");
					
		} else if (accion.equals("verificarConsultaEstatus")) {
			String numeroCuenta = request.getParameter("numeroCuenta");	
			verificarConsultaEstatus(numeroCuenta);
			registrarEnBitacora("Consulta Estatus de Cuenta", "WEB");
			response.sendRedirect("MenuControlador");
			
		} else if (accion.equals("consultarTotalComisiones")) {
			consultarTotalComisiones();
			registrarEnBitacora("Consulta Ganancias Totales Comisión (General)", "WEB");
			enviarSolicitud(request, response, "Operacion/totalComisiones.jsp");
			
		}else if (accion.equals("consultarComisionesCuenta")) {
			enviarSolicitud(request, response, "Operacion/comisionesCuenta.jsp");
		
		}else if (accion.equals("verificarConsultaComisionesCuenta")) {
			String numeroCuenta = request.getParameter("numeroCuenta");
			consultarComisionesCuenta(numeroCuenta);
			registrarEnBitacora("Consulta Ganancias Totales Comisión (Específico)", "WEB");
			enviarSolicitud(request, response, "Operacion/totalComisiones.jsp");
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
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void actualizarPin(String pNumeroCuenta, String pinActual, String pNuevoPin) {
		cuenta = cuentaCrud.consultarCuenta(pNumeroCuenta);
		
		if (sePermiteOperacion(cuenta) && validarPin(cuenta.getPin(), pinActual)) {
			cuenta.cambiarPin(pNuevoPin);
			cuentaCrud.cambiarPin(cuenta);
			request.getSession().setAttribute("mensaje", "Estimado usuario, se ha cambiado satisfactoriamente el PIN de su cuenta " + pNumeroCuenta);
		}
	}
	
	private void realizarDepositoColones(String pNumeroCuenta, double pMontoDeposito) {
		cuenta = cuentaCrud.consultarCuenta(pNumeroCuenta);

		if (sePermiteOperacion(cuenta)) {
			int cantidadOperaciones = operacionCrud.obtenerCantidadOperaciones();
			cuenta.depositarColones(pMontoDeposito, cantidadOperaciones);
			cuentaCrud.actualizarSaldo(cuenta);
			ArrayList<Operacion> operaciones = cuenta.getOperaciones();
			Operacion operacion = operaciones.get(operaciones.size() - 1);
			//Operacion operacion = cuenta.getOperaciones().get(cuenta.getOperaciones().size() - 1);
			new OperacionCRUD().registrarOperacion(operacion, pNumeroCuenta);
			//operacionCrud.registrarOperacion(operacion, pNumeroCuenta);

			request.getSession().setAttribute("mensaje", "Estimado usuario, se han depositado correctamente " + String.format("%,.2f", pMontoDeposito) + " colones"
							+ "<br></br>[El monto real depositado a su cuenta " + cuenta.getNumeroCuenta() + " es de " + String.format("%,.2f", (pMontoDeposito - operacion.getMontoComision())) + "colones]"
							+ "<br></br>[El monto cobrado por concepto de comisión fue de " + String.format("%,.2f", operacion.getMontoComision()) + " colones, que"
							+ "<br></br>fueron rebajados automáticamente de su saldo actual]");
		}
	}
	
	private void realizarDepositoDolares(String pNumeroCuenta, double pMontoDeposito) {
		cuenta = cuentaCrud.consultarCuenta(pNumeroCuenta);
		TipoCambio tc = new TipoCambio();

		if (sePermiteOperacion(cuenta)) {
			int cantidadOperaciones = operacionCrud.obtenerCantidadOperaciones();
			cuenta.depositarDolares(pMontoDeposito, cantidadOperaciones);
			cuentaCrud.actualizarSaldo(cuenta);
			Operacion operacion = cuenta.getOperaciones().get(cuenta.getOperaciones().size() - 1);
			new OperacionCRUD().registrarOperacion(operacion, pNumeroCuenta);
			request.getSession().setAttribute("mensaje", "Estimado usuario, se han depositado correctamente " + String.format("%,.2f", pMontoDeposito) + " dólares"
							+ "<br></br>[Según el BCCR, el tipo de cambio actual de compra del dólar es " + tc.getCompra() +  "]"
							+ "<br></br>[El monto equivalente en colones es " + String.format("%,.2f", tc.convertirAColones(pMontoDeposito) )+ "]"
							+ "<br></br>[El monto real depositado a su cuenta " + cuenta.getNumeroCuenta() + " es de " + String.format("%,.2f", (tc.convertirAColones(pMontoDeposito)- operacion.getMontoComision())) + "colones]"
							+ "<br></br>[El monto cobrado por concepto de comisión fue de " + String.format("%,.2f", operacion.getMontoComision()) + " colones, que"
							+ "<br></br>fueron rebajados automáticamente de su saldo actual]");
		}
	}
	
	private void realizarRetiroColones(String pNumeroCuenta, String pPin, double pMontoRetiro) {
		cuenta = cuentaCrud.consultarCuenta(pNumeroCuenta);
		Cliente cliente = new ClienteCRUD().consultarPropietarioCuenta(pNumeroCuenta);

		if (sePermiteOperacion(cuenta) && validarPin(cuenta.getPin(), pPin)) {
			palabraSecreta = PalabraSecreta.generarPalabraSecreta();
			Sms.enviarSms("Su palabra secreta es: " + palabraSecreta, cliente.getNumeroTelefono());
			request.getSession().setAttribute("cuenta", cuenta.getNumeroCuenta());
			request.getSession().setAttribute("cliente", cliente.getIdentificacion());
			request.getSession().setAttribute("monto", pMontoRetiro);
			request.getSession().setAttribute("mensajeTexto", "Estimado usuario se ha enviado una palabra por mensaje de texto, por favor revise sus mensajes y proceda a digitar la palabra enviada");
		}
	}
	
	private void verificarPalabraSecreta(String pPalabraSecretaDigitada, String pNumeroCuenta, double pMontoRetiro) {
		cuenta = cuentaCrud.consultarCuenta(pNumeroCuenta);
		
		if (validarPalabraSecreta(palabraSecreta, pPalabraSecretaDigitada, cuenta)) {
			try {
			int cantidadOperaciones = operacionCrud.obtenerCantidadOperaciones();
				cuenta.retirarColones(pMontoRetiro, cantidadOperaciones);
				cuentaCrud.actualizarSaldo(cuenta);
				Operacion operacion = cuenta.getOperaciones().get(cuenta.getOperaciones().size() - 1);												
				new OperacionCRUD().registrarOperacion(operacion, cuenta.getNumeroCuenta());
				request.getSession().setAttribute("mensaje", "Estimado usuario, el monto de este retiro es " + pMontoRetiro + " colones."
								+ "<br></br> [El monto cobrado por concepto de comisión fue de " + operacion.getMontoComision() + " colones"
								+ ", que fueron rebajados automáticamente de su saldo actual]");

			} catch (FondosInsuficientesExcepcion ex) {
				request.getSession().setAttribute("mensaje", "No hay suficientes fondos para realizar la operación");
			}
		}
	}
	
	private void realizarRetiroDolares(String pNumeroCuenta, String pPin, double pMontoRetiro) {
		cuenta = cuentaCrud.consultarCuenta(pNumeroCuenta);
		Cliente cliente = new ClienteCRUD().consultarPropietarioCuenta(pNumeroCuenta);

		if (sePermiteOperacion(cuenta) && validarPin(cuenta.getPin(), pPin)) {
			palabraSecreta = PalabraSecreta.generarPalabraSecreta();
			Sms.enviarSms("Su palabra secreta es: " + palabraSecreta, cliente.getNumeroTelefono());
			request.getSession().setAttribute("cuenta", cuenta.getNumeroCuenta());
			request.getSession().setAttribute("cliente", cliente.getIdentificacion());
			request.getSession().setAttribute("monto", pMontoRetiro);
			request.setAttribute("mensajeTexto", "Estimado usuario se ha enviado una palabra por mensaje de texto, por favor revise sus mensajes y proceda a digitar la palabra enviada");
		}
	}
	
	private void verificarPalabraSecretaDolares(String pPalabraSecretaDigitada, String pNumeroCuenta, double pMontoRetiro) {
		TipoCambio tc = new TipoCambio();
		cuenta = cuentaCrud.consultarCuenta(pNumeroCuenta);

		if (validarPalabraSecreta(palabraSecreta, pPalabraSecretaDigitada, cuenta)) {
			try {
				
			int cantidadOperaciones = operacionCrud.obtenerCantidadOperaciones();
				cuenta.retirarDolares(pMontoRetiro, cantidadOperaciones);
				cuentaCrud.actualizarSaldo(cuenta);
				Operacion operacion = cuenta.getOperaciones().get(cuenta.getOperaciones().size() - 1);
				new OperacionCRUD().registrarOperacion(operacion, cuenta.getNumeroCuenta());
				request.getSession().setAttribute("mensaje", "Estimado usuario, el monto de este retiro es " + pMontoRetiro + " dólares."
								+ "<br></br>  [Según el BCCR, el tipo de cambio de venta del dólar de hoy es: " + tc.getVenta() + "]"
								+ "<br></br> [El monto equivalente de su retiro es " + String.format("%,.2f", tc.convertirAColones(pMontoRetiro)) + " colones]"
								+ "<br></br> [El monto cobrado por concepto de comisión fue de " + operacion.getMontoComision() + " colones"
								+ ", que fueron rebajados automáticamente de su saldo actual]");

			} catch (FondosInsuficientesExcepcion ex) {
				request.getSession().setAttribute("mensaje", "No hay suficientes fondos para realizar la operación");
			}
		}
	}
	
	private void realizarTransferencia(String pNumeroCuenta, String pPin, double pMontoTransferencia, String pNumeroCuentaDestino) {
		cuenta = cuentaCrud.consultarCuenta(pNumeroCuenta);
		Cuenta cuentaDestino = cuentaCrud.consultarCuenta(pNumeroCuentaDestino);
		Cliente cliente = new ClienteCRUD().consultarPropietarioCuenta(pNumeroCuenta);

		if (sePermiteOperacion(cuenta) && validarPin(cuenta.getPin(), pPin) && sePermiteOperacion(cuentaDestino))  {
			palabraSecreta = PalabraSecreta.generarPalabraSecreta();
			Sms.enviarSms("Su palabra secreta es: " + palabraSecreta, cliente.getNumeroTelefono());
			request.getSession().setAttribute("cuenta", cuenta.getNumeroCuenta());
			request.getSession().setAttribute("cliente", cliente.getIdentificacion());
			request.getSession().setAttribute("monto", pMontoTransferencia);
			request.getSession().setAttribute("cuentaDestino", cuentaDestino.getNumeroCuenta());
			request.getSession().setAttribute("mensajeTexto", "Estimado usuario se ha enviado una palabra por mensaje de texto, por favor revise sus mensajes y proceda a digitar la palabra enviada");
		}
	}
	
		private void verificarPalabraSecretaTransferencia(String pPalabraSecretaDigitada, String pNumeroCuenta, double pMontoTransferencia, String pNumeroCuentaDestino) {
		cuenta = cuentaCrud.consultarCuenta(pNumeroCuenta);
		Cuenta cuentaDestino = cuentaCrud.consultarCuenta(pNumeroCuentaDestino);
		int cantidadOperaciones = 0;
		
		if (validarPalabraSecreta(palabraSecreta, pPalabraSecretaDigitada, cuenta)) {
			try {
				cantidadOperaciones = operacionCrud.obtenerCantidadOperaciones();
				cuenta.retirarColones(pMontoTransferencia, cantidadOperaciones);
				cuentaCrud.actualizarSaldo(cuenta);
				Operacion operacion = cuenta.getOperaciones().get(cuenta.getOperaciones().size() - 1);
				new OperacionCRUD().registrarOperacion(operacion, cuenta.getNumeroCuenta());
				
			    cantidadOperaciones = operacionCrud.obtenerCantidadOperaciones();
				cuentaDestino.depositarColones(pMontoTransferencia, cantidadOperaciones);
				cuentaCrud.actualizarSaldo(cuentaDestino);
				Operacion operacionDestino = cuentaDestino.getOperaciones().get(cuentaDestino.getOperaciones().size() - 1);
				new OperacionCRUD().registrarOperacion(operacionDestino, cuentaDestino.getNumeroCuenta());
						
				request.getSession().setAttribute("mensaje", "Estimado usuario, la transferencia de fondos se ejecutó satisfactoriamente. "
								+ "El monto retirado de la cuenta origen y depositado en la cuenta destino es " + String.format("%,.2f", pMontoTransferencia) + " colones."
								+ "<br></br> [El monto cobrado por concepto de comisión fue de " + operacion.getMontoComision() + " colones"
								+ ", que fueron rebajados automáticamente de su saldo actual]");

			} catch (FondosInsuficientesExcepcion ex) {
				request.getSession().setAttribute("mensaje", "No hay suficientes fondos para realizar la operación");
			}
		}
	}
		
	private void verificarConsultaSaldoActual(String pNumeroCuenta, String pPin) {
		cuenta = cuentaCrud.consultarCuenta(pNumeroCuenta);

		if (sePermiteOperacion(cuenta) && validarPin(cuenta.getPin(), pPin)) {
			request.getSession().setAttribute("mensaje", "Estimado usuario el saldo actual de su cuenta es " + String.format("%,.2f", cuenta.consultarSaldoActual()) + " colones");
		}
	}
	
	private void verificarConsultaSaldoActualDolares(String pNumeroCuenta, String pPin) {
		cuenta = cuentaCrud.consultarCuenta(pNumeroCuenta);

		if (sePermiteOperacion(cuenta) && validarPin(cuenta.getPin(), pPin)) {
			TipoCambio tc = new TipoCambio();
			request.getSession().setAttribute("mensaje", "Estimado usuario el saldo actual de su cuenta es " + String.format("%,.2f", tc.convertirADolares(cuenta.consultarSaldoActual())) + " dólares"
							+ "<br></br>Para esta conversión se utilizó el tipo de cambio del dólar, precio de compra."
							+ "<br></br>[Según el BCCR, el tipo de cambio de compra del dólar de hoy es: " + tc.getCompra());
		}
	}
	
	private void verificarConsultaEstadoCuenta(String pNumeroCuenta, String pPin) {
		cuenta = cuentaCrud.consultarCuenta(pNumeroCuenta);

		if (sePermiteOperacion(cuenta) && validarPin(cuenta.getPin(), pPin)) {
			request.getSession().setAttribute("mensaje", cambiarSaltosLinea(cuenta.estadoCuentaColones()));
		}
	}
	
	private void verificarConsultaEstadoCuentaDolares(String pNumeroCuenta, String pPin) {
		cuenta = cuentaCrud.consultarCuenta(pNumeroCuenta);

		if (sePermiteOperacion(cuenta) && validarPin(cuenta.getPin(), pPin)) {
			request.getSession().setAttribute("mensaje", cambiarSaltosLinea(cuenta.estadoCuentaDolares()));
		}
	}
	
	private void verificarConsultaEstatus(String pNumeroCuenta) {
		cuenta = cuentaCrud.consultarCuenta(pNumeroCuenta);

		if (esCuentaRegistrada(cuenta)) {
			request.getSession().setAttribute("mensaje", "La cuenta número " + cuenta.getNumeroCuenta() + " tiene estatus de  " + cuenta.getEstatus());
		} else {
			request.getSession().setAttribute("mensaje", "El número de la cuenta es incorrecto");
		}
	}
	
	private void consultarComisionesCuenta(String pNumeroCuenta){
		cuenta = cuentaCrud.consultarCuenta(pNumeroCuenta);
		if (sePermiteOperacion(cuenta)) {
			double comisionesDepositos =  cuenta.calcularTotalComisionesDepositos();
			double comisionesRetiros = cuenta.calcularTotalComisionesRetiros();
			double totalComisiones = cuenta.calcularTotalComisiones();	
			request.setAttribute("totalComisiones", String.format("%,.2f", totalComisiones));
			request.setAttribute("comisionesDepositos", String.format("%,.2f",comisionesDepositos));
			request.setAttribute("comisionesRetiros", String.format("%,.2f", comisionesRetiros));
		}
	}
	
	private void consultarTotalComisiones(){
		ArrayList<Cuenta> cuentas = cuentaCrud.consultarCuentas();
		double comisionesDepositos = 0.0;
		double comisionesRetiros = 0.0;
		double totalComisiones = 0.0;
		
		for (Cuenta cuenta : cuentas){
			totalComisiones += cuenta.calcularTotalComisiones();
			comisionesDepositos += cuenta.calcularTotalComisionesDepositos();
			comisionesRetiros += cuenta.calcularTotalComisionesRetiros();
		}
		request.setAttribute("totalComisiones", String.format("%,.2f", totalComisiones));
		request.setAttribute("comisionesDepositos", String.format("%,.2f",comisionesDepositos));
		request.setAttribute("comisionesRetiros", String.format("%,.2f", comisionesRetiros));
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
			gestionarIntentosFallidos(true);
			return true;
		} else {
			request.getSession().setAttribute("mensaje", "La palabra secreta es incorrecta");
			gestionarIntentosFallidos(false);
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
		System.out.println("QUEDAN " + this.intentos + "INTENTOS-------------------------------------------");
	}
	
	private void inactivarCuentaPorIntentosFallidos() {
		cuenta.inactivarCuenta();
		cuentaCrud.cambiarEstatus(cuenta);
		enviarCorreo();
	}
	
	private void enviarCorreo(){
		Cliente cliente = new ClienteCRUD().consultarPropietarioCuenta(cuenta.getNumeroCuenta());
		ICorreo correo = new CorreoLenguajeDecorator(new Correo(cliente.getNombre() + " " + cliente.getPrimerApellido() + " " + cliente.getSegundoApellido(), cuenta.getNumeroCuenta()));
		//Correo correo = new Correo(cliente.getNombre() + " " + cliente.getPrimerApellido() + " " + cliente.getSegundoApellido(), cuenta.getNumeroCuenta());
		correo.enviarCorreo(cliente.getCorreoElectronico());
	}

	private String cambiarSaltosLinea(String pTexto) {
		String textoCambiado = pTexto.replace("\n", "<br></br>");
		return textoCambiado;
	}
}