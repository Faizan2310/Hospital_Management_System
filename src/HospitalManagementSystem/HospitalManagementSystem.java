package HospitalManagementSystem;

import com.mysql.cj.jdbc.Driver;

import javax.print.Doc;
import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "root";

   public static void main(String [] args){
       try{
           Class.forName("com.mysql.cj.jbdc.Driver");
       }
       catch (ClassNotFoundException e){
           e.printStackTrace();
       }
       Scanner scanner = new Scanner(System.in);
       try{
           Connection connection = DriverManager.getConnection(url, username, password);
           Patient patient = new Patient(connection, scanner);
           Doctor doctor = new Doctor(connection);

           while (true){
               System.out.println("<<<--- HOSPITAL MANAGEMENT SYSTEM --->>>");
               System.out.println("1. Add Patient");
               System.out.println("2. View Patients");
               System.out.println("3. View Doctors");
               System.out.println("4. Book Appointment");
               System.out.println("5. Exit");
               System.out.print("Enter your choice: ");
               int choice = scanner.nextInt();

               switch (choice){
                   case 1:
                       //Add Patient
                       patient.addPatient();
                   case 2:
                       // View Patients
                       patient.viewPatients();
                   case 3:
                       // View Doctors
                       doctor.viewDoctors();
                   case 4:
                       // Book Appointment
                       bookAppointment(patient, doctor, connection, scanner);
                   case 5:
                       break;
                   default:
                       System.out.println("Enter a Valid option");
               }
           }

       }
       catch(SQLException e){
           e.printStackTrace();
       }
   }

   public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner){
       System.out.print("Enter Patient Id: ");
       int patientId = scanner.nextInt();
       System.out.print("Enter Doctor Id: ");
       int doctorId = scanner.nextInt();
       System.out.print("Enter appointment date (YYYY-MM-DD): ");
       String appointmentDate = scanner.next();
        if(patient.checkPatientExist(patientId) && doctor.checkDoctorExist(doctorId)){
            if(checkDoctorAvailability(doctorId, appointmentDate, connection)){
                String query = "insert into appointments(patient_id, doctor_id, appointment_date) values (? , ?, ?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("Appointment Booked");
                    }
                    else {
                        System.out.println("Failed to book appointment");
                    }
                }
                catch (SQLException e){
                    e.printStackTrace();
                }


            }else {
                System.out.println("Doctor not Available on this date :( ");
            }
        }else {
            System.out.println("Either doctor or patient does not exist");
        }
   }

   public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection){
       String query = "select count(*) from appoitments where doctor_id = ? and appointment_date = ?";
       try{
           PreparedStatement preparedStatement = connection.prepareStatement(query);
           preparedStatement.setInt(1,doctorId);
           preparedStatement.setString(2, appointmentDate);
           ResultSet resultSet = preparedStatement.executeQuery();
           if(resultSet.next()){
               int count = resultSet.getInt(1);
               if(count == 0){
                   return true;
               }
               else {
                   return false;
               }
           }

       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
       return false;
   }
}
