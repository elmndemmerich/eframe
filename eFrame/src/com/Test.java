package com;

import eFrame.server.Server;

/**
 * 
 * @author LiangRL
 * @alias E.E.
 */
public class Test {

	public static void main(String[] args) throws Exception {
		Server server = Server.getInstance();
		server.start();	
	}
}
