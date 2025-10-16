===========================================================
AIRPLANE TICKET BOOKING MANAGEMENT SYSTEM - README
===========================================================

PROJECT DESCRIPTION:
---------------------
This is a desktop-based Airplane Ticket Booking Management System developed using Java (OOP), JavaFX, Scene Builder, and PostgreSQL. The system allows three types of users—Customer, Operator, and Administrator—to interact with flight, booking, and user data through a role-based UI.

KEY FEATURES:
-------------
- Secure login with role-based dashboards
- Flight search with filtering by airport and time range
- Flight booking functionality (including via transit airports)
- Booking view for customers and operators
- Report generation by operators and admins
- Admin controls to create, update, activate, and deactivate users
- Logout and back navigation options across all interfaces

TECHNOLOGIES USED:
------------------
- Java 17+
- JavaFX
- Scene Builder (for UI layout)
- PostgreSQL
- pgAdmin (for DB management)
- JDBC (for database connectivity)

DATABASE STRUCTURE:
-------------------
The system uses 6 primary tables:
1. users
2. flights
3. bookings
4. airports
5. aircrafts
6. seats

SETUP INSTRUCTIONS:
-------------------
1. Ensure Java 17+ and PostgreSQL are installed.
2. Import the PostgreSQL database using the provided .sql file (use pgAdmin or command line).
3. Open the project in NetBeans or your preferred Java IDE.
4. Configure database credentials in the DB connection class.
5. Run the MainApp class to start the application.

LOGIN DETAILS (for testing):
----------------------------
Admin:
- Username: Admin
- Password: admin123

Operator:
- Username: saman_67
- Password: saman123

Customer:
- Username: adamsmith
- Password: adam123

NOTES:
------
- All users can log out or navigate back using the provided buttons.
- View bookings feature may take a few extra seconds to update the table.
- Magic numbers are avoided in code; constants are used for maintainability.
- Passwords should be stored securely (e.g., using hashing in real applications).

CONTACT:
--------
For further support or queries, please contact:
nidukajayathunga886@gmail.com

