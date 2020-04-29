CREATE TABLE Base_Price
(
	ID			    INT                 NOT NULL,
	Base_Cost		DECIMAL(13,2)                 NOT NULL,
	Size			VARCHAR(255)		NOT NULL,
	Crust_Type		VARCHAR(255)		NOT NULL,
	Price			DECIMAL(13,2)                 NOT NULL,
	CONSTRAINT BS_PRPK
		PRIMARY KEY(ID)
);

CREATE TABLE Cust_Order
(
    ID INT NOT NULL PRIMARY KEY,
    Total_Price DECIMAL(13, 2) NOT NULL,
    Business_Cost DECIMAL(13, 2) NOT NULL,
    Completed BOOLEAN DEFAULT FALSE NOT NULL,
    Type VARCHAR(255) NOT NULL
);

CREATE TABLE Pizza
(
    ID           INT                NOT NULL    PRIMARY KEY,
    Timestamp    DATETIME          NOT NULL,
    Price        DECIMAL(13,2)                NOT NULL,
    Cost         DECIMAL(13,2)                NOT NULL,
    baseprice_ID INT                NOT NULL,
    order_ID     INT                NOT NULL,
    Completed BOOLEAN DEFAULT FALSE NOT NULL,

    CONSTRAINT BS_PR_IDFK
        FOREIGN KEY (baseprice_ID) REFERENCES Base_Price (ID),
        FOREIGN KEY (order_ID) REFERENCES Cust_Order(ID)
);

CREATE TABLE Toppings
(
    ID INT NOT NULL PRIMARY KEY ,
    Name VARCHAR(255) NOT NULL,
    Price_to_Business DECIMAL(13,2) NOT NULL,
    Price_to_Customer DECIMAL(13,2) NOT NULL,
    Inventory DOUBLE NOT NULL,
    Amt_per_small DECIMAL(13,2) NOT NULL,
    Amt_per_medium DECIMAL(13,2) NOT NULL,
    Amt_per_large DECIMAL(13,2) NOT NULL,
    Amt_per_xlarge DECIMAL(13,2) NOT NULL
);

CREATE TABLE Has
(
    PizzaID     INT  NOT NULL,
    ToppingID   INT  NOT NULL,
    extra   BOOLEAN  NOT NULL DEFAULT false,

    PRIMARY KEY (PizzaID, ToppingID),
    CONSTRAINT PIZZA_ID_FK
        FOREIGN KEY (PizzaID) REFERENCES Pizza (ID),

    CONSTRAINT TOPPNAM_FK
        FOREIGN KEY (ToppingID) REFERENCES Toppings (ID)
);

CREATE TABLE Customer
(
    ID    INT        NOT NULL    Primary Key,
    Name    varchar(255)    NOT NULL,
    Address    varchar(255),
    Phone    varchar(255)    NOT NULL
);

CREATE TABLE Discounts
(
    ID INT NOT NULL PRIMARY KEY,
    Name VARCHAR(255) NOT NULL
);

CREATE TABLE Percent_off
(
    Discount_ID    INT    NOT NULL    Primary Key,
    Percentage_off    DOUBLE,
    CONSTRAINT Discount_ID_FK
        FOREIGN KEY (Discount_ID) REFERENCES Discounts (ID)
);

CREATE TABLE Dollar_off
(
    Discount_ID    INT    NOT NULL    Primary Key,
    Dollar_off    Decimal(13,2),
    CONSTRAINT Discount_ID_FK2
        FOREIGN KEY (Discount_ID) REFERENCES Discounts (ID)
);

CREATE TABLE Applied_to_Pizza
(
    Pizza_ID INT NOT NULL,
    Discount_ID INT NOT NULL,
    CONSTRAINT Discount_ID_FK3
        FOREIGN KEY (Discount_ID) REFERENCES Discounts (ID)
    ON UPDATE CASCADE  ON DELETE CASCADE,

    CONSTRAINT Pizza_ID_FK2
        FOREIGN KEY (Pizza_ID) REFERENCES Pizza (ID)
);

CREATE TABLE Applied_to_Order
(
    ORDER_ID    INT    NOT NULL,
    DISCOUNT_ID    INT    NOT NULL,
    PRIMARY KEY(ORDER_ID, DISCOUNT_ID),
    CONSTRAINT DISCOUNT_FK
        FOREIGN KEY (DISCOUNT_ID) REFERENCES Discounts(ID),
    CONSTRAINT ORDER_FK
        FOREIGN KEY (ORDER_ID) REFERENCES Cust_Order(ID)
);

CREATE TABLE Delivery
(
    Order_ID INT NOT NULL,
    Customer_ID INT NOT NULL,

    CONSTRAINT DeliverPKs
        PRIMARY KEY (Order_ID, Customer_ID),
    CONSTRAINT DeliverFks
        FOREIGN KEY (Order_ID) REFERENCES Cust_Order(ID),
        FOREIGN KEY (Customer_ID) REFERENCES Customer(ID)
);

CREATE TABLE Dine_IN
(
    Order_ID INT NOT NULL PRIMARY KEY,
    Table_Number INT NOT NULL,
    CONSTRAINT Order_FK3
        FOREIGN KEY (Order_ID) REFERENCES Cust_Order(ID)
);

CREATE TABLE Seat_Number
(
    Order_ID INT NOT NULL,
    Seat_Num INT NOT NULL,
    CONSTRAINT ORDER_IDFK
        FOREIGN KEY (Order_ID) REFERENCES Dine_IN(Order_ID),
    CONSTRAINT SeatNumberPK
        PRIMARY KEY (Order_ID, Seat_Num)
);

CREATE TABLE PickUp
(
    Order_ID    INT    NOT NULL,
    Customer_ID    INT    NOT NULL,
    CONSTRAINT Pick_PK
        PRIMARY KEY(Order_ID, Customer_ID),
    CONSTRAINT Order_FK2
        FOREIGN KEY (Order_ID) REFERENCES Cust_Order(ID),
    CONSTRAINT Cust_ID_FK
        FOREIGN KEY (Customer_ID) REFERENCES Customer(ID)
);

