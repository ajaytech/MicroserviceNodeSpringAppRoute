package com.abcComp.hr.lms.code.businesslogic;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.abcComp.hr.lms.code.entities.Student;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.saj.InvalidSyntaxException;

import java.sql.Connection;


@Component
public class StudentOperation {

	public Connection conn;
	public Statement stmt;
	public ResultSet rs;
	

    String url;
    String user;
    String password;
	
    
    
	@PostConstruct
	public void startConnection() throws SQLException {
		
		this.url = "";
		this.user = "";
		this.password = "";
		
		try {
			
			String vcap_service = System.getenv("VCAP_SERVICES");
			
			if(vcap_service != null && vcap_service.length() >0 ) {
			
			JsonNode root = new JdomParser().parse(vcap_service);
			JsonNode serviceRoot = root.getNode("hanatrial");
			JsonNode serviceCreden = serviceRoot.getNode(0).getNode("credentials");
			
			this.url = serviceCreden.getStringValue("url");
			this.user = serviceCreden.getStringValue("user");
			this.password = serviceCreden.getStringValue("password");
			
			}
			
		} catch (InvalidSyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("!!Error in getting  VCAP_SERVICES Prop!!");
		}
		
		
//		this.url = "jdbc:sap://zeus.hana.prod.eu-central-1.whitney.dbaas.ondemand.com:23803?encrypt=true&validateCertificate=true&currentschema=FD7F1B5BF4114966A7EB062B198A6E9A";
//		this.user = "FD7F1B5BF4114966A7EB062B198A6E9A_13L11V4JJM5GB1YWRGE056T3X_RT";
//		this.password = "Eo1qeGRlYPkryXktTrrRib9XTXc1sTVyNgkE0lITUisZWVBzfvUaqDXCtU3aDB22AEc4mCeX5332ClpKliEGTSSVN5fP30HCU9WoBRrjhtXkZ0dlEseBoyYwisTjMhRW";
//		
		try {
		conn = DriverManager.getConnection(this.url,this.user,this.password);
		stmt = conn.createStatement();
		}catch(SQLException e) {
			System.out.println("!!Error in DB Conn!!");
			System.out.println(e.getMessage());
		}
		
		
	}
	
	//What this method does
	public List<Student> giveAllStudent() throws SQLException{
		
		List<Student> studentList = new ArrayList<Student>();
		
		rs = stmt.executeQuery("Select top 100 * from Student");
		
		while(rs.next()) {
			
			studentList.add(new Student(rs.getInt("ID"),
										rs.getString("FIRSTNAME"),
										rs.getString("LASTNAME"),
										rs.getString("LOCATION_STUDENT")));
		}
		return studentList;
	}
	
	
	
	//What this method does
		public Student giveStudent(int id) throws SQLException{
			
			Student studentObj = new Student(0,null,null,null);
			
			rs = stmt.executeQuery("Select * from Student where id ='"+Integer.toString(id)+"'" );
			
			while(rs.next()) {
				
				studentObj.id = rs.getInt("ID");
				studentObj.firstName = rs.getString("FIRSTNAME");
				studentObj.lastName = rs.getString("LASTNAME");
				studentObj.locationStudent = rs.getString("LOCATION_STUDENT");
											
			
		}
			return studentObj;
		}
		
	
	
	@PreDestroy
	public void endConnection() throws SQLException {
		
		rs.close();
		stmt.close();
		conn.close();
		
			
	}
	
}
