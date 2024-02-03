package com.ijse.gdse.spagdse65backend.api;

import com.ijse.gdse.spagdse65backend.db.DBProcess;
import com.ijse.gdse.spagdse65backend.dto.CombinedOrderDTO;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "PlaceOder",urlPatterns = "/place_oder",
        initParams = {
                @WebInitParam(name = "db-user",value = "root"),
                @WebInitParam(name = "db-pw",value = "1234"),
                @WebInitParam(name = "db-url",value = "jdbc:mysql://localhost:3306/spagdse65"),
                @WebInitParam(name = "db-class",value= "com.mysql.cj.jdbc.Driver")
        }
        ,loadOnStartup = 5
)
public class PlaceOder extends HttpServlet {

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String action = req.getParameter("action");
//        if (action != null){
//            if (action.equals("generateOderID")) {
//                generateOderId(req, resp);
//            }else if (action.equals(("getAllCustomer"))){
////                getAllCustomer(req,resp);
//            }else if (action.equals("getCustomer")){
//                String customerId = req.getParameter("customerId");
////                getCustomer(req, resp, customerId);
//            }
//        }
        String action = req.getParameter("action");

        if ("getAllOrders".equals(action)) {
            getAllOrders(req, resp);
        } else if ("getOrderId".equals(action)) {
            generateOderId(req, resp);
        } else if ("getOrder".equals(action)) {
            var orderId = req.getParameter("orderId");
            getOrder(req, resp, orderId);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
//            logger.warn("Invalid action parameter: {}", action);
        }
    }

    private void getOrder(HttpServletRequest req, HttpServletResponse resp, String orderId) {
        try {
            if (orderId != null) {
                DBProcess orderDBProcess = new DBProcess();
                CombinedOrderDTO order = orderDBProcess.getOrder(orderId, connection);
                if (order != null) {
                    Jsonb jsonb = JsonbBuilder.create();
                    String json = jsonb.toJson(order);
                    resp.setContentType("application/json");
                    resp.getWriter().write(json);
//                    logger.debug("Returned order successfully: {}", orderId);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found.");
//                    logger.warn("Order not found: {}", orderId);
                }
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing orderId parameter.");
//                logger.warn("Missing orderId parameter for getOrder");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getAllOrders(HttpServletRequest req, HttpServletResponse resp) {

        try {
            DBProcess orderDBProcess = new DBProcess();
            List<CombinedOrderDTO> allOrders = orderDBProcess.getAllOrders(connection);
            Jsonb jsonb = JsonbBuilder.create();
            String json = jsonb.toJson(allOrders);
            resp.setContentType("application/json");
            resp.getWriter().write(json);
//            logger.debug("Returned all orders successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateOderId(HttpServletRequest req, HttpServletResponse resp) {
        var oderDb = new DBProcess();
        String getoderid = oderDb.generateOderID(connection);

        Jsonb jsonb = JsonbBuilder.create();
        String json = jsonb.toJson(getoderid);
        resp.setContentType("application/json");
        try {
            resp.getWriter().write(json);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() != null && req.getContentType().toLowerCase().startsWith("application/json")) {
            Jsonb jsonb = JsonbBuilder.create();
            CombinedOrderDTO combinedOrderDTO = jsonb.fromJson(req.getReader(), CombinedOrderDTO.class);
            DBProcess orderDBProcess = new DBProcess();
            boolean result = orderDBProcess.saveOrder(combinedOrderDTO, connection);
            if (result) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Order information saved successfully.");
//                logger.info("Order information saved successfully.");
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save Order information.");
//                logger.error("Failed to save Order information.");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
//            logger.warn("Invalid request format for POST");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() != null && req.getContentType().toLowerCase().startsWith("application/json")) {
            Jsonb jsonb = JsonbBuilder.create();
            CombinedOrderDTO combinedOrderDTO = jsonb.fromJson(req.getReader(), CombinedOrderDTO.class);
            DBProcess orderDBProcess = new DBProcess();
            boolean result = orderDBProcess.updateOrder(combinedOrderDTO, connection);
            if (result) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Order information updated successfully.");
//                logger.info("Order information updated successfully.");
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update Order information.");
//                logger.error("Failed to update Order information.");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
//            logger.warn("Invalid request format for PUT");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String orderId = req.getParameter("orderId");
        if (orderId != null) {
            DBProcess orderDBProcess = new DBProcess();
            boolean result = orderDBProcess.delete(orderId, connection);
            if (result) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Order deleted successfully.");
//                logger.info("Order deleted successfully: {}", orderId);
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete order.");
//                logger.error("Failed to delete order: {}", orderId);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing orderId parameter.");
//            logger.warn("Missing orderId parameter for DELETE");
        }
    }

}
