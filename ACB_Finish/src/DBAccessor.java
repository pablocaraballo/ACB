import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

public class DBAccessor {
	
	private String host;
	private String port; 
	private String dbname;
	private String user;
	private String passwd;
	private String schema;
	
	Connection conn=null;
	
	
	public void init(){
		
		Properties prop=new Properties();
		InputStream sprop=this.getClass().getClassLoader().getResourceAsStream("db.properties");
		
		try{
			prop.load(sprop);
			this.host=prop.getProperty("host");
			this.port=prop.getProperty("port");
			this.dbname=prop.getProperty("dbname");
			this.user=prop.getProperty("user");
			this.passwd=prop.getProperty("passwd");
			this.schema=prop.getProperty("schema");
		}catch(IOException e){
			String mesage="Error en el archivo db.properties";
			System.err.println(mesage);
		}
	}
	
	public Connection getConnection() throws ClassNotFoundException, SQLException{
		
		Class.forName("org.postgresql.Driver");
		String url="jdbc:postgresql://"+host+":"+port+"/"+dbname;
		
		conn=DriverManager.getConnection(url,user,passwd);
		
		conn.setAutoCommit(false);
		
		return conn;
	}
	
	public void muestraEquipos() throws SQLException{
		
		Statement st=conn.createStatement();
		ResultSet rs=st.executeQuery("SELECT * FROM team");
		
		while(rs.next()){
			System.out.println("Nombre: "+rs.getString("name")+"\nTipo: "+rs.getString("type")+"\nPais: "+rs.getString("country")+"\nCiudad: "+rs.getString("city")+"\nEstadio: "+rs.getString("court_name"));
			System.out.println("----------------");
		}
		st.close();
		rs.close();
	}
	
	public void muestraJugadoresEquipo() throws SQLException, IOException{
		
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		muestraEquipos();
		System.out.println("Introduce el nombre del equipo del que quieres ver los jugadores: ");
		String name=br.readLine();
		
		Statement st=conn.createStatement();
		ResultSet rs=st.executeQuery("SELECT * FROM player p, team t WHERE t.name=p.team_name AND team_name Like '"+name+"'");
		
		while(rs.next()){
			System.out.println("Nombre: "+rs.getString("first_name")+"\nApellido: "+rs.getString("last_name")+"\nEquipo: "+rs.getString("team_name"));
			System.out.println("-------------------------------------");
		}
		rs.close();
		st.close();
	}
	
	public void creaEquipo() throws IOException, SQLException{
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		
		
		System.out.println("Introduce los datos del nuevo equipo");
		System.out.println("-------------------------------------");
		System.out.println("Nombre del equipo: ");
		String name=br.readLine();
		
		System.out.println("Tipo del equipo: ");
		String tipo=br.readLine();
		
		System.out.println("País: ");
		String pais=br.readLine();
		
		System.out.println("Ciudad: ");
		String ciudad=br.readLine();
		
		System.out.println("Nombre del estadio: ");
		String estadio=br.readLine();
		
		Statement st=conn.createStatement();
		st.executeUpdate("INSERT INTO team(name,type,country,city,court_name)"
		+ " VALUES('"+name+"','"+tipo+"','"+pais+"','"+ciudad+"','"+estadio+"')");
		
		br.close();
		st.close();
	}
	
	public void creaJugador() throws NumberFormatException, IOException, SQLException{
		
		BufferedReader br=new BufferedReader(new InputStreamReader (System.in));
		
		System.out.println("Introduce los datos del nuevo jugador");
		System.out.println("-------------------------------------");
		
		System.out.println("Federacion: ");
		String federacion=br.readLine();
		
		System.out.println("Nombre: ");
		String name=br.readLine();
		
		System.out.println("Apellido: ");
		String apellido=br.readLine();
		
		System.out.println("Edad de nacimiento: ");
		String cumple=br.readLine();
		
		
		System.out.println("Sexo: ");
		String sexo=br.readLine();
		
		System.out.println("Altura: ");
		int altura=Integer.parseInt(br.readLine());
		
		System.out.println("Nombre del equipo: ");
		String team=br.readLine();
		
		System.out.println("Numero de MVP'S: ");
		int mvp=Integer.parseInt(br.readLine());
		
		Statement st=conn.createStatement();
		
		st.executeUpdate("INSERT INTO player (federation_license_code,first_name,last_name,birth_date,gender,height,team_name,mvp_total) "
				+ "VALUES('"+federacion+"','"+name+"','"+apellido+"','"+cumple+"','"+sexo+"','"+altura+"','"+team+"',"+mvp+")");
	}
	
	public void creaPartido() throws IOException, SQLException{
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Introduce los datos del nuevo partido");
		System.out.println("-------------------------------------");
		
		System.out.println("Introduce el anfitrion: ");
		String home=br.readLine();
		
		System.out.println("Introduce el visitante: ");
		String visitant=br.readLine();
		
		System.out.println("Introduce la fecha: ");
		String fecha=br.readLine();
		
		System.out.println("Introduce la asistencia: ");
		int assist=Integer.parseInt(br.readLine());
		
		System.out.println("Introduce el numero de mvp: ");
		String mvp=br.readLine();
		
		Statement st=conn.createStatement();
		
		st.executeUpdate("INSERT INTO match (home_team,visitor_team,match_date,attendance,mvp_player) "
				+ "VALUES ('"+home+"','"+visitant+"','"+fecha+"',"+assist+",'"+mvp+"')");
	}
	
	public void muestraJugadorSinEquipo() throws SQLException{
		
		Statement st=conn.createStatement();
		ResultSet rs=st.executeQuery("SELECT * FROM player WHERE team_name IS NULL");
		
		while(rs.next()){
			System.out.println("Nombre: "+rs.getString("first_name")+"\nApellido: "+rs.getString("last_name"));
			System.out.println("----------------------------");
		}
		
		rs.close();
		st.close();
	}
	
	public void asignaJugadorEquipo() throws SQLException, IOException{
		
		
		Statement st=conn.createStatement();
		ResultSet rs=st.executeQuery("SELECT * FROM player WHERE team_name IS NULL");
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		
		while(rs.next()){
			
			System.out.println("Nombre: "+rs.getString("first_name")+"Apellido: "+rs.getString("last_name"));
			
			System.out.println("Quieres asignarle un equipo a este jugador?: ");
			String resp=br.readLine();
			
			if(resp=="si"){
				System.out.println("Introduce el nombre del equipo que quieres asignar al jugador: ");
				rs.updateString("team_name", br.readLine());
				rs.updateRow();
			}
			else
				System.out.println("---------------------------------");
		}
	}
	
	public void desvinculaJugadorEquipo() throws SQLException, IOException{
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		muestraJugadoresEquipo();
		
		System.out.println("Introduce el numero de federacion del jugador que quieres dejar sin equipo: ");
		String fede=br.readLine();
		
		Statement st=conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
		ResultSet rs=st.executeQuery("SELECT * FROM player WHERE federation_license_code LIKE '"+fede+"'");
		
		rs.next();
		rs.updateString("team_name", null);
		rs.updateRow();
		System.out.println("Jugador con el numero de federacion "+fede+" ya no tiene equipo.");
	}
	
	public void cargarEstadisticas() throws SQLException, IOException, ParseException{
		
		String sql="INSERT INTO match_statistics (home_team, visitor_team, match_date, player, minutes_played,points, offensive_rebounds, defensive_rebounds, assists, committed_fouls,received_fouls, free_throw_attempts, free_throw_made, two_point_attempts,two_point_made, three_point_attempts, three_point_made, blocks,blocks_against, steals, turnovers, mvp_score) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pst=conn.prepareStatement(sql);
		
		BufferedReader br=new BufferedReader(new FileReader("src/estadistiques.csv"));
		
		String linea=br.readLine();
		linea=br.readLine();
		
		SimpleDateFormat format=new SimpleDateFormat("dd/MM/YYYY");//Creas un formateador
		
		while(linea!=null){
			
			StringTokenizer token=new StringTokenizer(linea, ",");
			pst.clearParameters(); //Sirve para limpiar los parametros en la siguiente linea.
			
			pst.setString(1, token.nextToken());
			pst.setString(2, token.nextToken());
			java.util.Date mydate=format.parse(token.nextToken());//Utilizas el java.util con el format y pillas el texto
			java.sql.Date fecha=new java.sql.Date(mydate.getTime());//Utilizas el sql y el gettime para ponerlo en formato fecha
			pst.setDate(3, fecha); //Se coge la fecha y se carga
			pst.setInt(4, Integer.parseInt(token.nextToken()));
			pst.setInt(5, Integer.parseInt(token.nextToken()));
			pst.setInt(6, Integer.parseInt(token.nextToken()));
			pst.setInt(7, Integer.parseInt(token.nextToken()));
			pst.setInt(8, Integer.parseInt(token.nextToken()));
			pst.setInt(9, Integer.parseInt(token.nextToken()));
			pst.setInt(10, Integer.parseInt(token.nextToken()));
			pst.setInt(11, Integer.parseInt(token.nextToken()));
			pst.setInt(12, Integer.parseInt(token.nextToken()));
			pst.setInt(13, Integer.parseInt(token.nextToken()));
			pst.setInt(14, Integer.parseInt(token.nextToken()));
			pst.setInt(15, Integer.parseInt(token.nextToken()));
			pst.setInt(16, Integer.parseInt(token.nextToken()));
			pst.setInt(17, Integer.parseInt(token.nextToken()));
			pst.setInt(18, Integer.parseInt(token.nextToken()));
			pst.setInt(19, Integer.parseInt(token.nextToken()));
			pst.setInt(20, Integer.parseInt(token.nextToken()));
			pst.setInt(21, Integer.parseInt(token.nextToken()));
			pst.setInt(22, Integer.parseInt(token.nextToken()));

			pst.executeUpdate(); //Ejecuta los cambios
			linea=br.readLine();
		}
	}
	
	
	

}
