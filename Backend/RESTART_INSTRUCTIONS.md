# Fix Instructions

## Problem
PostgreSQL table columns don't match the Java entity fields.

## Solution Steps

1. **Stop your Spring Boot application**

2. **The application.properties has been updated to recreate tables**
   - Changed `spring.jpa.hibernate.ddl-auto=create` (will drop and recreate tables)
   - Added `spring.jpa.show-sql=true` (to see SQL queries)

3. **Restart Spring Boot application**
   ```bash
   cd AIDigitalMarketingBackend/Backend
   ./mvnw spring-boot:run
   ```

4. **Test the signup** - It should work now

5. **After successful signup, change back to update mode**
   - Open `application.properties`
   - Change `spring.jpa.hibernate.ddl-auto=create` to `spring.jpa.hibernate.ddl-auto=update`
   - Restart the application

## What Changed
- Added explicit `@Column` annotations to User and Business entities
- Column names now match PostgreSQL snake_case convention
- Tables will be recreated with correct schema
