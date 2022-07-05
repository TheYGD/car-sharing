package carsharing;

import carsharing.domain.Car;
import carsharing.domain.Company;
import carsharing.domain.Customer;
import carsharing.repo.*;

import java.sql.*;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private DbInfo dbInfo;
    private String fileName;
    private final ICompanyDao companyDao;
    private final ICustomerDao customerDao;
    private final ICarDao carDao;

    public Main(String[] args) {
        serve_clArgs(args);
        this.companyDao = new CompanyDao(dbInfo);
        this.customerDao = new CustomerDao(dbInfo);
        this.carDao = new CarDao(dbInfo);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName ("org.h2.Driver");
        Main main = new Main(args);

        main.menu();
    }

    private void serve_clArgs(String[] args) {
        final String FILENAME_TAG = "-databaseFileName";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(FILENAME_TAG) && i != args.length - 1) {
                fileName = args[++i];
            }
        }

        if (fileName == null) {
            fileName = "baza.xd";
        }

        dbInfo = new DbInfo("jdbc:h2:file:./src/carsharing/db/" + fileName);
    }

    private void menu() {
        int menu_option;
        Scanner scanner = new Scanner(System.in);

        // main loop
        while (true) {
            System.out.println("\n1. Log in as a manager\n" +
                    "2. Log in as a customer\n" +
                    "3. Create a customer\n" +
                    "0. Exit");

            menu_option = scanner.nextInt();
            switch (menu_option) {
                case 1:
                    manager_menu();
                    break;

                case 2:
                    list_customers();
                    break;

                case 3:
                    create_customer();
                    break;

                default:
                    return;
            }
        }
    }

    private void list_customers() {
        System.out.println();
        List<Customer> customers = customerDao.getAllCustomers();

        if (customers.size() == 0) {
            System.out.println("The customer list is empty!");
            return;
        }

        System.out.println("Choose a customer:");
        for (int i = 0; i < customers.size(); i++) {
            System.out.println((i+1) + ". " + customers.get(i));
        }
        System.out.println("0. Back");

        int chosen = (new Scanner(System.in)).nextInt();
        if (chosen == 0) {
            return;
        }

        if (chosen < 1 || chosen > customers.size()) {
            System.out.println(" * ERROR *\n");
            return;
        }

        customer_menu(customers.get(chosen - 1));
    }

    private void customer_menu(Customer customer) {
        while (true) {
            System.out.println("\n1. Rent a car\n" +
                    "2. Return a rented car\n" +
                    "3. My rented car\n" +
                    "0. Back");

            switch ((new Scanner(System.in)).nextInt()) {
                case 1:
                    rent_car(customer);
                    break;
                case 2:
                    return_car(customer);
                    break;
                case 3:
                    print_rented_car(customer);
                    break;
                default:
                    return;
            }
        }
    }

    private void print_rented_car(Customer customer) {
        Car car = customerDao.getCustomersCar(customer);
        if (car == null) {
            System.out.println("\nYou didn't rent a car!");
            return;
        }

        Company company = companyDao.getCompanyById(car.getCompanyId());

        System.out.println("Your rented car:\n" +
                car.getName() +
                "\nCompany:\n" +
                company.getName());
    }

    private void return_car(Customer customer) {
        if (customer.getRented_car_id() == null) {
            System.out.println("\nYou didn't rent a car!");
            return;
        }

        customer.setRented_car_id(null);
        customerDao.updateCustomersCar(customer, new Car(null, null, 0));
        System.out.println("\nYou've returned a rented car!");
    }

    private List<Car> rent_car_print_cars(Company company) {
        System.out.println();
        List<Car> cars = carDao.getAllCompanysCars(company);

        if (cars == null || cars.size() == 0) {
            System.out.println("The car list is empty!");
            return null;
        }

        Set<Integer> rentedCars = customerDao.getRentedCarsIds();

        int order = 1;
        System.out.println("Choose car:");
        for (int i = 0; i < cars.size();) {
            if (rentedCars.contains(cars.get(i).getId())) {
                cars.remove(cars.get(i));
                continue;
            }
            System.out.println((order++) + ". " + cars.get(i));
            i++;
        }

        return order != 1 ? cars : null;
    }

    private void rent_car(Customer customer) {
        if (customer.getRented_car_id() != null) {
            System.out.println("\nYou've already rented a car!");
            return;
        }

        List<Company> companies = print_companies();
        if (companies == null) {
            return;
        }

        int chosen = (new Scanner(System.in)).nextInt();
        if (chosen < 1 || chosen > companies.size()) {
            System.out.println(" * ERROR *\n");
        }

        if (chosen == 0) {
            return;
        }

        List<Car> cars = rent_car_print_cars(companies.get(chosen - 1));
        if (cars == null) {
            System.out.println("Sorry - No cars available!");
            return;
        }

        chosen = (new Scanner(System.in)).nextInt();
        if (chosen < 1 || chosen > cars.size()) {
            System.out.println(" * ERROR *\n");
        }

        if (chosen == 0) {
            return;
        }

        // chosen
        Car chosenCar = cars.get(chosen - 1);
        customerDao.updateCustomersCar(customer, chosenCar);
        customer.setRented_car_id(chosenCar.getId());
        System.out.println("\nYou rented '" + chosenCar + "'");

    }

    private void create_customer() {
        System.out.println("\nEnter the customer name:");

        String customersName = (new Scanner(System.in)).nextLine();
        customerDao.createCustomer(customersName);

        System.out.println("The customer was added!");
    }

    private void manager_menu() {
        while (true) {
            System.out.println("\n1. Company list\n" +
                    "2. Create a company\n" +
                    "0. Back");
            int menu_option = (new Scanner(System.in)).nextInt();

            switch (menu_option) {
                case 1:
                    list_companies();
                    break;

                case 2:
                    create_company();
                    break;

                default:
                    return;
            }
        }
    }

    private List<Company> print_companies() {
        System.out.println();
        List<Company> companies = companyDao.getAllCompanies();

        if (companies.size() == 0) {
            System.out.println("The company list is empty!");
            return null;
        }

        System.out.println("Choose a company:");
        for (int i = 0; i < companies.size(); i++) {
            System.out.println((i+1) + ". " + companies.get(i));
        }
        System.out.println("0. Back");

        return companies;
    }

    private void list_companies() {
        List<Company> companies = print_companies();
        if (companies == null) {
            return;
        }

        int chosen = (new Scanner(System.in)).nextInt();
        if (chosen < 1 || chosen > companies.size()) {
            System.out.println(" * ERROR *\n");
        }

        if (chosen == 0) {
            return;
        }

        company_menu(companies.get(chosen - 1));
    }

    private void company_menu(Company company) {
        while (true) {
            System.out.println("\n'" + company.getName() + "' company");
            System.out.println("1. Car list\n" +
                    "2. Create a car\n" +
                    "0. Back");

            switch ((new Scanner(System.in)).nextInt()) {
                case 1:
                    list_cars(company);
                    break;
                case 2:
                    add_car(company);
                    break;
                default:
                    return;
            }
        }
    }

    private void list_cars(Company company) {
        System.out.println();
        List<Car> cars = carDao.getAllCompanysCars(company);

        if (cars.size() == 0) {
            System.out.println("The car list is empty!");
            return;
        }

        System.out.println("Car list:");
        for (int i = 0; i < cars.size(); i++) {
            System.out.println((i+1) + ". " + cars.get(i));
        }
    }

    private void add_car(Company company) {
        System.out.println("\nEnter the car name:");

        String name = (new Scanner(System.in)).nextLine();
        carDao.addCompanysCar(name, company);

        System.out.println("The car was added!");
    }

    private void create_company() {
        System.out.println("\nEnter the company name:");

        String name = (new Scanner(System.in)).nextLine();
        companyDao.addCompany(name);

        System.out.println("The company was created!");
    }
}