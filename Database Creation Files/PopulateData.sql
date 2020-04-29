INSERT INTO Toppings
VALUES  (1,'Pepperoni', .2, 1.25, 100, 2, 2.75, 3.5, 4.5),
        (2,'Sausage', .15, 1.25, 100, 2.5, 3, 3.5, 4.25),
        (3,'Ham', .15, 1.5, 78, 2, 2.5, 3.25, 4),
        (4,'Chicken', .25, 1.75, 56, 1.5, 2, 2.25, 3),
        (5,'Green Pepper', .2, .5, 79, 1, 1.5, 2, 2.5),
        (6,'Onion', .02, .5, 85, 1, 1.5, 2, 2.75),
        (7,'Roma Tomato', .03, .75, 86, 2, 3, 3.5, 4.5),
        (8,'Mushrooms', .1, .6, 52, 1.5, 2, 2.5, 3),
        (9,'Black Olives', .1, 1.25, 39, .75, 1, 1.5, 2),
        (10,'Pineapple', .25, 1, 15, 1, 1.25, 1.75, 2),
        (11,'Jalapenos', .05, .5, 64, .5, .75, 1.25, 1.75),
        (12,'Banana Peppers', .05, .5, 36, .6, 1, 1.3, 1.75),
        (13,'Regular Cheese', .12, 1.5, 250, 2, 3.5, 5, 7),
        (14,'Four Cheese Blend', .15, 2, 150, 2, 3.5, 5, 7),
        (15,'Feta Cheese', .18, 2, 75, 1.75, 3, 4, 5.5),
        (16,'Goat Cheese', .2, 2, 54, 1.6, 2.75, 4, 5.5),
        (17,'Bacon', .25, 1.5, 89, 1, 1.5, 2, 3);

INSERT INTO Discounts
    VALUES  (1, 'employee'),
            (2, 'Lunch Special Medium'),
            (3, 'Lunch Special Large'),
            (4, 'Specialty Pizza'),
            (5, 'Gameday special');

INSERT INTO Percent_off
    values (1, 15),
           (5, 20);

INSERT INTO Dollar_off
    values (2, 1),
           (3, 2),
           (4, 1.5);

INSERT INTO Base_Price
    VALUES  (1, 0.5, 'small', 'Thin', 3),
            (2, 0.75, 'small', 'Original', 3),
            (3, 1, 'small', 'pan', 3.5),
            (4, 2, 'small', 'Gluten-Free', 4),
            (5, 1, 'medium', 'Thin', 5),
            (6, 1.5, 'medium', 'Original', 5),
            (7, 2.25, 'medium', 'Pan', 6),
            (8, 3, 'medium', 'Gluten-Free', 6.25),
            (9, 1.25, 'Large', 'Thin', 8),
            (10, 2, 'Large', 'Original', 8),
            (11, 3, 'Large', 'Pan', 9),
            (12, 4, 'Large', 'Gluten-Free', 9.5),
            (13, 2, 'X-Large', 'Thin', 10),
            (14, 3, 'X-Large', 'Original', 10),
            (15, 4.5, 'X-Large', 'Pan', 11.5),
            (16, 6, 'X-Large', 'Gluten-Free', 12.5);
#
# INSERT INTO Cust_Order VALUES (1, 13.50, 3.68, TRUE);
# INSERT INTO Pizza VALUES (1, '2020-04-05 12:03:00', 13.50, 3.68, 13, 1);
# INSERT INTO Has VALUES (1, 'Regular Cheese', TRUE), (1, 'Pepperoni', FALSE), (1, 'Sausage', FALSE);
# INSERT INTO Applied_to_Pizza VALUES (1, 3);
# INSERT INTO Dine_IN VALUES (1, 14);
# INSERT INTO Seat_Number VALUES (1, 1), (1, 2), (1, 3);
#
# INSERT INTO Cust_Order VALUES (4, 64.5, 19.8, TRUE);
# INSERT INTO Customer VALUES (21, 'Andrew Wilkes-Krier', '115 Party Blvd, Anderson SC 29621', '864-254-5861');
# INSERT INTO PickUp VALUES (4, 21);
# INSERT INTO Pizza VALUES (4, '2020-04-03 12:05:00', 10.75, 3.30, 10, 4);
# INSERT INTO Pizza VALUES (12, '2020-04-03 12:05:00', 10.75, 3.30, 10, 4);
# INSERT INTO Pizza VALUES (13, '2020-04-03 12:05:00', 10.75, 3.30, 10, 4);
# INSERT INTO Pizza VALUES (14, '2020-04-03 12:05:00', 10.75, 3.30, 10, 4);
# INSERT INTO Pizza VALUES (15, '2020-04-03 12:05:00', 10.75, 3.30, 10, 4);
# INSERT INTO Pizza VALUES (16, '2020-04-03 12:05:00', 10.75, 3.30, 10, 4);
# INSERT INTO Has VALUES (4, 'Regular Cheese', FALSE), (4, 'Pepperoni', FALSE);
# INSERT INTO Has VALUES (12, 'Regular Cheese', FALSE), (12, 'Pepperoni', FALSE);
# INSERT INTO Has VALUES (13, 'Regular Cheese', FALSE), (13, 'Pepperoni', FALSE);
# INSERT INTO Has VALUES (14, 'Regular Cheese', FALSE), (14, 'Pepperoni', FALSE);
# INSERT INTO Has VALUES (15, 'Regular Cheese', FALSE), (15, 'Pepperoni', FALSE);
# INSERT INTO Has VALUES (16, 'Regular Cheese', FALSE), (16, 'Pepperoni', FALSE);
#
# INSERT INTO Cust_Order VALUES (3, 6.75, 1.40, TRUE);
# INSERT INTO Pizza VALUES (3, '2020-04-03 12:05:00', 6.75, 1.40, 2, 3);
# INSERT INTO Has VALUES (3, 'Regular Cheese', FALSE), (3, 'Chicken', FALSE), (3, 'Banana Peppers', FALSE);
# INSERT INTO Applied_to_Pizza VALUES (3, 4);
# INSERT INTO Dine_IN VALUES(3, 4);
# INSERT INTO Seat_Number VALUES (3, 2);
#
# INSERT INTO Cust_Order VALUES (2, 10.60, 3.23, TRUE);
# INSERT INTO Pizza VALUES (2, '2020-04-03 12:05:00', 10.60, 3.23, 13, 2);
# INSERT INTO Has VALUES (2, 'Feta Cheese', FALSE), (2,'Black Olives', FALSE), (2,'Roma Tomato', FALSE), (2,'Mushrooms', FALSE), (2,'Banana Peppers', FALSE);
# INSERT INTO Applied_to_Order VALUES (2, 2);
# INSERT INTO Applied_to_Pizza VALUES (2, 4);
# INSERT INTO Dine_IN VALUES (2, 4);
# INSERT INTO Seat_Number Values (2,1);
#
# INSERT INTO Cust_Order VALUES (5, 45.5, 16.86, TRUE);
# INSERT INTO Pizza VALUES (5, '2020-04-05 07:11:00', 14.50, 5.59, 14,5), (6, '2020-04-05 07:11:00', 17, 5.59, 14, 5),
# (7, '2020-04-05 07:11:00', 14, 5.68, 14, 5);
# INSERT INTO Has VALUES (5, 'Four Cheese Blend', FALSE), (5, 'Pepperoni', FALSE), (5, 'Sausage', FALSE),
# (6, 'Four Cheese Blend', FALSE), (6, 'Ham', TRUE), (6, 'Pineapple', TRUE),
# (7, 'Four Cheese Blend', FALSE), (7, 'Jalapenos', FALSE), (7, 'Bacon', FALSE);
# INSERT INTO Applied_to_Order VALUES (5, 5);
# INSERT INTO Applied_to_Pizza VALUES (6, 4);
# INSERT INTO Delivery VALUES (5, 21);
#
# INSERT INTO Customer VALUES (2, 'Matt Engers', NULL, '864-474-9953');
# INSERT INTO Cust_Order VALUES (6, 16.85, 7.85, TRUE);
# INSERT INTO Pizza VALUES (8, '2020-04-02 5:30:00', 16.85, 7.85, 16, 6);
# INSERT INTO Has VALUES (8, 'Green Pepper', FALSE), (8, 'Onion', FALSE), (8, 'Roma Tomato', FALSE), (8, 'Mushrooms', FALSE),
# (8, 'Black Olives', FALSE), (8, 'Goat Cheese', FALSE);
# INSERT INTO Applied_to_Pizza VALUES (8, 4);
#
# INSERT INTO Customer VALUES (3, 'Frank Turner', '6745 Wessex St Anderson SC 29621', '864-232-8944');
# INSERT INTO Cust_Order VALUES (7, 13.25, 3.20, TRUE);
# INSERT INTO Pizza VALUES (9, '2020-04-06 08:32:00', 13.25, 3.20, 9, 7);
# INSERT INTO Has VALUES (9, 'Chicken', FALSE), (9, 'Green Pepper', FALSE), (9, 'Onion', FALSE), (9, 'Mushrooms', FALSE),
# (9, 'Four Cheese Blend', FALSE);
# INSERT INTO Delivery VALUES (7, 3);
#
# INSERT INTO Customer VALUES (12, 'Milo Auckerman', '8879 Suburban Home, Anderson, SC 29621', '864-878-5679');
# INSERT INTO Cust_Order VALUES (15, 24, 6.3, TRUE);
# INSERT INTO Pizza VALUES (10, '2020-04-06 8:32:00', 12, 3.75, 9, 15);
# INSERT INTO Pizza VALUES (11, '2020-04-06 8:32:00', 12, 2.55, 9, 15);
# INSERT INTO Has VALUES (10, 'Four Cheese Blend', TRUE), (11, 'Regular Cheese', FALSE), (11, 'Pepperoni', TRUE);
# INSERT INTO Applied_to_Order VALUES (15, 1);
# INSERT INTO Delivery VALUES (15, 12);