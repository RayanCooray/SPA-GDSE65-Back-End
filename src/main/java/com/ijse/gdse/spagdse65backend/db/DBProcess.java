package com.ijse.gdse.spagdse65backend.db;

import com.ijse.gdse.spagdse65backend.dto.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBProcess {

    private static String SAVE_DATA = "INSERT INTO item (Item_Code,Item_Description,Item_Price,Qty_On_Hand) VALUES (?, ?, ?,?)";
    private static String SAVE_Cus = "INSERT INTO customer (Customer_Id,Customer_F_Name,Customer_Address,Customer_Salary) VALUES (?, ?, ?,?)";



    ////////////////Customer 👇👨🏻‍🦰👩🏻‍👇///////////////////////////////
    //save Customer
    public boolean savecustomer(CustomerDto customerDto, Connection connection) {
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_Cus);
            preparedStatement.setString(1, customerDto.getCustomer_id());
            preparedStatement.setString(2, customerDto.getName());
            preparedStatement.setString(3, customerDto.getAddress());
            preparedStatement.setString(4, String.valueOf(customerDto.getSalary()));
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows != 0) {
                System.out.println("Data Saved");
            } else {
                System.out.println("Data Not Saved");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    //getCustomer
    public CustomerDto getCustomer(String customerId, Connection connection) {
        String get_customer = "select * from customer where Customer_Id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(get_customer);
            preparedStatement.setString(1,customerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                System.out.println(customerId);
                return new CustomerDto(
                        resultSet.getString("Customer_Id"),
                        resultSet.getString("Customer_F_Name"),
                        resultSet.getString("Customer_Address"),
                        resultSet.getDouble("Customer_Salary")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    //get ALL Customer
    public ArrayList<CustomerDto> getAllCustomer(Connection connection){
        String get_all_customer = "select * from customer;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(get_all_customer);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<CustomerDto> customerDTOS = new ArrayList<>();
            while (resultSet.next()){
                CustomerDto customerDTO = new CustomerDto(
                        resultSet.getString("Customer_Id"),
                        resultSet.getString("Customer_F_Name"),
                        resultSet.getString("Customer_Address"),
                        resultSet.getDouble("Customer_Salary")
                );
                customerDTOS.add(customerDTO);
            }
            return customerDTOS;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //generate customer id
    public String generateCustomerId(Connection connection) {
        String get_last_customer_id = "SELECT MAX(Customer_Id) as last_customer_id FROM customer;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(get_last_customer_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String lastCustomerId = resultSet.getString("last_customer_id");
                if (lastCustomerId == null) {
                    return "C-0001";
                } else {
                    int nextId = Integer.parseInt(lastCustomerId.substring(5)) + 1;
                    return "C-" + String.format("%04d", nextId);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

    //update customer
    public boolean updateCustomer(CustomerDto customerDTO, Connection connection) {
        String update_customer = "update customer set Customer_F_Name = ?, Customer_Address =?, Customer_Salary = ? where Customer_Id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(update_customer);
            preparedStatement.setString(1,customerDTO.getName());
            preparedStatement.setString(2,customerDTO.getAddress());
            preparedStatement.setString(3, String.valueOf(customerDTO.getSalary()));
            preparedStatement.setString(4,customerDTO.getCustomer_id());

            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }

    }

    //delete customer
    public boolean deleteCustomer(Connection connection, String customerId) {
        String delete_customer = "delete from customer where Customer_Id=?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(delete_customer);
            preparedStatement.setString(1,customerId);

            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }

    }









        //////ITEM 👇🛍️🛍️🛍️🛍️🛍️🛍️🛍️🛍️🛍️👇////////////////////
    //Save Item
    public boolean saveitem(ItemDto itemDTO, Connection connection) {
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_DATA);
            preparedStatement.setString(1, itemDTO.getItem_code());
            preparedStatement.setString(2, itemDTO.getDescription());
            preparedStatement.setString(3, String.valueOf(itemDTO.getUnit_price()));
            preparedStatement.setString(4, String.valueOf(itemDTO.getQty()));
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows != 0) {
                System.out.println("Data Saved");
            } else {
                System.out.println("Data Not Saved");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    //get alll item
    public ArrayList<ItemDto> getAllItem(Connection connection) {
        String get_all_item = "select * from item;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(get_all_item);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<ItemDto> itemDtos = new ArrayList<>();
            while (resultSet.next()){
                ItemDto itemDto = new ItemDto(
                        resultSet.getString("Item_Code"),
                        resultSet.getString("Item_Description"),
                        resultSet.getDouble("Item_Price"),
                        resultSet.getInt("Qty_On_Hand")
                );
                itemDtos.add(itemDto);
            }
            return itemDtos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //get id
    public ItemDto getItem(String itemCodeValue, Connection connection) {
        String get_customer = "select * from item where Item_Code = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(get_customer);
            preparedStatement.setString(1,itemCodeValue);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                System.out.println(itemCodeValue);
                return new ItemDto(
                        resultSet.getString("Item_Code"),
                        resultSet.getString("Item_Description"),
                        resultSet.getDouble("Item_Price"),
                        resultSet.getInt("Qty_On_Hand")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    //generate item id
    public String generateItemID(Connection connection) {
        String get_last_item_id = "SELECT MAX(Item_Code) as last_item_id FROM item;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(get_last_item_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String lastItemId = resultSet.getString("last_item_id");
                if (lastItemId == null) {
                    return "IT-0001";
                } else {
                    int nextId = Integer.parseInt(lastItemId.substring(5)) + 1;
                    return "IT-" + String.format("%04d", nextId);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    //update item
    public boolean updateItem(ItemDto itemDto, Connection connection) {
        String update_item = "update item set Item_Description = ?, Item_Price =?, Qty_On_Hand = ? where Item_Code = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(update_item);
            preparedStatement.setString(1,itemDto.getDescription());
            preparedStatement.setString(2, String.valueOf(itemDto.getUnit_price()));
            preparedStatement.setString(3, String.valueOf(itemDto.getQty()));
            preparedStatement.setString(4,itemDto.getItem_code());

            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }

    //delete item
    public boolean deleteitem(Connection connection, String itemCode) {
        String delete_customer = "delete from item where Item_Code=?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(delete_customer);
            preparedStatement.setString(1,itemCode);

            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }








    /////////👇📦📦📦📦📦📦📦📦📦📦ORDERS📦📦📦📦📦📦📦📦📦📦👇///////
    //generate oder id
    public String generateOderID(Connection connection) {
        String get_oder_item_id = "SELECT MAX(Oder_ID) as last_oder_id FROM oder;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(get_oder_item_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String lastItemId = resultSet.getString("last_oder_id");
                if (lastItemId == null) {
                    return "OP-0001";
                } else {
                    int nextId = Integer.parseInt(lastItemId.substring(5)) + 1;
                    return "OP-" + String.format("%04d", nextId);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    private boolean save(OrderDTO orderDTO, Connection connection) throws SQLException {
        String save_item = "INSERT INTO Orders (order_date, order_id, customer_id, total, discount, cash) VALUES  (?,?,?,?,?,?);";

        var preparedStatement = connection.prepareStatement(save_item);
        preparedStatement.setDate(1, Date.valueOf(orderDTO.getOrder_date()));
        preparedStatement.setString(2, orderDTO.getOrder_id());
        preparedStatement.setString(3, orderDTO.getCustomer_id());
        preparedStatement.setDouble(4, orderDTO.getTotal());
        preparedStatement.setDouble(5, orderDTO.getDiscount());
        preparedStatement.setDouble(6, orderDTO.getCash());

        boolean result = preparedStatement.executeUpdate() != 0;
        if (result) {
//            logger.info("Order information saved successfully: {}", orderDTO.getOrder_id());
        } else {
//            logger.error("Failed to save order information: {}", orderDTO.getOrder_id());
        }
        return result;
    }

    public boolean saveOrder(CombinedOrderDTO combinedOrderDTO, Connection connection) {
        try {
            connection.setAutoCommit(false);

            if (save(combinedOrderDTO.getOrderDTO(), connection)) {

                for (OrderDetailsDTO orderDetailsDTO : combinedOrderDTO.getOrderDetailsDTOS()) {
                    boolean isSavedOrderDetails = new DBProcess().saveOrderDetails(orderDetailsDTO, connection);

                    if (isSavedOrderDetails) {
                        boolean isSavedItemDetails = new DBProcess().updateItemOrder(orderDetailsDTO, connection);

                        if (!isSavedItemDetails) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }

                connection.commit();
                return true;
            }

            return false;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                throw new RuntimeException(rollbackException);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean updateItemOrder(OrderDetailsDTO orderDetailsDTO, Connection connection) {
        try {
            String updateItemQtyQuery = "UPDATE Item SET Qty_On_Hand = Qty_On_Hand - ? WHERE Item_Code = ?;";

            PreparedStatement updateItemQtyStatement = connection.prepareStatement(updateItemQtyQuery);
            updateItemQtyStatement.setInt(1, orderDetailsDTO.getQty());
            updateItemQtyStatement.setString(2, orderDetailsDTO.getItem_Code());

            boolean result = updateItemQtyStatement.executeUpdate() != 0;
            if (result) {
//                logger.info("Item quantity updated successfully for order: {}", orderDetailsDTO.getOrder_id());
            } else {
//                logger.error("Failed to update item quantity for order: {}", orderDetailsDTO.getOrder_id());
            }
            return result;

        } catch (SQLException e) {
//            logger.error("Error updating item quantity for order", e);
            throw new RuntimeException(e);
        }
    }

    private boolean saveOrderDetails(OrderDetailsDTO orderDetailsDTO, Connection connection) {
        try {
            String saveItem = "INSERT INTO OrderDetails (order_id, Item_Code, price, qty) VALUES (?, ?, ?, ?);";

            try (PreparedStatement preparedStatement = connection.prepareStatement(saveItem)) {
                preparedStatement.setString(1, orderDetailsDTO.getOrder_id());
                preparedStatement.setString(2, orderDetailsDTO.getItem_Code());
                preparedStatement.setDouble(3, orderDetailsDTO.getPrice());
                preparedStatement.setInt(4, orderDetailsDTO.getQty());

                boolean result = preparedStatement.executeUpdate() != 0;
                if (result) {
//                    logger.info("Order details saved successfully: OrderID={}, ItemID={}", orderDetailsDTO.getOrder_id(), orderDetailsDTO.getItem_id());
                } else {
//                    logger.error("Failed to save order details: OrderID={}, ItemID={}", orderDetailsDTO.getOrder_id(), orderDetailsDTO.getItem_id());
                }
                return result;
            }
        } catch (SQLException e) {
//            logger.error("Error saving order details", e);
            throw new RuntimeException(e);
        }
    }

    public boolean delete(String orderId, Connection connection) {
        try {
            connection.setAutoCommit(false);

            ArrayList<OrderDetailsDTO> orderDetailsDTOS = new DBProcess().getOrderDetails(orderId, connection);

            for (OrderDetailsDTO orderDetailsDTO : orderDetailsDTOS) {
                orderDetailsDTO.setQty(-orderDetailsDTO.getQty());
                if (!new DBProcess().updateItemOrder(orderDetailsDTO, connection)) {
                    return false;
                }
            }

            if (new DBProcess().deleteOrderDetails(orderId, connection)) {
                if (deleteOrder(orderId, connection)) {
                    connection.commit();
                    return true;
                }
            }

            connection.rollback();
            return false;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                throw new RuntimeException(rollbackException);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ArrayList<OrderDetailsDTO> getOrderDetails(String orderId, Connection connection) {
        ArrayList<OrderDetailsDTO> orderDetailsList = new ArrayList<>();

        try {
            String getOrderDetailsQuery = "SELECT * FROM OrderDetails WHERE order_id = ?;";

            try (PreparedStatement getOrderDetailsStatement = connection.prepareStatement(getOrderDetailsQuery)) {
                getOrderDetailsStatement.setString(1, orderId);
                try (ResultSet resultSet = getOrderDetailsStatement.executeQuery()) {
                    while (resultSet.next()) {
                        OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO();
                        orderDetailsDTO.setOrder_id(resultSet.getString("order_id"));
                        orderDetailsDTO.setItem_Code(resultSet.getString("item_id"));
                        orderDetailsDTO.setPrice(resultSet.getDouble("price"));
                        orderDetailsDTO.setQty(resultSet.getInt("qty"));
                        orderDetailsList.add(orderDetailsDTO);
                    }
                }
            }
            return orderDetailsList;

        } catch (SQLException e) {
//            logger.error("Error retrieving order details", e);
            throw new RuntimeException(e);
        }
    }

    private boolean deleteOrderDetails(String orderId, Connection connection) {
        try {
            String deleteOrderDetailsQuery = "DELETE FROM OrderDetails WHERE order_id = ?;";

            try (PreparedStatement deleteOrderDetailsStatement = connection.prepareStatement(deleteOrderDetailsQuery)) {
                deleteOrderDetailsStatement.setString(1, orderId);

                boolean result = deleteOrderDetailsStatement.executeUpdate() != 0;
                if (result) {
                } else {
                }
                return result;
            }
        } catch (SQLException e) {
//            logger.error("Error deleting order details", e);
            throw new RuntimeException(e);
        }
    }

    private boolean deleteOrder(String orderId, Connection connection) throws SQLException {
        String deleteOrderQuery = "DELETE FROM Orders WHERE order_id = ?;";
        PreparedStatement deleteOrderStatement = connection.prepareStatement(deleteOrderQuery);
        deleteOrderStatement.setString(1, orderId);

        boolean result = deleteOrderStatement.executeUpdate() != 0;
        if (result) {
//            logger.info("Order information deleted successfully: {}", orderId);
        } else {
//            logger.error("Failed to delete order information: {}", orderId);
        }
        return result;
    }

    public CombinedOrderDTO getOrder(String orderId, Connection connection) {
        try {
            String getOrderQuery = "SELECT * FROM Orders WHERE order_id = ?;";

            PreparedStatement getOrderStatement = connection.prepareStatement(getOrderQuery);
            getOrderStatement.setString(1, orderId);
            ResultSet resultSet = getOrderStatement.executeQuery();

            OrderDTO orderDTO = new OrderDTO();

            if (resultSet.next()) {
                orderDTO.setOrder_date(String.valueOf(resultSet.getDate("order_date")));
                orderDTO.setOrder_id(resultSet.getString("order_id"));
                orderDTO.setCustomer_id(resultSet.getString("customer_id"));
                orderDTO.setTotal(resultSet.getDouble("total"));
                orderDTO.setDiscount(resultSet.getDouble("discount"));
                orderDTO.setCash(resultSet.getDouble("cash"));
            }

            ArrayList<OrderDetailsDTO> orderDetailsDTOS = new DBProcess().getOrderDetails(orderId, connection);

            return new CombinedOrderDTO(orderDTO, orderDetailsDTOS);

        } catch (SQLException e) {
//            logger.error("Error retrieving order information", e);
            throw new RuntimeException(e);
        }
    }

    public List<CombinedOrderDTO> getAllOrders(Connection connection) {
        try {
            String getOrderQuery = "SELECT * FROM Orders;";

            PreparedStatement getOrderStatement = connection.prepareStatement(getOrderQuery);
            ResultSet resultSet = getOrderStatement.executeQuery();

            ArrayList<CombinedOrderDTO> combinedOrderDTOS = new ArrayList<>();

            while (resultSet.next()) {
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setOrder_date(String.valueOf(resultSet.getDate("order_date")));
                orderDTO.setOrder_id(resultSet.getString("order_id"));
                orderDTO.setCustomer_id(resultSet.getString("customer_id"));
                orderDTO.setTotal(resultSet.getDouble("total"));
                orderDTO.setDiscount(resultSet.getDouble("discount"));
                orderDTO.setCash(resultSet.getDouble("cash"));
                ArrayList<OrderDetailsDTO> orderDetailsDTOS = new DBProcess().getOrderDetails(orderDTO.getOrder_id(), connection);
                combinedOrderDTOS.add(new CombinedOrderDTO(orderDTO, orderDetailsDTOS));
            }

//            logger.info("Retrieved all orders successfully");
            return combinedOrderDTOS;

        } catch (SQLException e) {
//            logger.error("Error retrieving all orders", e);
            throw new RuntimeException(e);
        }
    }

    public boolean updateOrder(CombinedOrderDTO combinedOrderDTO, Connection connection) {
        try {
            connection.setAutoCommit(false);

            ArrayList<OrderDetailsDTO> orderDetailsDTOS = new DBProcess().getOrderDetails(
                    combinedOrderDTO.getOrderDTO().getOrder_id(), connection);

            for (OrderDetailsDTO orderDetailsDTO : orderDetailsDTOS) {
                orderDetailsDTO.setQty(-orderDetailsDTO.getQty());
                if (!new DBProcess().updateItemOrder(orderDetailsDTO, connection)) {
                    return false;
                }
            }

            if (update(combinedOrderDTO.getOrderDTO(), connection)) {

                for (OrderDetailsDTO orderDetailsDTO : combinedOrderDTO.getOrderDetailsDTOS()) {
                    boolean isUpdatedOrderDetails = new DBProcess().updateOrderDetails(orderDetailsDTO, connection);

                    if (isUpdatedOrderDetails) {
                        boolean isUpdatedItemDetails = new DBProcess().updateItemOrder(orderDetailsDTO, connection);

                        if (!isUpdatedItemDetails) {
                            return false;
                        }

                    } else {
                        return false;
                    }
                }

                connection.commit();
                return true;
            }

            return false;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                throw new RuntimeException(rollbackException);
            }
//            logger.error("Error updating order", e);
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean updateOrderDetails(OrderDetailsDTO orderDetailsDTO, Connection connection) {
        try {
            String updateItem = "UPDATE OrderDetails SET price=?, qty=? WHERE order_id=? AND Item_Code=?;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateItem)) {
                preparedStatement.setDouble(1, orderDetailsDTO.getPrice());
                preparedStatement.setInt(2, orderDetailsDTO.getQty());
                preparedStatement.setString(3, orderDetailsDTO.getOrder_id());
                preparedStatement.setString(4, orderDetailsDTO.getItem_Code());

                boolean result = preparedStatement.executeUpdate() != 0;
                if (result) {
//                    logger.info("Order details updated successfully: OrderID={}, ItemID={}", orderDetailsDTO.getOrder_id(), orderDetailsDTO.getItem_id());
                } else {
//                    logger.error("Failed to update order details: OrderID={}, ItemID={}", orderDetailsDTO.getOrder_id(), orderDetailsDTO.getItem_id());
                }
                return result;
            }
        } catch (SQLException e) {
//            logger.error("Error updating order details", e);
            throw new RuntimeException(e);
        }
    }

    private boolean update(OrderDTO orderDTO, Connection connection) throws SQLException {
        String update_item = "UPDATE Orders SET order_date=?, customer_id=?, total=?, discount=?, cash=? WHERE order_id=?;";

        var preparedStatement = connection.prepareStatement(update_item);
        preparedStatement.setDate(1, Date.valueOf(orderDTO.getOrder_date()));
        preparedStatement.setString(2, orderDTO.getCustomer_id());
        preparedStatement.setDouble(3, orderDTO.getTotal());
        preparedStatement.setDouble(4, orderDTO.getDiscount());
        preparedStatement.setDouble(5, orderDTO.getCash());
        preparedStatement.setString(6, orderDTO.getOrder_id());

        boolean result = preparedStatement.executeUpdate() != 0;
        if (result) {
//            logger.info("Order information updated successfully: {}", orderDTO.getOrder_id());
        } else {
//            logger.error("Failed to update order information: {}", orderDTO.getOrder_id());
        }
        return result;
    }
}
