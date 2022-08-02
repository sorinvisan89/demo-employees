CREATE
    TABLE
        employees(
            employee_id SERIAL NOT NULL,
            name VARCHAR(255) NOT NULL,
            description TEXT NOT NULL,
            age INTEGER NOT NULL,
            department_id INTEGER NOT NULL,
            PRIMARY KEY(employee_id),
            UNIQUE(name)
        );
