INSERT INTO category (category_id, title, colour, account_id, created_at, updated_at, version)
VALUES
    ('2cb7304c-0116-464c-94e5-e4ea8b799461', 'Test Category1 Controller', '#CBDFBC', '5199a3a2-81ca-45d7-88e4-62310dcc09e1', '2024-11-13', '2024-11-13', 1),
    ('3dc7304c-0226-464c-94e5-e4ea8b799462', 'Test Category2 Controller', '#DDEEFF', '5199a3a2-81ca-45d7-88e4-62310dcc09e1', '2024-11-13', '2024-11-13', 1);

INSERT INTO task (task_id, title, description, status, date, hours, category_id, created_at, updated_at, version)
VALUES
    ('09f535f1-d205-4cf0-b5fe-b31be8605b91', 'Test Task1 Controller', 'Test Task1 Description', 'TODO',
     '2024-11-13', 1.5, '2cb7304c-0116-464c-94e5-e4ea8b799461', '2024-11-13', '2024-11-13', 1),
    ('19f535f1-d205-4cf0-b5fe-b31be8605b92', 'Test Task2 Controller', 'Test Task2 Description', 'TODO',
     '2024-11-14', 4.5, '2cb7304c-0116-464c-94e5-e4ea8b799461', '2024-11-14', '2024-11-14', 1);