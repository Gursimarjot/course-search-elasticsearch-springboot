
# 🧠 Course Search - Spring Boot + Elasticsearch

This project implements a course search system using **Spring Boot** and **Elasticsearch**. It allows full-text search across course data with filtering, sorting, and pagination.

> ✅ Assignment A Completed  

---

## 📦 Features

- 🔍 Full-text search on course title & description
- 🧮 Range filters: age, price
- 🎯 Exact filters: category, type
- 📅 Filter by next session date
- 📊 Sorting: by session date or price
- 📄 Pagination: page & size params
- 🚀 Loads 50+ sample course documents on startup

---

## 🛠️ Prerequisites

- Java 17+
- Gradle
- Docker + Docker Compose

---

## 🐳 Run Elasticsearch with Docker

Spin up a single-node cluster:

```bash
docker-compose up -d
```

Verify it’s running:

```bash
curl http://localhost:9200
```

You should get a JSON response like:
```json
{
  "name" : "elasticsearch",
  "cluster_name" : "docker-cluster",
  ...
}
```

---

## ▶️ Run the Spring Boot App

```bash
./gradlew bootRun
```

App runs at: [http://localhost:8080](http://localhost:8080)

At startup, sample courses from `src/main/resources/sample-courses.json` are bulk-indexed into Elasticsearch.

---

## 🔎 API: Course Search

### Endpoint
```
GET /api/search
```

### Query Parameters

| Param        | Type      | Description                                |
|--------------|-----------|--------------------------------------------|
| `q`          | String    | Full-text keyword search                   |
| `category`   | String    | Filter by category (e.g., Math, Art)       |
| `type`       | String    | Filter by type (ONE_TIME, COURSE, CLUB)    |
| `minAge`     | Integer   | Minimum age filter                         |
| `maxAge`     | Integer   | Maximum age filter                         |
| `minPrice`   | Decimal   | Filter for price >= minPrice               |
| `maxPrice`   | Decimal   | Filter for price <= maxPrice               |
| `startDate`  | ISO-8601  | Filter courses starting on or after date   |
| `sort`       | String    | `upcoming` (default), `priceAsc`, `priceDesc` |
| `page`       | Integer   | Default = 0                                |
| `size`       | Integer   | Default = 10                               |

---

### 🧪 Example CURL Requests

#### 1. Search by keyword
```bash
curl "http://localhost:8080/api/search?q=science"
```

#### 2. Filter by category and age range
```bash
curl "http://localhost:8080/api/search?category=Science&minAge=8&maxAge=12"
```

#### 3. Sort by price (low to high)
```bash
curl "http://localhost:8080/api/search?sort=priceAsc"
```

#### 4. Filter by start date
```bash
curl "http://localhost:8080/api/search?startDate=2025-07-20T00:00:00Z"
```

---

### ✅ Sample JSON Response
```json
{
  "total": 12,
  "courses": [
    {
      "id": "course-12",
      "title": "Dinosaur Explorers",
      "category": "Science",
      "price": 29.99,
      "nextSessionDate": "2025-07-22T15:00:00Z"
    },
    ...
  ]
}
```

---

## 📁 Project Structure

```
src/main/java/com/example/course_search/
├── config/         # Elasticsearch config
├── controller/     # API layer
├── model/          # CourseDocument class
├── repository/     # Spring Data Elasticsearch repo
├── service/        # Search + Data loader services
```

---

## 📘 Sample Data

Stored in:
```
src/main/resources/sample-courses.json
```

Includes 50+ course records with different categories, prices, ages, and session dates.

---

## 🤝 Author

Made with ❤️ by [Gursimarjot Kaur](https://github.com/Gursimarjot)

GitHub Repo: [course-search-elasticsearch-springboot](https://github.com/Gursimarjot/course-search-elasticsearch-springboot)

---

## ✅ Status

- [x] Elasticsearch setup via Docker
- [x] Spring Boot project + indexing logic
- [x] Search API with filters and sorting
- [x] Sample data
- [x] README with curl examples

