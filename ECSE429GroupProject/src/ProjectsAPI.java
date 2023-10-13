import org.json.simple.JSONObject;

public class ProjectsAPI {
	public static String baseURL = "http://localhost:4567/projects";
	public ProjectsAPI() {
	}
	
	//TODO NEED TO PERFORM HTTP REQUEST IN THE FUNCTIONS BELOW
	
	//--->in eclipse can do "run as JUnit test"
	
	public int GET() {
		//uses baseURL
		//TODO
		return 0;
	}
	
	public JSONObject GET(int id) {
		//baseURL/{id}
		//TODO
		return null;
	}
	
	public JSONObject POST(JSONObject j) {
		//uses baseURL
		//TODO
		return null;
	}
	public JSONObject POST(JSONObject j, int id) {
		//baseURL/{id}
		//TODO
		return null;
	}
	
	
}
