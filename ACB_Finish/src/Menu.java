import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Menu {
	
	private int op;
	
	public int menuPrincipal() throws NumberFormatException, IOException{
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		
		
			System.out.println("###########  ACB  ###########");
			System.out.println("1. Muesta equipos");
			System.out.println("2. Muestra jugadores de determinado equipo");
			System.out.println("3. Crea equipo");
			System.out.println("4. Crea Jugador");
			System.out.println("5. Crea partido");
			System.out.println("6. Muestra jugadores sin equipo");
			System.out.println("7. Asigna jugador a un equipo");
			System.out.println("8. Desvincula jugador de un equipo");
			System.out.println("9. Cargar Estadisticas");
			System.out.println("10. Salir");
			System.out.println("#############################");
			System.out.println("Elige una opcion: ");
			op=Integer.parseInt(br.readLine());
		
		
		return op;
	}

}
