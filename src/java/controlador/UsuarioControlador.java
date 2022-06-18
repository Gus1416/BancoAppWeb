package controlador;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logicacreacional.BitacoraSingleton;
import logicadeaccesodedatos.OperacionCRUD;
import logicadeaccesodedatos.UsuarioCRUD;
import logicadenegocios.Bitacora;

@WebServlet(name = "UsuarioControlador", urlPatterns = {"/UsuarioControlador"})
public class UsuarioControlador extends HttpServlet {
		RequestDispatcher dispatcher = null;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String accion;
		accion = request.getParameter("accion");

		if (accion.equals("iniciarSesionUsuario")) {
			dispatcher = request.getRequestDispatcher("Usuario/inicioSesion.jsp");
			dispatcher.forward(request, response);
			
		}else if (accion.equals("autorizar")) {
			System.out.println("entra");
			String nombreUsuario = request.getParameter("nombreUsuario");
			String contrasena = request.getParameter("contrasena");
			if (autorizarUsuario(nombreUsuario, contrasena)){
				enviarSolicitud(request, response, "Usuario/consultarBitacora.jsp");
			} else {
				request.getSession().setAttribute("mensaje", "El nombre de usuario o la contraseña son incorrectos");
				response.sendRedirect("MenuControlador");
			}
					
		} else if (accion.equals("consultarBitacora")){
			String formato = request.getParameter("formato");
			String filtro = request.getParameter("filtro");
			mostrarBitacora(formato, filtro);
		}	
	}
	//"Usuario/consultarBitacora.jsp"
	private void enviarSolicitud(HttpServletRequest request, HttpServletResponse response, String pRuta) throws ServletException, IOException {
		dispatcher = request.getRequestDispatcher(pRuta);
		dispatcher.forward(request, response);
	}
	
	private boolean autorizarUsuario(String pNombreUsuario, String pContrasena){
		return new UsuarioCRUD().iniciarSesion(pNombreUsuario, pContrasena);
	}
	
	private void mostrarBitacora(String pFormato, String pFiltro){
		ArrayList<String[]> registros = obtenerRegistros(pFiltro);
		cargarArchivos(registros);
		
		switch (pFormato) {
			case "csv" -> abrirCsv();
			case "xml" -> abrirXml();
			case "tramaPlana" -> abrirTramaPlana();
			default -> System.out.println("Error");
		}
	}
	
	private ArrayList<String[]> obtenerRegistros(String pFiltro) {
		OperacionCRUD operacionCrud = new OperacionCRUD();
		ArrayList<String[]> registros;
		registros = switch (pFiltro) {
			case "hoy" -> operacionCrud.consultarBitacoraFecha(obtenerFechaSistema());
			case "todos" -> operacionCrud.consultarBitacora();
			default -> operacionCrud.consultarBitacoraVista(pFiltro);
		};
		return registros;
	}
	
	private void cargarArchivos(ArrayList<String[]> pRegistros){
		borrarArchivosExistentes();
		Bitacora bitacora = BitacoraSingleton.getInstance();
		for (String[] registro : pRegistros){
			bitacora.setRegistro(registro);
		}
	}
	
	private void borrarArchivosExistentes(){
		new File("C:\\Users\\Gustavo\\OneDrive\\Documentos\\NetBeansProjects\\BancoApp\\bitacora.csv").delete();
		new File("C:\\Users\\Gustavo\\OneDrive\\Documentos\\NetBeansProjects\\BancoApp\\FormatoPosicional.txt").delete();
		new File("C:\\Users\\Gustavo\\OneDrive\\Documentos\\NetBeansProjects\\BancoApp\\Bitacoras.xml").delete();
	}
	
	private String obtenerFechaSistema(){
		Date fecha = new Date(System.currentTimeMillis());
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
		return formatoFecha.format(fecha);
	}
	
	private void abrirCsv(){
		try {
			Desktop.getDesktop().open(new File("C:\\Users\\Gustavo\\OneDrive\\Documentos\\NetBeansProjects\\BancoApp\\bitacora.csv"));
		} catch (IOException ex) {
			System.out.println(Arrays.toString(ex.getStackTrace()));
		}
	}
	
	private void abrirXml() {
		try {
			File file = new File("C:\\Users\\Gustavo\\OneDrive\\Documentos\\NetBeansProjects\\BancoApp\\Bitacoras.xml");
			if (!Desktop.isDesktopSupported()) {
				System.out.println("Desktop is not supported");
				return;
			}
			Desktop desktop = Desktop.getDesktop();
			if (file.exists()) {
				desktop.open(file);
			}
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private void abrirTramaPlana() {
		ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "C:\\Users\\Gustavo\\OneDrive\\Documentos\\NetBeansProjects\\BancoApp\\FormatoPosicional.txt");
		try {
			pb.start();
		} catch (IOException ex) {
			System.out.println(Arrays.toString(ex.getStackTrace()));
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}