package com.ijse.gdse.spagdse65backend.api;

import com.ijse.gdse.spagdse65backend.db.DBProcess;
import com.ijse.gdse.spagdse65backend.dto.CustomerDto;
import com.ijse.gdse.spagdse65backend.dto.ItemDto;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(name = "Customer",urlPatterns = "/customer",
        initParams = {
                @WebInitParam(name = "db-user",value = "root"),
                @WebInitParam(name = "db-pw",value = "1234"),
                @WebInitParam(name = "db-url",value = "jdbc:mysql://localhost:3306/spagdse65"),
                @WebInitParam(name = "db-class",value= "com.mysql.cj.jdbc.Driver")
        }
        ,loadOnStartup = 5
)
public class Customer extends HttpServlet {
    Connection connection;



    @Override
    public void init() throws ServletException {
        String userName = getServletConfig().getInitParameter("db-user");
        String password = getServletConfig().getInitParameter("db-pw");
        String url = getServletConfig().getInitParameter("db-url");



        try {
            Class.forName(getServletConfig().getInitParameter("db-class"));
            this.connection = DriverManager.getConnection(url,userName,password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json"))
        {resp.sendError(HttpServletResponse.SC_BAD_REQUEST);}
        else {
            Jsonb jsonb = JsonbBuilder.create();
            CustomerDto customerDto = jsonb.fromJson(req.getReader(), CustomerDto.class);
            DBProcess dbProcess = new DBProcess();
            boolean issaved = dbProcess.savecustomer(customerDto, connection);
            PrintWriter writer = resp.getWriter();
            resp.setContentType("text/html");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action != null){
            if (action.equals("generateCustomerId")) {
                generateCustomerId(req, resp);
            }else if (action.equals(("getAllCustomer"))){
                getAllCustomer(req,resp);
            }else if (action.equals("getCustomer")){
                String customerId = req.getParameter("customerId");
                getCustomer(req, resp, customerId);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() != null && req.getContentType().toLowerCase().startsWith("application/json")){
            Jsonb jsonb = JsonbBuilder.create();
            CustomerDto customerDTO = jsonb.fromJson(req.getReader(), CustomerDto.class);

            var dbProcess = new DBProcess();
            boolean result = dbProcess.updateCustomer(customerDTO, connection);

            if (result){
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Customer information updated successfully.");
            }else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Failed to update customer information.");
            }

        }else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var customerId = req.getParameter("customerIdValue");
        var dbProcess = new DBProcess();
        boolean result = dbProcess.deleteCustomer(connection, customerId);
        if (result) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Student information delete successfully.");
        } else {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete student information.");

        }

    }


    private void getCustomer(HttpServletRequest req, HttpServletResponse resp, String customerId) {
        var dbProcess = new DBProcess();
        CustomerDto customerDTO = dbProcess.getCustomer(customerId, connection);
        Jsonb jsonb = JsonbBuilder.create();
        try {
            var json = jsonb.toJson(customerDTO);
            resp.setContentType("application/json");
            resp.getWriter().write(json);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }

    public void getAllCustomer(HttpServletRequest req, HttpServletResponse resp){
        DBProcess customerDB = new DBProcess();
        ArrayList<CustomerDto> allCustomer = customerDB.getAllCustomer(connection);
        System.out.println(allCustomer==null);
        Jsonb jsonb = JsonbBuilder.create();
        var json = jsonb.toJson(allCustomer);
        resp.setContentType("application/json");
        try {
            resp.getWriter().write(json);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }

    private void generateCustomerId(HttpServletRequest req, HttpServletResponse resp){
        var customerDb = new DBProcess();
        String getLastCustomerId = customerDb.generateCustomerId(connection);

        Jsonb jsonb = JsonbBuilder.create();
        String json = jsonb.toJson(getLastCustomerId);
        resp.setContentType("application/json");
        try {
            resp.getWriter().write(json);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }

    }
}
