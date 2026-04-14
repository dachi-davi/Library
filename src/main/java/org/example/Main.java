package org.example;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import repository.DatabaseManager;
import servlet.BookServlet;
import servlet.BorrowingServlet;
import servlet.MemberServlet;

import java.io.File;

public class Main {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("server");
        tomcat.setPort(8080);


        String docBase = new File("src/main/webapp").getAbsolutePath();

        Context context = tomcat.addContext("", docBase);


        Tomcat.addServlet(context, "default",
                "org.apache.catalina.servlets.DefaultServlet");
        context.addServletMappingDecoded("/", "default");


        Tomcat.addServlet(context, "BookServlet", new BookServlet());
        context.addServletMappingDecoded("/books/*", "BookServlet");


        Tomcat.addServlet(context, "MemberServlet", new MemberServlet());
        context.addServletMappingDecoded("/members/*", "MemberServlet");

        Tomcat.addServlet(context, "BorrowingServlet", new BorrowingServlet());
        context.addServletMappingDecoded("/borrow/*", "BorrowingServlet");

        tomcat.start();

        tomcat.getConnector();
        System.out.println("books http://localhost:8080/books.html");
        System.out.println("members http://localhost:8080/members.html");
        System.out.println("borrow http://localhost:8080/borrow.html");

        tomcat.getServer().await();
    }

}