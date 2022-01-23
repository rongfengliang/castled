CREATE SCHEMA DEMO_SCHEMA;

CREATE TABLE DEMO_SCHEMA.LEADS (
	business_name VARCHAR ( 255 ) UNIQUE NOT NULL,
	category VARCHAR ( 255 ) NOT NULL,
	contact_fname VARCHAR ( 255 ) NOT NULL,
	contact_lname VARCHAR ( 255 ) NOT NULL,
	email VARCHAR ( 255 ) PRIMARY KEY
);

INSERT INTO DEMO_SCHEMA.LEADS (business_name, category, contact_fname, contact_lname, email)
VALUES
('Wayne Enterprises', 'Manufacturing', 'Bruce', 'Wayne', 'bruce@wayne.com'),
('Duff Beer', 'Food and beverages', 'Jane', 'Doe', 'jane@duff.com'),
('Skynet', 'Robotics',	'Serena', 'Kogan', 'serena@skynet.com'),
('Bubba Gump', 'Restaurant', 'Tim', 'Forest', 'tim@bubba.com'),
('Sterling Cooper',	'Marketing', 'Roger', 'Sterling', 'roger@sc.com'),
('Oscorp',	'Technology', 'Noram', 'Osborn', 'norman@oscorp.com'),
('Pawtucket Brewery',	'Food and beverages', 'Peter', 'Griffin', 'peter@pb.com');

-- Following steps mentioned here => https://docs.castled.io/getting-started/Sources/configure-postgres to enable access for Castled

-----------------------------------USER CREATON STARTS-------------------------------------------------------
-- Create a new user CASTLED for connecting to postgres DB
CREATE USER CASTLED WITH PASSWORD 'castled';
-----------------------------------USER CREATON ENDS----------------------------------------------------------


-----------------------------------BOOK KEEPING SCHEMA ACCESS STARTS------------------------------------------
-- Create a private bookkeeping schema for storing sync data
CREATE SCHEMA CASTLED;

-- Give the CASTLED user full access to the bookkeeping schema
GRANT ALL ON SCHEMA CASTLED TO CASTLED;

-- Give CASTLED user access to all objects existing n the bookkeeping schema
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA CASTLED TO CASTLED;
-----------------------------------BOOK KEEPING SCHEMA ACCESS ENDS---------------------------------------------


-----------------------------------GRANT TABLE READ ONLY ACCESS STARTS------------------------------------------
-- Give access to CASTLED user to SEE your schema
GRANT USAGE ON SCHEMA DEMO_SCHEMA TO CASTLED;

-- Give READ ONLY access to  CASTLED user to read from all existing tables in your schema
GRANT SELECT ON ALL TABLES IN SCHEMA DEMO_SCHEMA TO CASTLED;

-- Give READ ONLY access to CASTLED user to read from all the future tables being created in your schema
ALTER DEFAULT PRIVILEGES IN SCHEMA DEMO_SCHEMA GRANT SELECT ON TABLES TO CASTLED;

-- Give access to  the CASTLED user to execute any existing functions in you schema
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA DEMO_SCHEMA TO CASTLED;

-- Give access to  the CASTLED user to execute any new functions added to this schema
ALTER DEFAULT PRIVILEGES IN SCHEMA DEMO_SCHEMA GRANT EXECUTE ON FUNCTIONS TO CASTLED;
-----------------------------------GRANT TABLE READ ONLY ACCESS ENDS---------------------------------------------