package context;



import Business.Threads.CheckingInterestInOrder;
import Model.Beans.Orders;
import Model.Beans.State;
import Model.DAOs.OrdersDAO;
import Model.DAOs.StateDAO;
import Model.Exceptions.NotFoundException;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


@WebListener
public class MainContext implements ServletContextListener {
	public void contextInitialized(ServletContextEvent event) { 

		//Preoccuparsi di far partire un thread che si occupa di eliminare tutte le sottoscrizioni (Nel Bus di Servizio) derivanti da contratti scaduti


		ServletContext context = event.getServletContext();
		MysqlDataSource ds = null;
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			//ds = (DataSource) envCtx.lookup("jdbc/ShApp");
			ds = new MysqlDataSource();
			ds.setDatabaseName(System.getenv("dbName"));
			ds.setUser(System.getenv("dbUser"));
			ds.setPassword(System.getenv("dbPass"));
			ds.setServerName("shappdb.mysql.database.azure.com");
			ds.setPort(3306);
			try{
				ds.setSslMode("REQUIRED");
			}catch (Exception e){
				System.out.println("Error trying to set sslMode");
				e.printStackTrace();
			}
		} catch (NamingException e) {
			System.out.println(e.getMessage());
		}
		context.setAttribute("DataSource", ds);
		context.setAttribute("FunctionDomain","https://shfunctionapp-1703421188607.azurewebsites.net/api");

		OrdersDAO ordersDAO = new OrdersDAO(ds);

		Logger l = Logger.getGlobal();

		l.log(Level.INFO ,"Controllo ordini per i quali lanciare i thread...");
		try{
			ArrayList<Orders> orders = ordersDAO.retrieveNotAssigned();

			for(int i = 0; i < orders.size(); i++){
				CheckingInterestInOrder cIO = new CheckingInterestInOrder(orders.get(i), ordersDAO);
				cIO.start();
			}

		}catch (NotFoundException e){
			System.out.println("Nessun ordine non assegnato per cui lanciare un thread dedicato .");
		}catch (Exception e){
			System.out.println("Problema inatteso");
			System.out.println(e);
		}

	}

	public void contextDestroyed(ServletContextEvent event) {
		ServletContext context = event.getServletContext();

		context.removeAttribute("DataSource");
	}
}
