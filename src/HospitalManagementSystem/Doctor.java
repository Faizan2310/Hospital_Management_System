package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Doctor {
    private Connection connection;


    public Doctor(Connection connection){
        this.connection = connection;
    }


    public void viewDoctors(){
        try{
            String query = "select * from doctors";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                System.out.println("| Doctor Id: " + id + " | " + "Doctor name: " + name + " | " + "Specialization: " + specialization + " | ");
                System.out.println(" ");
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean checkDoctorExist(int id){
        try{
            String query = "select * from doctors where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else {
                return false;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

}

