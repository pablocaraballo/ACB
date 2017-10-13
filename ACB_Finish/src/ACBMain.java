import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

public class ACBMain {

	public static void main(String[] args) throws NumberFormatException, IOException, ClassNotFoundException, SQLException, ParseException {
		
		Menu menu=new Menu();
		Connection conn=null;
		DBAccessor dbaccessor=new DBAccessor();
		dbaccessor.init();
		conn=dbaccessor.getConnection();
		int op;
		
		do{
			op=menu.menuPrincipal();
			switch(op){
			
				case 1: dbaccessor.muestraEquipos(); break;
				case 2: dbaccessor.muestraJugadoresEquipo(); break;
				case 3: dbaccessor.creaEquipo(); break;
				case 4: dbaccessor.creaJugador();break;
				case 5: dbaccessor.creaPartido();break;
				case 6: dbaccessor.muestraJugadorSinEquipo();break;
				case 7: dbaccessor.asignaJugadorEquipo();break;
				case 8: dbaccessor.desvinculaJugadorEquipo(); break;
				case 9: dbaccessor.cargarEstadisticas(); break;
				case 10: System.out.println("Nos vemos pronto!");break;
				default: break;
			}
			
		}while(op!=10);
	}

}
