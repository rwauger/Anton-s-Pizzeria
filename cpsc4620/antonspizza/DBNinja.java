package cpsc4620.antonspizza;

import java.io.*;
import java.sql.*;
import java.util.*;

/*
This file is where most of your code changes will occur
You will write the code to retrieve information from the database, or save information to the database

The class has several hard coded static variables used for the connection, you will need to change those to your connection information

This class also has static string variables for pickup, delivery and dine-in. If your database stores the strings differently (i.e "pick-up" vs "pickup") changing these static variables will ensure that the comparison is checking for the right string in other places in the program. You will also need to use these strings if you store this as boolean fields or an integer.


*/

/**
 * A utility class to help add and retrieve information from the database
 */

public final class DBNinja {
    //enter your user name here
    private static String user = "uzzltqlq";
    //enter your password here
    private static String password = "ClemsonTigers2020";
    //enter your database name here
    private static String database_name = "AntonPizza_ryan_zack_9h70";
    //Do not change the port. 3306 is the default MySQL port
    private static String port = "3306";
    private static Connection conn;

    //Change these variables to however you record dine-in, pick-up and delivery, and sizes and crusts
    public final static String pickup = "pickup";
    public final static String delivery = "delivery";
    public final static String dine_in = "dine-in";

    public final static String size_s = "Small";
    public final static String size_m = "Medium";
    public final static String size_l = "Large";
    public final static String size_xl = "X-Large";

    public final static String crust_thin = "Thin";
    public final static String crust_orig = "Original";
    public final static String crust_pan = "Pan";
    public final static String crust_gf = "Gluten-Free";



    /**
     * This function will handle the connection to the database
     * @return true if the connection was successfully made
     * @throws SQLException
     * @throws IOException
     */
    private static boolean connect_to_db() throws SQLException, IOException
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println ("Could not load the driver");

            System.out.println("Message     : " + e.getMessage());


            return false;
        }

        conn = DriverManager.getConnection("jdbc:mysql://mysql1.cs.clemson.edu:"+port+"/"+database_name, user, password);
        return true;
    }

    /**
     *
     * @param o order that needs to be saved to the database
     * @throws SQLException
     * @throws IOException
     * @requires o is not NULL. o's ID is -1, as it has not been assigned yet. The pizzas do not exist in the database
     *          yet, and the topping inventory will allow for these pizzas to be made
     * @ensures o will be assigned an id and added to the database, along with all of it's pizzas. Inventory levels
     *          will be updated appropriately
     */
    public static void addOrder(Order o) throws SQLException, IOException
    {
        connect_to_db();
        int OrderID = getLastID("Cust_Order")+1;
 
        String sql = "INSERT INTO Cust_Order (ID, Total_Price, Business_Cost, Completed, Type)" + " VALUES (?,?,?,?,?)";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setInt(1,OrderID);
        statement.setDouble(2, o.calcPrice());
        statement.setDouble(3,0.0);
        statement.setBoolean(4, false);
        statement.setString(5, o.getType()); //May not need to use order type in future?

        try
        {
            statement.executeUpdate();
            addPizzas(o.getPizzas(), OrderID);
            if(o.getType() == DBNinja.pickup)
            {
                addPickup(OrderID, o.getCustomer().getID());
            }
            else if(o.getType() == DBNinja.delivery)
            {
                addDelivery(OrderID, o.getCustomer().getID());
            }
            else
            {
                addDineIn(OrderID, o.getCustomer());
            }

            addDiscountstoOrder(OrderID, o.getDiscounts());
            
        }
        catch (SQLException e) {
            System.out.println("Error adding Order");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }
            //don't leave your connection open!
            conn.close();
        }
        

        //add a TimeStamp to the order?
        

        
        
        
		/* add code to add the order to the DB. Remember to add the pizzas and discounts as well, which will involve multiple tables. Customer should already exist. Toppings will need to be added to the pizzas.

		It may be beneficial to define more functions to add an individual pizza to a database, add a topping to a pizza, etc.

		Note: the order ID will be -1 and will need to be replaced to be a fitting primary key.

		You will also need to add timestamps to your pizzas/orders in your database. Those timestamps are not stored in this program, but you can get the current time before inserting into the database

		Remember, when a new order comes in the ingredient levels for the topping need to be adjusted accordingly. Remember to check for "extra" of a topping here as well.

		You do not need to check to see if you have the topping in stock before adding to a pizza. You can just let it go negative.
		*/
				

        conn.close();

    }

    /**
     *
     * @param c the new customer to add to the database
     * @throws SQLException
     * @throws IOException
     * @requires c is not null. C's ID is -1 and will need to be assigned
     * @ensures c is given an ID and added to the database
     */
    public static void addCustomer(ICustomer c) throws SQLException, IOException
    {
        connect_to_db();
		/*add code to add the customer to the DB.
		Note: the ID will be -1 and will need to be replaced to be a fitting primary key
		Note that the customer is an ICustomer data type, which means c could be a dine in, carryout or delivery customer
        */
        int lastID = getLastID("Customer");
        String name = null;
        String addr = null;
        String phone = null;
        
        
        String sql = "INSERT INTO Customer (ID, Name, Address, Phone)" + " VALUES (?,?,?,?)";
        PreparedStatement statement = conn.prepareStatement(sql);

        //getting information depending on customer type
        if (c instanceof DeliveryCustomer)
        {
            DeliveryCustomer d = (DeliveryCustomer) c;
            name = d.getName();
            addr = d.getAddress();
            phone = d.getPhone();
        }
        else if (c instanceof DineOutCustomer)
        {
            DineOutCustomer d = (DineOutCustomer) c;
            name = d.getName();
            phone = d.getPhone();  
        }

        statement.setInt(1, lastID + 1);
        statement.setString(2, name);
        statement.setString(3, addr);
        statement.setString(4, phone);

        try
        {
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Error adding Customer");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }
            //don't leave your connection open!
            conn.close();
        }
        

        conn.close();
    }

    /**
     *
     * @param o the order to mark as complete in the database
     * @throws SQLException
     * @throws IOException
     * @requires the order exists in the database
     * @ensures the order will be marked as complete
     */
    public static void CompleteOrder(Order o) throws SQLException, IOException
    {
        connect_to_db();
        /*add code to mark an order as complete in the DB. You may have a boolean field for this, or maybe a completed time timestamp. However you have it, */
        

        String query = "UPDATE Pizza SET Completed = 1 WHERE order_ID = " + o.getID() + ";";
        Statement stmt = conn.createStatement();

        String query2 = "Update Cust_Order SET Completed = 1 WHERE ID = ?";
        PreparedStatement statement = conn.prepareStatement(query2);

        statement.setInt(1, o.getID());

        try
        {
            stmt.executeUpdate(query);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Error updating completion");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }

            //don't leave your connection open!
            conn.close();
            return;
        }
        

        conn.close();
    }

    /**
     *
     * @param t the topping whose inventory is being replenished
     * @param toAdd the amount of inventory of t to add
     * @throws SQLException
     * @throws IOException
     * @requires t exists in the database and toAdd > 0
     * @ensures t's inventory level is increased by toAdd
     */
    public static void AddToInventory(Topping t, double toAdd) throws SQLException, IOException
    {
        connect_to_db();

        double newInv = toAdd + t.getInv();
        String sql = "UPDATE Toppings SET Inventory = ? WHERE ID = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setDouble(1, newInv);
        statement.setInt(2, t.getID());

        try
        {
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Error updating inventory");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }

            //don't leave your connection open!
            conn.close();
            return;
        }
        

        conn.close();
    }


    /*
        A function to get the list of toppings and their inventory levels. I have left this code "complete" as an example of how to use JDBC to get data from the database. This query will not work on your database if you have different field or table names, so it will need to be changed

        Also note, this is just getting the topping ids and then calling getTopping() to get the actual topping. You will need to complete this on your own

        You don't actually have to use and write the getTopping() function, but it can save some repeated code if the program were to expand, and it keeps the functions simpler, more elegant and easy to read. Breaking up the queries this way also keeps them simpler. I think it's a better way to do it, and many people in the industry would agree, but its a suggestion, not a requirement.
    */

    /**
     *
     * @return the List of all toppings in the database
     * @throws SQLException
     * @throws IOException
     * @ensures the returned list will include all toppings and accurate inventory levels
     */
    public static ArrayList<Topping> getInventory() throws SQLException, IOException
    {
        //start by connecting
        connect_to_db();
        ArrayList<Topping> ts = new ArrayList<Topping>();
        //create a string with out query, this one is an easy one
        String query = "Select ID From Toppings;";

        Statement stmt = conn.createStatement();
        try {
            ResultSet rset = stmt.executeQuery(query);
            //even if you only have one result, you still need to call ResultSet.next() to load the first tuple
            while(rset.next())
            {
					/*Use getInt, getDouble, getString to get the actual value. You can use the column number starting with 1, or use the column name as a string

					NOTE: You want to use rset.getInt() instead of Integer.parseInt(rset.getString()), not just because it's shorter, but because of the possible NULL values. A NUll would cause parseInt to fail

					If there is a possibility that it could return a NULL value you need to check to see if it was NULL. In this query we won't get nulls, so I didn't. If I was going to I would do:

					int ID = rset.getInt(1);
					if(rset.wasNull())
					{
						//set ID to what it should be for NULL, and whatever you need to do.
					}

					NOTE: you can't check for NULL until after you have read the value using one of the getters.

					*/
                int ID = rset.getInt(1);
                //Now I'm just passing my primary key to this function to get the topping itself individually
                ts.add(getTopping(ID));
            }
        }
        catch (SQLException e) {
            System.out.println("Error loading inventory");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }

            //don't leave your connection open!
            conn.close();
            return ts;
        }


        //end by closing the connection
        conn.close();
        return ts;
    }

    /**
     *
     * @return a list of all orders that are currently open in the kitchen
     * @throws SQLException
     * @throws IOException
     * @ensures all currently open orders will be included in the returned list.
     */
    public static ArrayList<Order> getCurrentOrders() throws SQLException, IOException
    {
        connect_to_db();

        ArrayList<Order> os = new ArrayList<Order>();
        /*add code to get a list of all open orders. Only return Orders that have not been completed. If any pizzas are not completed, then the order is open.*/

        String query = "SELECT ID FROM Cust_Order";
        Statement stmt = conn.createStatement();

        try
        {
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next())
            {
                int id = rs.getInt(1);

                if(getOrder(id) != null)
                {
                    os.add(getOrder(id));
                }
                
            }

        }
        catch (SQLException e) {
            System.out.println("Error loading orders");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }

            //don't leave your connection open!
            conn.close();
            return os;
        }
        
        conn.close();
        return os;
    }

    /**
     *
     * @param size the pizza size
     * @param crust the type of crust
     * @return the base price for a pizza with that size and crust
     * @throws SQLException
     * @throws IOException
     * @requires size = size_s || size_m || size_l || size_xl AND crust = crust_thin || crust_orig || crust_pan || crust_gf
     * @ensures the base price for a pizza with that size and crust is returned
     */
    public static double getBasePrice(String size, String crust) throws SQLException, IOException
    {
        connect_to_db();
        double bp = 0.0;
        //add code to get the base price for that size and crust pizza Depending on how you store size and crust in your database, you may have to do a conversion
        String query = "SELECT Price FROM Base_Price WHERE Size = ? AND Crust_Type = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, size);
        stmt.setString(2, crust);

        try
        {
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                bp = rs.getDouble(1);
            }

        }
        catch (SQLException e) {
            System.out.println("Error loading base prices");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }

            //don't leave your connection open!
            conn.close();
            return bp;
        }

        conn.close();
        return bp;
    }

    /**
     *
     * @return the list of all discounts in the database
     * @throws SQLException
     * @throws IOException
     * @ensures all discounts are included in the returned list
     */
    public static ArrayList<Discount> getDiscountList() throws SQLException, IOException
    {
        ArrayList<Discount> discs = new ArrayList<Discount>();
        connect_to_db();
        //add code to get a list of all discounts
        String query = "Select ID From Discounts;";
        Statement stmt = conn.createStatement();

        try {
            ResultSet rset = stmt.executeQuery(query);
            //even if you only have one result, you still need to call ResultSet.next() to load the first tuple
            while(rset.next())
            {			
                int ID = rset.getInt(1);
                discs.add(getDiscount(ID));
            }
        }
        catch (SQLException e) {
            System.out.println("Error loading discounts");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }

            //don't leave your connection open!
            conn.close();
            return discs;
        }



        conn.close();
        return discs;
    }

    /**
     *
     * @return the list of all delivery and carry out customers
     * @throws SQLException
     * @throws IOException
     * @ensures the list contains all carryout and delivery customers in the database
     */
    public static ArrayList<ICustomer> getCustomerList() throws SQLException, IOException
    {
        ArrayList<ICustomer> custs = new ArrayList<ICustomer>();
        connect_to_db();
        
        //add code to get a list of all customers
        String query = "Select ID From Customer;";

        Statement stmt = conn.createStatement();
        try {
            ResultSet rset = stmt.executeQuery(query);
            //even if you only have one result, you still need to call ResultSet.next() to load the first tuple
            while(rset.next())
            {
					
                int ID = rset.getInt(1);
                //Now I'm just passing my primary key to this function to get the topping itself individually
                custs.add(getCustomer(ID));
            }
        }
        catch (SQLException e) {
            System.out.println("Error loading customers");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }

            //don't leave your connection open!
            conn.close();
            return custs;
        }

        conn.close();
        return custs;
    }



	/*
	Note: The following incomplete functions are not strictly required, but could make your DBNinja class much simpler. For instance, instead of writing one query to get all of the information about an order, you can find the primary key of the order, and use that to find the primary keys of the pizzas on that order, then use the pizza primary keys individually to build your pizzas. We are no longer trying to get everything in one query, so feel free to break them up as much as possible

	You could also add functions that take in a Pizza object and add that to the database, or take in a pizza id and a topping id and add that topping to the pizza in the database, etc. I would recommend this to keep your addOrder function much simpler

	These simpler functions should still not be called from our menu class. That is why they are private

	We don't need to open and close the connection in these, since they are only called by a function that has opened the connection and will close it after
	*/

	
    private static Topping getTopping(int ID) throws SQLException, IOException
    {

        //add code to get a topping
		//the java compiler on unix does not like that t could be null, so I created a fake topping that will be replaced
        Topping t = new Topping("fake", 0.25, 100.0, -1);
		String query = "Select Name, Price_to_Customer, Inventory From Toppings where ID = " + ID + ";";

        Statement stmt = conn.createStatement();
        try {
            ResultSet rset = stmt.executeQuery(query);
            //even if you only have one result, you still need to call ResultSet.next() to load the first tuple
            while(rset.next())
            {
					String tname = rset.getString(1);
					double price = rset.getDouble(2);
					double inv = rset.getDouble(3);
					
					t = new Topping(tname, price, inv, ID);
			}
			
		}
		catch (SQLException e) {
            System.out.println("Error loading Topping");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }

            //don't leave your connection open!
            conn.close();
            return t;
        }
		
        return t;

    }

    private static Discount getDiscount(int ID)  throws SQLException, IOException
    {

        //add code to get a discount
        //just print outname or do we need other information?
        Discount D = null;
        String query = "SELECT Name FROM Discounts WHERE ID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, ID);

        try {
            ResultSet rset = stmt.executeQuery();
            //even if you only have one result, you still need to call ResultSet.next() to load the first tuple
            while(rset.next())
            {
                    String name = rset.getString(1);
                    double discount = getAmountOff(ID);
                    String type = getType(ID);
                    
                    if (type == "Dollar")
                    {
                        D = new Discount(name, 0.0, discount, ID);
                    }
                    else if (type == "Percent")
                    {
                        D = new Discount(name, discount, 0.0, ID);
                    }
                    
			}
			
		}
		catch (SQLException e) {
            System.out.println("Error getting Discount");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }

            //don't leave your connection open!
            conn.close();
            return D;
        }

        return D;

    }

    private static double getAmountOff(int ID)  throws SQLException, IOException
    {
        double amount = 0.0;
        String query = "SELECT Percentage_off FROM Percent_off WHERE Discount_ID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);

        stmt.setInt(1, ID);

        try
        {
            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                amount = rs.getDouble(1);
            }
            else
            {
                query = "SELECT Dollar_off FROM Dollar_off WHERE Discount_ID = ?";
                stmt = conn.prepareStatement(query);

                stmt.setInt(1, ID);
                rs = stmt.executeQuery();
                rs.next();
                amount = rs.getDouble(1);
            }
            

        }
        catch (SQLException e) {
            System.out.println("Error getting amount off");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }

            //don't leave your connection open!
            conn.close();
            return amount;
        }


        return amount;
    }

    private static String getType(int ID)  throws SQLException, IOException
    {
        String type = new String();
        String query = "SELECT Percentage_off FROM Percent_off WHERE Discount_ID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);

        stmt.setInt(1, ID);

        try
        {
            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                type = "Percent"; 
            }
            else
            {
                type = "Dollar";
            }  
        }
        catch (SQLException e) {
            System.out.println("Error getting amount off");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }

            //don't leave your connection open!
            conn.close();
            return type;
        }


        return type;
    }

    private static ArrayList<Pizza> GetOrderPizzas(int ID)  throws SQLException, IOException
    {

        
        ArrayList<Pizza> p = new ArrayList<Pizza>();
        
        String query = "SELECT ID, baseprice_ID FROM Pizza WHERE order_ID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);

        stmt.setInt(1, ID);

        try 
        {
            ResultSet rs = stmt.executeQuery();
            //doing all this in one function too lazy to break up
            while(rs.next())
            {
                //variables for info
                int id = rs.getInt(1);
                int bpID = rs.getInt(2);
                String size = null;
                String crust = null;
                double price = 0.0;
            
                //getting size, crust, and base price id
                
                String bpQuery = "SELECT Size, Crust_Type, Price FROM Base_Price WHERE ID = ?";
                PreparedStatement statement = conn.prepareStatement(bpQuery);
                statement.setInt(1, bpID);
                ResultSet rset = statement.executeQuery();

                while(rset.next())
                {
                    size = rset.getString(1);
                    crust = rset.getString(2);
                    price = rset.getDouble(3);
                }

                //creating pizza
                Pizza e = new Pizza(id, size, crust, price);

                //getting toppings
                String tQuery = "SELECT ToppingID FROM Has WHERE PizzaID = ?";
                statement = conn.prepareStatement(tQuery);
                statement.setInt(1, id);
                rset = statement.executeQuery();

                while(rset.next())
                {
                    int tID = rset.getInt(1);
                    e.addTopping(getTopping(tID));
                }

                //getting discounts
                String dQuery = "SELECT Discount_ID FROM Applied_to_Pizza WHERE Pizza_ID = ?";
                statement = conn.prepareStatement(dQuery);
                statement.setInt(1, id);
                rset = statement.executeQuery();

                while(rset.next())
                {
                    int disID = rs.getInt(1);
                    e.addDiscount(getDiscount(disID));
                }

                //adding pizza to list
                p.add(e);
            }
            
        }
        catch (SQLException e) {
            System.out.println("Error getting order pizzas");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }

            //don't leave your connection open!
            conn.close();
            return p;
        }

        return p;

    }

    private static ArrayList<Discount> GetOrderDiscounts(int ID)  throws SQLException, IOException
    {
        ArrayList<Discount> d = new ArrayList<Discount>();


        String query = "SELECT Discount_ID FROM Applied_to_Order WHERE ORDER_ID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, ID);

        try
        {
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                int discID = rs.getInt(1);
                d.add(getDiscount(discID));
            }

        }
        catch (SQLException e) {
            System.out.println("Error getting order discounts");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }

            //don't leave your connection open!
            conn.close();
            return d;
        }

        return d;
    }

    private static ICustomer getCustomer(int ID)  throws SQLException, IOException
    {

        //add code to get customer
        ICustomer C = null;
        String type = null;
        String query = "SELECT Name, Address, Phone FROM Customer WHERE ID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, ID);


        try {
            ResultSet rset = stmt.executeQuery();
            //even if you only have one result, you still need to call ResultSet.next() to load the first tuple
            while(rset.next())
            {
					String name = rset.getString(1);
                    String addr = rset.getString(2);

                    if(rset.wasNull())
					{
						type = DBNinja.pickup;
                    }
                    else
                    {
                        type = DBNinja.delivery;
                    }

					String phone = rset.getString(3);
                    
                    if(type == DBNinja.pickup)
                    {
                        C = new DineOutCustomer(ID, name, phone);
                    }
                    else
                    {
                        C = new DeliveryCustomer(ID, name, phone, addr);
                    }		
			}
			
		}
		catch (SQLException e) {
            System.out.println("Error loading Customer");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }

            //don't leave your connection open!
            conn.close();
            return C;
        }


        return C;
    }

    private static ICustomer GetOrderCustomer(int ID, String type) throws SQLException {
        String query = new String();
        int cust_ID = 0;
        String Name = null;
        String Phone = null;
        String Address = null;
        ICustomer cust = null;
        PreparedStatement stmt = null;
        ArrayList<Integer> seats = new ArrayList<Integer>();
        int table = 0;

        if (type.compareTo(DBNinja.pickup) == 0) {

            query = "SELECT Customer_ID From PickUp WHERE Order_ID = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, ID);
        } else if (type.compareTo(DBNinja.delivery) == 0) {

            query = "SELECT Customer_ID From Delivery WHERE Order_ID = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, ID);
        }
        else
        {
            query = "SELECT Table_Number, Seat_Num FROM Dine_IN JOIN Seat_Number ON Seat_Number.Order_ID = Dine_IN.Order_ID WHERE Dine_IN.Order_ID = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, ID);
        }
        

        try {
            
            ResultSet rs = stmt.executeQuery();

            
            
            while (rs.next()) 
            {
                if (type.compareTo(DBNinja.pickup) == 0) {
                    cust_ID = rs.getInt(1);
                    String pQuery = "SELECT Name, Phone From Customer WHERE ID = ?";
                    PreparedStatement statement = conn.prepareStatement(pQuery);
                    statement.setInt(1, cust_ID);

                    ResultSet rset = statement.executeQuery();
    
                    while (rset.next()) 
                    {
                        
                        Name = rset.getString(1);
                        Phone = rset.getString(2);
                    }
 
                   cust = new DineOutCustomer(cust_ID, Name, Phone);
        
                } 
                else if (type.compareTo(DBNinja.delivery) == 0) 
                {
                    cust_ID = rs.getInt(1);
                    String dQuery = "SELECT Name, Address, Phone From Customer WHERE ID = ?";
                    PreparedStatement statement = conn.prepareStatement(dQuery);
    
                    statement.setInt(1, cust_ID);
        
                    
                    ResultSet rset = statement.executeQuery();
    
                    while (rset.next()) 
                    {
                        Name = rset.getString(1);
                        Address = rset.getString(2);
                        Phone = rset.getString(3);
                    }
                    
                    cust = new DeliveryCustomer(cust_ID, Name, Address, Phone);
                   
                }
                else
                {
                    
                    table = rs.getInt(1);
                    int seat = rs.getInt(2);
                    seats.add(seat);
                }
            }
            
            if (type.compareTo(DBNinja.dine_in) == 0)
            {
                cust = new DineInCustomer(table, seats, -1);
            }

        } catch (SQLException e) {
            System.out.println("Error getting cust_ID");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }
        }

        

 
        
        


        return cust;
    }

    private static Order getOrder(int ID)  throws SQLException, IOException
    {

        //add code to get an order. Remember, an order has pizzas, a customer, and discounts on it


        Order O = null;
        String sql = "SELECT Completed, Type FROM Cust_Order WHERE ID = ?"; 
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, ID);

        try {
            ResultSet rset = stmt.executeQuery();
            //even if you only have one result, you still need to call ResultSet.next() to load the first tuple
            while(rset.next())
            {
				
                Boolean complete = rset.getBoolean(1);
                String type = rset.getString(2);
                
                
                if(!complete)
                {
                    ICustomer cust = GetOrderCustomer(ID, type);
                    O = new Order(ID, cust, type);
                    
                    ArrayList<Pizza> p = GetOrderPizzas(ID);
                    
                    for(Pizza i : p)
                    {
                        O.addPizza(i);
                    }

                    ArrayList<Discount> d = GetOrderDiscounts(ID);

                    for(Discount i : d)
                    {
                        O.addDiscount(i);
                    }
                    
                }	
			}
			
		}
		catch (SQLException e) {
            System.out.println("Error loading Order");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }

            //don't leave your connection open!
            conn.close();
            return O;
        }
        
        return O;
    }
    

    //gets last used ID from given table. If an error occurs returns -1
    private static int getLastID(String table) throws SQLException, IOException
    {
        int lastID = -1;
        //can't use prepared statments with table names
        String query = "SELECT MAX(ID) FROM "  + table + ";";

        Statement stmt = conn.createStatement();

        try {
            ResultSet rset = stmt.executeQuery(query);
            //even if you only have one result, you still need to call ResultSet.next() to load the first tuple
            while(rset.next())
            {
                lastID = rset.getInt(1);

            }

        }
        catch (SQLException e) {
            System.out.println("Error getting Last ID");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }

            //don't leave your connection open!
            conn.close();
            lastID = -1;
            return lastID;
        }

        return lastID;
    }

    private static void addPizzas(ArrayList<Pizza> p, int OrderID) throws SQLException, IOException
    {
        
        
        for(Pizza i : p)
        {
            int id = getLastID("Pizza")+1;
            Timestamp time = new Timestamp(System.currentTimeMillis());
            int bpID = getBaseID(i.getCrust(), i.getSize());
    
            String query = "INSERT INTO Pizza (ID, Timestamp, Price, Cost, basePrice_ID, order_ID, Completed) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
            PreparedStatement stmt = conn.prepareStatement(query);
    
            stmt.setInt(1, id);
            stmt.setTimestamp(2, time);
            stmt.setDouble(3, i.calcPrice());
            stmt.setDouble(4, 0.0);
            stmt.setInt(5, bpID);
            stmt.setInt(6, OrderID);
            stmt.setBoolean(7, false);

    
            try {
                stmt.executeUpdate();
                addToppings(i, id);
                addDiscountstoPizza(id, i.getDiscounts());
            }
            catch (SQLException e) {
                System.out.println("Error adding pizza");
                while (e != null) {
                    System.out.println("Message     : " + e.getMessage());
                    e = e.getNextException();
                }
                //don't leave your connection open!
                conn.close();
            }
        }
    }

    private static void addToppings(Pizza p, int PizzaID) throws SQLException, IOException
    {
        for(Topping i : p.getToppings())
        {
            String query = "INSERT INTO Has (PizzaID, ToppingID, extra) VALUES (?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, PizzaID);
            stmt.setInt(2, i.getID());
            stmt.setBoolean(3, i.getExtra());

            double newInv = getUpdatedInv(i, p.getSize());
            String query2 = "UPDATE Toppings SET Inventory = ? WHERE ID = ?";
            PreparedStatement stmt2 = conn.prepareStatement(query2);

            stmt2.setDouble(1, newInv);
            stmt2.setInt(2, i.getID());

            try {
                stmt.executeUpdate();
                stmt2.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println("Error adding topping");
                while (e != null) {
                    System.out.println("Message     : " + e.getMessage());
                    e = e.getNextException();
                }
                //don't leave your connection open!
                conn.close();
            }
        }
    }

    private static double getUpdatedInv(Topping t, String size) throws SQLException, IOException
    {
        double inv = 0;
        double amount = 0; 
        String query = null;

        
        //name in table didn't match up do preset size string names. Was too lazy to change
        if(size == DBNinja.size_s)
        {
            query = "SELECT Inventory, Amt_per_small From Toppings WHERE ID = ?";
        }
        else if(size == DBNinja.size_m)
        {
            query = "SELECT Inventory, Amt_per_medium From Toppings WHERE ID = ?";
        }
        else if(size == DBNinja.size_l)
        {
            query = "SELECT Inventory, Amt_per_large From Toppings WHERE ID = ?";
        }
        else if(size == DBNinja.size_xl)
        {
            query = "SELECT Inventory, Amt_per_xlarge From Toppings WHERE ID = ?";
        }


        PreparedStatement stmt = conn.prepareStatement(query);

        stmt.setInt(1, t.getID());

        try{
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                inv = rs.getDouble(1);
                amount = rs.getDouble(2);
            }
        }
        catch (SQLException e) {
            System.out.println("Error getting inventory");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }
            //don't leave your connection open!
            conn.close();
        }

        if(t.getExtra())
        {
            inv -= (amount*2);
        }
        else
        {
            inv -= amount;
        }

        return inv;
    }

    private static int getBaseID(String crust, String size) throws SQLException, IOException
    {
        int id = -1;
        String query = "SELECT ID FROM Base_Price WHERE Size = ? AND Crust_Type = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, size);
        stmt.setString(2, crust);

        try
        {
            ResultSet rset = stmt.executeQuery();
            while (rset.next())
            {
                id = rset.getInt(1);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error getting Base price ID");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }
            //don't leave your connection open!
            conn.close();
        }

        return id;
    }

    private static void addPickup(int OrderID, int CustomerID) throws SQLException, IOException
    {
        String query = "INSERT INTO PickUp (Order_ID, Customer_ID) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, OrderID);
        stmt.setInt(2, CustomerID);

        try
        {
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Error adding Pickup order");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }
            //don't leave your connection open!
            conn.close();
        }
    }

    private static void addDelivery(int OrderID, int CustomerID) throws SQLException, IOException
    {
        String query = "INSERT INTO Delivery (Order_ID, Customer_ID) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, OrderID);
        stmt.setInt(2, CustomerID);

        try
        {
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Error adding Delivery order");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }
            //don't leave your connection open!
            conn.close();
        }
    }

    private static void addDineIn(int OrderID, ICustomer c) throws SQLException, IOException
    {
        DineInCustomer d = (DineInCustomer) c;

        String query = "INSERT INTO Dine_IN (Order_ID, Table_Number) VALUES(?,?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, OrderID);
        stmt.setInt(2, d.getTableNum());

        try
        {
            stmt.executeUpdate();
            addSeats(OrderID, d.getSeats());
        }
        catch (SQLException e)
        {
            System.out.println("Error adding Dine In order");
            while (e != null) {
                System.out.println("Message     : " + e.getMessage());
                e = e.getNextException();
            }
            //don't leave your connection open!
            conn.close();
        }
    }

    private static void addSeats(int OrderID, List<Integer> seats) throws SQLException, IOException
    {
        for (Integer i : seats)
        {
            String query = "INSERT INTO Seat_Number (Order_ID, Seat_Num) VALUES(?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, OrderID);
            stmt.setInt(2, i);

            try
            {
                stmt.executeUpdate();
            }
            catch (SQLException e)
            {
                System.out.println("Error adding seats to order");
                while (e != null) {
                    System.out.println("Message     : " + e.getMessage());
                    e = e.getNextException();
                }
                //don't leave your connection open!
                conn.close();
            }
        }
    }

    private static void addDiscountstoOrder(int OrderID, ArrayList<Discount> d) throws SQLException, IOException
    {
        for(Discount i: d)
        {
            String query = "INSERT INTO Applied_to_Order (ORDER_ID, DISCOUNT_ID) VALUES (?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, OrderID);
            stmt.setInt(2, i.getID());

            try
            {
                stmt.executeUpdate();
            }
            catch (SQLException e)
            {
                System.out.println("Error adding discount to order");
                while (e != null) {
                    System.out.println("Message     : " + e.getMessage());
                    e = e.getNextException();
                }
                //don't leave your connection open!
                conn.close();
            }
        }
    }

    
    private static void addDiscountstoPizza(int PizzaID, ArrayList<Discount> d) throws SQLException, IOException
    {
        for(Discount i: d)
        {
            String query = "INSERT INTO Applied_to_Pizza (Pizza_ID, Discount_ID) VALUES (?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, PizzaID);
            stmt.setInt(2, i.getID());

            try
            {
                stmt.executeUpdate();
            }
            catch (SQLException e)
            {
                System.out.println("Error adding discount to pizza");
                while (e != null) {
                    System.out.println("Message     : " + e.getMessage());
                    e = e.getNextException();
                }
                //don't leave your connection open!
                conn.close();
            }
        }
    }
}
