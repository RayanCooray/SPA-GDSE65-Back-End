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
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Item",urlPatterns = "/item",
        initParams = {
                @WebInitParam(name = "db-user",value = "root"),
                @WebInitParam(name = "db-pw",value = "1234"),
                @WebInitParam(name = "db-url",value = "jdbc:mysql://localhost:3306/spagdse65"),
                @WebInitParam(name = "db-class",value= "com.mysql.cj.jdbc.Driver")
        }
        ,loadOnStartup = 5
)
public class Item extends HttpServlet {

    final static Logger LOGGER = LoggerFactory.getLogger(Item.class);

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
        LOGGER.info("Init the item service");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json"))
        {resp.sendError(HttpServletResponse.SC_BAD_REQUEST);}
        else {
            Jsonb jsonb = JsonbBuilder.create();
            ItemDto itemList = jsonb.fromJson(req.getReader(),ItemDto.class);
            DBProcess dbProcess = new DBProcess();
            boolean issaved = dbProcess.saveitem(itemList, connection);
            PrintWriter writer = resp.getWriter();
            resp.setContentType("text/html");
            resp.getWriter().write("Item Saved Successfully");
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action != null){
            if (action.equals("generateItemID")) {
                generateItemID(req, resp);
            }else if (action.equals(("getAllItem"))){
                getAllItem(req,resp);
            }
            else if (action.equals("getItem")){
                String item_code_Value = req.getParameter("item_code_Value");
                getItem(req, resp, item_code_Value);
            }
        }
    }

    private void getItem(HttpServletRequest req, HttpServletResponse resp, String itemCodeValue) {
        var dbProcess = new DBProcess();
        ItemDto itemDto = dbProcess.getItem(itemCodeValue, connection);
        Jsonb jsonb = JsonbBuilder.create();
        try {
            var json = jsonb.toJson(itemDto);
            resp.setContentType("application/json");
            resp.getWriter().write(json);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }

    private void getAllItem(HttpServletRequest req, HttpServletResponse resp) {
        DBProcess itemDB = new DBProcess();
        ArrayList<ItemDto> allitems = itemDB.getAllItem(connection);
        System.out.println(allitems==null);
        Jsonb jsonb = JsonbBuilder.create();
        var json = jsonb.toJson(allitems);
        resp.setContentType("application/json");
        try {
            resp.getWriter().write(json);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }

    private void generateItemID(HttpServletRequest req, HttpServletResponse resp){
        var itemDB = new DBProcess();
        String getLastCustomerId = itemDB.generateItemID(connection);

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

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var itemCode = req.getParameter("itemCode");
        var dbProcess = new DBProcess();
        boolean result = dbProcess.deleteitem(connection, itemCode);
        if (result) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Item information delete successfully.");
        } else {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete student information.");

        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() != null && req.getContentType().toLowerCase().startsWith("application/json")){
            Jsonb jsonb = JsonbBuilder.create();
            ItemDto itemDto = jsonb.fromJson(req.getReader(), ItemDto.class);

            var dbProcess = new DBProcess();
            boolean result = dbProcess.updateItem(itemDto, connection);

            if (result){
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Item information updated successfully.");
            }else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Failed to update customer information.");
            }

        }else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
