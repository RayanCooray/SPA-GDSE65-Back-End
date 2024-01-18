package com.ijse.gdse.spagdse65backend.db;

import com.ijse.gdse.spagdse65backend.dto.CustomerDto;
import com.ijse.gdse.spagdse65backend.dto.ItemDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBProcess {

    private static String SAVE_DATA = "INSERT INTO item (Item_Code,Item_Description,Item_Price,Qty_On_Hand) VALUES (?, ?, ?,?)";
    private static String SAVE_Cus = "INSERT INTO customer (Customer_Id,Customer_F_Name,Customer_Address,Customer_Salary) VALUES (?, ?, ?,?)";


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


    //search Customer
    public void searchCustomer(){

    }

    //get Customer
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


    //get All Customers
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



    //get All Customers
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

    //get item
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
}
