CREATE
    TABLE
        departments(
            department_id SERIAL NOT NULL,
            name VARCHAR(255) NOT NULL,
            description TEXT NOT NULL,
            PRIMARY KEY(department_id),
            UNIQUE(name)
        );
