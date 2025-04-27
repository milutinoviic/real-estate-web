

INSERT INTO users (name, last, phone, address, email, password, active, role)
VALUES ('John', 'Doe', '123-456-7890', '123 Main St', 'admin@admin', 'admin', true, 3);

INSERT INTO users (name, last, phone, address, email, password, active, role)
VALUES ('Jane', 'Smith', '987-654-3210', '456 Elm St', 'jane.smith@example.com', 'pass456', true, 0);

INSERT INTO users (name, last, phone, address, email, password, active, role)
VALUES ('Bob', 'Johnson', '555-555-5555', '789 Pine St', 'o@o', 'o', true, 1);

INSERT INTO users (name, last, phone, address, email, password, active, role)
VALUES ('Alice', 'Brown', '111-222-3333', '101 Oak St', 'alice.brown@example.com', 'alicepassword', true, 2);
INSERT INTO users (id,name, last, phone, address, email, password, active, role)
VALUES (44,'AliAAce', 'BroAAwn', '111-222-3333', '101 Oak St', 'a@a', 'a', true, 2);

INSERT INTO users (name, last, phone, address, email, password, active, role)
VALUES ('JanSSe', 'SmitDSh', '987-654-3210', '456 Elm St', 'b@b', 'b', true, 0);



INSERT INTO agency (id,name, owner_id) VALUES (55,'Sunshine Realty', 1);

INSERT INTO real_estate (id,location, surface_area, price, type_sales, type_real_estate, picture, agency_id, agent_id, active)
VALUES
    (77,'123 Elm Street', 120.5, 250000, 0, 0, 'lake.jpg', 55, 44, true),
    (22,'456 Oak Avenue', 85.0, 1500, 1, 1, 'lake.jpg', 55, 44, true),
    (33,'789 Pine Boulevard', 200.0, 450000, 0, 1, 'vac.jpg', 55, 44, true);






