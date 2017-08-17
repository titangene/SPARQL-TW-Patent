package SPARQL;

import java.io.InputStream;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.json.JSONObject;
import org.json.XML;

public class JenaQuery {
	private static Model model;
	final static String OWL_File = "./owl/patent_ontology_r.owl";
	final static String OWL_Base = "http://www.owl-ontologies.com/Ontology1502685072.owl#";
	final static String queryString = "prefix base: <" + OWL_Base + ">"
			+ "SELECT *"
			+ "WHERE {"
			+ "?專利編號 base:發明人為 ?發明人."
			+ "}";
	
	public static void main(String[] args) {
		model = loadOntology(OWL_File);
		String result = execQuery_getJSON(queryString);
		System.out.println(result);
	}
	
	public static String execQuery_getJSON(String sparql){
		Query query = QueryFactory.create(sparql);
		QueryExecution queryExce = QueryExecutionFactory.create(query, model);
		
		try {
			ResultSet resultset = queryExce.execSelect();			
			String resultSetFormatter = ResultSetFormatter.asXMLString(resultset);
			
			JSONObject jsonResult = XML.toJSONObject(resultSetFormatter);
//			return jsonResult.toString(2);	// json beautify
			return jsonResult.toString();
		} catch(Exception e) {
			queryExce.close();
		}
		return "";
	}
	
	public static Model loadOntology(String fileName) {
		// Create an empty model
		Model model = ModelFactory.createDefaultModel();
		// use the FileManager to find the input file
		InputStream in = FileManager.get().open(fileName);
		if (in == null) {
		    throw new IllegalArgumentException("File: " + fileName + " not found");
		}
		// read the RDF/XML file
		model.read(in, null);
		System.out.println("Ontology " + fileName + " loaded.");
		return model;
	}
}
