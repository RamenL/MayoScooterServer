package com.mayo.server;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import com.mayo.handler.DAOHandler;

public class MayoWebServer {
	private static String RESOURCES_BASE = "src/main/resources";
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("Usage: MayoWebServer port");
			System.exit(-1);
		}
		try {
			Server server = new Server();
			ServerConnector connector = new ServerConnector(server);
			connector.setPort(Integer.parseInt(args[0]));
			server.setConnectors(new Connector[] {connector});
			ResourceHandler resource_handler = new ResourceHandler();
			resource_handler.setDirectoriesListed(true);
			resource_handler.setWelcomeFiles(new String[] {"index.html"});
			resource_handler.setResourceBase(RESOURCES_BASE);
			HandlerList handlers = new HandlerList();
			handlers.setHandlers(new Handler[] { resource_handler, new DAOHandler() });//resouce_handler, ExampleHandler
			server.setHandler(handlers);
			server.start();
			server.join();
			
		} catch (Exception e){
			e.printStackTrace();
		}


	}

}
