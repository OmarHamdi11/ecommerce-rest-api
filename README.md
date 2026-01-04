# 🛒 E-commerce REST API

A comprehensive, production-ready REST API for managing an e-commerce platform built with Spring Boot 3.5.7. This API provides complete functionality for product management, user authentication, shopping cart, wishlist, orders, reviews, and more.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-Database-blue.svg)](https://www.mysql.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 📋 Table of Contents

- [Features](#-features)
- [Technologies Used](#️-technologies-used)
- [Architecture](#-architecture)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Project Structure](#-project-structure)
- [Authentication](#-authentication)
- [Database Schema](#️-database-schema)
- [Usage Examples](#-usage-examples)
- [Docker Deployment](#-docker-deployment)
- [Contributing](#-contributing)
- [License](#-license)
- [Contact](#-contact)

## ✨ Features

### 🔐 Authentication & Authorization
- JWT-based authentication system
- Role-based access control (ADMIN, USER, EMPLOYEE)
- Secure password encryption using BCrypt
- Token-based session management
- Protected endpoints with Spring Security

### 📦 Product Management
- Complete CRUD operations for products
- Product variants with SKU system
- Multiple product images with primary image selection
- Product attributes (Color, Size, Material, Style)
- Product categorization and sub-categorization
- Featured products functionality
- Advanced product search and filtering
- Stock management and low stock alerts
- Brand management
- Slug-based URLs for SEO
- View count tracking

### 👤 User Management
- User profile management
- Profile image upload via ImgBB integration
- Multiple address management (shipping & billing)
- User authentication history
- Full name and phone number support

### 🛍️ Shopping Cart
- Add/remove items from cart
- Update item quantities
- Real-time stock validation
- Cart persistence across sessions
- Automatic price calculations
- Stock availability checking

### ❤️ Wishlist
- Add/remove products to wishlist
- Check product availability in wishlist
- Wishlist persistence
- Product tracking
- Quick access to favorite items

### 📦 Order Management
- Create orders from cart
- Multiple order statuses (Pending, Confirmed, Processing, Shipped, Delivered, Cancelled, Refunded)
- Multiple payment methods (Cash on Delivery, Credit Card, Debit Card, PayPal, Bank Transfer)
- Payment status tracking
- Order tracking with unique order numbers
- Admin order management
- User order history
- Order cancellation (for pending orders)
- Automatic stock deduction on order placement
- Shipping cost, tax, and discount calculations

### ⭐ Review System
- Product reviews and ratings (1-5 stars)
- Admin approval workflow for reviews
- Verified purchase indicators
- Average rating calculation
- Review title and detailed comments
- User-specific review limitations (one per product)

### 🖼️ Image Management
- Integration with ImgBB for image hosting
- Support for multiple product images
- Automatic image optimization
- Cover image functionality
- Primary image selection
- Bulk image upload
- Image display order management

### 📊 Admin Dashboard
- Dashboard statistics overview
- Sales reports with custom date ranges
- Recent activities tracking
- Low stock products monitoring
- Top selling products analytics
- Order status distribution
- Revenue tracking (today, total)
- User and product counts

### 🔍 Additional Features
- Pagination support for all list endpoints
- Advanced search with multiple filters
- Soft delete functionality for products
- Comprehensive error handling
- API documentation with Swagger/OpenAPI
- View count tracking for products
- CORS configuration for cross-origin requests
- Request/Response logging

## 🛠️ Technologies Used

### Core Framework
- **Spring Boot 3.5.7** - Main application framework
- **Spring Data JPA** - Database abstraction layer
- **Spring Security** - Authentication and authorization
- **Spring Web** - REST API development
- **Spring Validation** - Input validation

### Database
- **MySQL 8.0+** - Primary database (development)
- **PostgreSQL** - Production database support
- **Hibernate** - ORM implementation

### Security
- **JWT (JSON Web Tokens)** - Authentication tokens
- **BCrypt** - Password encryption
- **JJWT 0.13.0** - JWT implementation

### Documentation
- **SpringDoc OpenAPI 2.8.13** - API documentation (Swagger UI)

### Utilities
- **Lombok** - Reduce boilerplate code
- **ModelMapper 3.2.5** - Object mapping (DTO ↔ Entity)
- **ImgBB API** - Image hosting service

### Development Tools
- **Spring Boot DevTools** - Hot reload during development
- **Maven** - Dependency management and build tool

### Containerization
- **Docker** - Application containerization
- **Docker Compose** - Multi-container orchestration

## 🏗 Architecture

The project follows a **layered architecture** with clear separation of concerns:

```
┌─────────────────────────────────────┐
│     Presentation Layer              │
│  (Controllers, DTOs, Requests)      │
├─────────────────────────────────────┤
│      Business Logic Layer           │
│    (Services, Mappers)              │
├─────────────────────────────────────┤
│     Persistence Layer               │
│  (Repositories, Entities)           │
├─────────────────────────────────────┤
│        Database Layer               │
│     (MySQL/PostgreSQL)              │
└─────────────────────────────────────┘
```

### Key Design Patterns
- **Repository Pattern** - Data access abstraction
- **Service Layer Pattern** - Business logic encapsulation
- **DTO Pattern** - Data transfer between layers
- **Builder Pattern** - Entity construction (via Lombok)
- **Dependency Injection** - Loose coupling via Spring

## 🚀 Getting Started

### Prerequisites

Before running this application, ensure you have:

- **Java 21** or higher installed
- **Maven 3.6+** installed
- **MySQL 8.0+** installed and running (or PostgreSQL for production)
- **ImgBB API Key** (free from [ImgBB](https://api.imgbb.com/))
- **Git** for version control

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/OmarHamdi11/ecommerce-rest-api.git
cd ecommerce-rest-api
```

2. **Create MySQL Database**
```sql
CREATE DATABASE ecommerce;
```

3. **Configure Application Properties**

Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD

# JWT Configuration
app.jwt-secret=YOUR_SECRET_KEY_HERE
app-jwt-expiration-millisecond=86400000

# ImgBB Configuration
imgbb.api.key=YOUR_IMGBB_API_KEY
imgbb.api.url=https://api.imgbb.com/1/upload
```

4. **Generate JWT Secret Key**

Use OpenSSL to generate a secure key:

```bash
openssl rand -hex 32
```

5. **Build the Project**
```bash
mvn clean install
```

6. **Run the Application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Environment Variables

You can also configure the application using environment variables:

```bash
# Database
export DATABASE_HOST=localhost
export DATABASE_PORT=3306
export DATABASE_NAME=ecommerce
export DATABASE_USERNAME=root
export DATABASE_PASSWORD=your_password

# JWT
export JWT_SECRET=your_jwt_secret
export JWT_EXP_MILLI=86400000

# ImgBB
export IMGBB_API_KEY=your_imgbb_api_key

# Application
export SPRING_APP_NAME=ecommerce-rest-api
export DDL_AUTO=update
export PROFILES_ACTIVE=default
```

## 📚 API Documentation

Once the application is running, access the interactive API documentation at:

**Swagger UI:** `http://localhost:8080/swagger-ui.html`

**OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

### Main API Endpoints

#### Authentication (`/api/v1/auth`)
- `POST /register` - Register new user
- `POST /login` - User login

#### Products (`/api/v1/products`)
- `GET /` - Get all products (paginated)
- `GET /{id}` - Get product by ID
- `GET /slug/{slug}` - Get product by slug
- `POST /` - Create new product (Admin)
- `PUT /{id}` - Update product (Admin)
- `DELETE /{id}` - Delete product permanently (Admin)
- `DELETE /{id}/soft` - Soft delete product (Admin)
- `POST /search` - Search products
- `GET /featured` - Get featured products
- `GET /brands` - Get all brands
- `PATCH /{productId}/featured` - Toggle featured status (Admin)
- `PATCH /{productId}/active` - Toggle active status (Admin)

#### Product Images (`/api/v1/products`)
- `POST /{productId}/images` - Add single image (Admin)
- `POST /{productId}/images/bulk` - Add multiple images (Admin)
- `PATCH /{productId}/images/{imageId}/primary` - Set primary image (Admin)
- `DELETE /images/{imageId}` - Delete image (Admin)

#### Product SKUs (`/api/v1/products`)
- `POST /{productId}/skus` - Add SKU (Admin)
- `PUT /skus/{skuId}` - Update SKU (Admin)
- `GET /skus/{skuId}` - Get SKU by ID
- `GET /{productId}/skus` - Get all product SKUs
- `DELETE /skus/{skuId}` - Delete SKU (Admin)

#### Product Attributes (`/api/v1/products/attributes`)
- `POST /` - Create attribute (Admin)
- `GET /` - Get all attributes
- `GET /type/{type}` - Get attributes by type

#### Shopping Cart (`/api/v1/cart`)
- `GET /` - Get user cart
- `POST /items` - Add item to cart
- `PUT /items/{itemId}` - Update item quantity
- `DELETE /items/{itemId}` - Remove item from cart
- `DELETE /clear` - Clear cart

#### Wishlist (`/api/v1/wishlist`)
- `GET /` - Get user wishlist
- `POST /items/{productId}` - Add item to wishlist
- `DELETE /items/{productId}` - Remove item from wishlist
- `GET /check/{productId}` - Check if product in wishlist
- `DELETE /clear` - Clear wishlist

#### Orders (`/api/v1/orders`)
- `POST /` - Create order (User)
- `GET /{orderId}` - Get order by ID
- `GET /my-orders` - Get user orders (User)
- `GET /` - Get all orders (Admin)
- `PATCH /{orderId}/status` - Update order status (Admin)
- `PATCH /{orderId}/cancel` - Cancel order (User)

#### Reviews (`/api/v1/products`)
- `POST /reviews` - Add review (User)
- `GET /{productId}/reviews` - Get product reviews
- `PATCH /reviews/{reviewId}/approve` - Approve review (Admin)
- `DELETE /reviews/{reviewId}` - Delete review (Admin)

#### Categories (`/api/v1/categories`)
- `POST /` - Create category (Admin)
- `PUT /{id}` - Update category (Admin)
- `GET /{id}` - Get category by ID
- `GET /` - Get all categories
- `DELETE /{id}` - Delete category (Admin)
- `POST /{categoryId}/subcategories` - Create subcategory (Admin)

#### SubCategories (`/api/v1/categories`)
- `PUT /subcategories/{id}` - Update subcategory (Admin)
- `GET /{categoryId}/subcategories` - Get category subcategories
- `DELETE /subcategories/{id}` - Delete subcategory (Admin)

#### User Management (`/api/v1/user`)
- `GET /` - Get current user profile
- `POST /update` - Update user profile
- `PUT /profile-image` - Update profile image
- `POST /address` - Add new address
- `GET /address` - Get all addresses
- `PUT /address/{addressId}` - Update address
- `DELETE /address/{addressId}` - Delete address

#### Admin Dashboard (`/api/v1/admin/dashboard`)
- `GET /stats` - Get dashboard statistics
- `GET /sales-report` - Get sales report
- `GET /recent-activities` - Get recent activities
- `GET /low-stock-products` - Get low stock products
- `GET /top-products` - Get top selling products

## 📁 Project Structure

```
ecommerce-rest-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/ecommerce_rest_api/
│   │   │       ├── common/              # Shared utilities
│   │   │       │   ├── DTOs/           # Common DTOs
│   │   │       │   ├── exception/      # Exception handling
│   │   │       │   ├── response/       # Response wrappers
│   │   │       │   └── service/        # Common services
│   │   │       ├── config/             # Configuration classes
│   │   │       │   ├── CorsConfig.java
│   │   │       │   ├── ModelMapperConfig.java
│   │   │       │   ├── OpenApiConfig.java
│   │   │       │   └── SecurityConfig.java
│   │   │       ├── features/           # Feature modules
│   │   │       │   ├── admin/          # Admin dashboard
│   │   │       │   ├── auth/           # Authentication
│   │   │       │   ├── cart/           # Shopping cart
│   │   │       │   ├── category/       # Categories
│   │   │       │   ├── order/          # Orders
│   │   │       │   ├── product/        # Products
│   │   │       │   ├── review/         # Reviews
│   │   │       │   ├── user/           # Users
│   │   │       │   └── wishlist/       # Wishlist
│   │   │       ├── security/           # Security components
│   │   │       │   ├── CustomUserDetailsService.java
│   │   │       │   ├── JwtAuthenticationEntryPoint.java
│   │   │       │   ├── JwtAuthenticationFilter.java
│   │   │       │   └── JwtTokenProvider.java
│   │   │       └── utils/              # Utility classes
│   │   │           ├── AppConstants.java
│   │   │           └── SecurityUtils.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application_prod.properties
│   └── test/                           # Test files
├── docker-compose.yml                  # Docker configuration
├── pom.xml                             # Maven dependencies
└── README.md                           # This file
```

### Feature Module Structure

Each feature follows a consistent structure:

```
feature/
├── controller/      # REST endpoints
├── dto/            # Data Transfer Objects
├── entity/         # JPA entities
├── ENUM/           # Enumerations
├── mapper/         # Entity-DTO mappers
├── repository/     # Data access layer
└── service/        # Business logic
    ├── Service.java (interface)
    └── ServiceImpl.java (implementation)
```

## 🔐 Authentication

This API uses JWT (JSON Web Tokens) for authentication.

### Registration

```bash
POST /api/v1/auth/register
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "SecurePass123!",
  "confirmPassword": "SecurePass123!",
  "role": "USER",
  "gender": "MALE"
}
```

**Response:**
```json
{
  "success": true,
  "status": 201,
  "message": "User registered successfully",
  "timestamp": "2024-12-21T10:30:00",
  "data": null
}
```

### Login

```bash
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "SecurePass123!"
}
```

**Response:**
```json
{
  "success": true,
  "status": 200,
  "message": "Logged in Successfully",
  "timestamp": "2024-12-21T10:30:00",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "username": "johndoe",
    "role": "USER"
  }
}
```

### Using the Token

Include the token in the `Authorization` header for protected endpoints:

```bash
Authorization: Bearer <your-jwt-token>
```

### Password Requirements

Passwords must meet the following criteria:
- At least 8 characters long
- At least one uppercase letter
- At least one lowercase letter
- At least one digit
- At least one special character (@$!%*?&)

## 🗄️ Database Schema

### Main Entities

#### Users & Authentication
- **users** - User accounts and authentication
  - Primary fields: id, username, email, password, role, gender
  - Profile: fullName, phone_number, profileImageUrl
  - Timestamps: createdAt

- **addresses** - User delivery/billing addresses
  - Fields: title, address_line_1, address_line_2, country, city, postal_code, landmark, phone_number
  - Foreign key: user_id

#### Products
- **products** - Product information
  - Fields: name, slug, description, summary, coverImage, brand, isActive, isFeatured, viewCount
  - Timestamps: createdAt, updatedAt, deletedAt

- **product_skus** - Product variants
  - Fields: sku (unique), price, compareAtPrice, costPrice, quantity, lowStockThreshold, weight, isActive
  - Foreign key: product_id
  - Timestamps: createdAt, updatedAt, deletedAt

- **product_images** - Product images
  - Fields: imageUrl, imageDeleteUrl, imageId, displayOrder, isPrimary
  - Foreign key: product_id
  - Timestamp: createdAt

- **product_attributes** - Product attributes (color, size, etc.)
  - Fields: type (enum), value, displayValue, hexCode
  - Timestamp: createdAt

- **sku_attributes** - Many-to-many relationship between SKUs and attributes

#### Categories
- **categories** - Product categories
  - Fields: name, description
  - Timestamps: created_at, deleted_at

- **sub_categories** - Product subcategories
  - Fields: name, description
  - Foreign key: parent_id (category)
  - Timestamps: created_at, deleted_at

- **product_categories** - Many-to-many relationship between products and subcategories

#### Shopping
- **carts** - Shopping carts
  - Foreign key: user_id
  - Timestamps: created_at, updated_at

- **cart_items** - Cart items
  - Foreign keys: cart_id, sku_id
  - Fields: quantity, price
  - Timestamps: created_at, updated_at

- **wishlists** - User wishlists
  - Foreign key: user_id
  - Timestamps: created_at, updated_at

- **wishlist_items** - Wishlist items
  - Foreign keys: wishlist_id, product_id
  - Timestamp: added_at

#### Orders
- **orders** - Customer orders
  - Fields: orderNumber (unique), status, paymentMethod, paymentStatus
  - Prices: subtotal, shippingCost, tax, discount, total
  - Shipping: shippingName, shippingPhone, shippingAddressLine1, shippingAddressLine2, shippingCity, shippingCountry, shippingPostalCode
  - Foreign key: user_id
  - Timestamps: created_at, updated_at, delivered_at

- **order_items** - Order items
  - Foreign keys: order_id, sku_id
  - Fields: productName, skuCode, quantity, price, subtotal

#### Reviews
- **reviews** - Product reviews and ratings
  - Foreign keys: product_id, user_id
  - Fields: rating (1-5), title, comment, isVerifiedPurchase, isApproved
  - Timestamps: created_at, updated_at

### Entity Relationships

```
User 1----* Address
User 1----1 Cart
User 1----1 Wishlist
User 1----* Order
User 1----* Review

Product 1----* ProductSku
Product 1----* ProductImage
Product *----* SubCategory
Product 1----* Review

ProductSku *----* ProductAttribute
ProductSku 1----* CartItem
ProductSku 1----* OrderItem

Cart 1----* CartItem
Wishlist 1----* WishlistItem

Category 1----* SubCategory
Order 1----* OrderItem
```

## 💡 Usage Examples

### Creating a Product (Admin)

```bash
POST /api/v1/products
Authorization: Bearer <admin-token>
Content-Type: multipart/form-data

data: {
  "name": "Premium T-Shirt",
  "description": "High-quality cotton t-shirt with comfortable fit",
  "summary": "Comfortable and stylish",
  "brand": "Fashion Brand",
  "isActive": true,
  "isFeatured": true,
  "subCategoryIds": [1, 2],
  "skus": [
    {
      "sku": "TSHIRT-001-S",
      "price": 29.99,
      "compareAtPrice": 39.99,
      "quantity": 100,
      "attributeIds": [1, 5]
    }
  ]
}
coverImage: <file>
```

### Searching Products

```bash
POST /api/v1/products/search?pageNo=0&pageSize=10
Content-Type: application/json

{
  "keyword": "shirt",
  "categoryIds": [1, 2],
  "minPrice": 20.0,
  "maxPrice": 50.0,
  "brand": "Fashion Brand",
  "isActive": true,
  "sortBy": "price",
  "sortDirection": "ASC"
}
```

### Adding Items to Cart

```bash
POST /api/v1/cart/items
Authorization: Bearer <user-token>
Content-Type: application/json

{
  "skuId": 1,
  "quantity": 2
}
```

### Creating an Order

```bash
POST /api/v1/orders
Authorization: Bearer <user-token>
Content-Type: application/json

{
  "paymentMethod": "CASH_ON_DELIVERY",
  "shippingCost": 5.00,
  "shippingName": "John Doe",
  "shippingPhone": "+1234567890",
  "shippingAddressLine1": "123 Main St",
  "shippingCity": "New York",
  "shippingCountry": "USA",
  "shippingPostalCode": "10001"
}
```

### Adding a Review (User)

```bash
POST /api/v1/products/reviews
Authorization: Bearer <user-token>
Content-Type: application/json

{
  "productId": 1,
  "rating": 5,
  "title": "Excellent Product!",
  "comment": "Very satisfied with the quality and delivery time. Highly recommended!"
}
```

### Admin Dashboard Statistics

```bash
GET /api/v1/admin/dashboard/stats
Authorization: Bearer <admin-token>
```

**Response:**
```json
{
  "success": true,
  "status": 200,
  "message": "Dashboard stats retrieved successfully",
  "data": {
    "totalProducts": 150,
    "totalUsers": 1200,
    "totalOrders": 850,
    "pendingOrders": 25,
    "processingOrders": 40,
    "deliveredOrders": 750,
    "totalRevenue": 125000.00,
    "todayOrders": 15,
    "todayRevenue": 2500.00,
    "lowStockProducts": 8,
    "pendingReviews": 12
  }
}
```

## 🐳 Docker Deployment

The application includes Docker support for easy deployment.

### Using Docker Compose

1. **Start the application and database:**

```bash
docker-compose up -d
```

This will start:
- MySQL database on port 3307
- Application on port 8080

2. **View logs:**

```bash
docker-compose logs -f app
```

3. **Stop the application:**

```bash
docker-compose down
```

4. **Stop and remove volumes:**

```bash
docker-compose down -v
```

### Docker Compose Configuration

The `docker-compose.yml` includes:

- **MySQL Service**
  - Image: mysql:latest
  - Database: ecommerce
  - Port: 3307:3306
  - Health check enabled
  - Persistent volume for data

- **Application Service**
  - Image: omarellafy/ecommerce-app:0.5.Release
  - Port: 8080:8080
  - Depends on MySQL (waits for health check)
  - Environment variables for database connection

### Building Custom Docker Image

```bash
# Build with Maven
mvn clean package -DskipTests

# Build Docker image
docker build -t ecommerce-app:latest .

# Run with custom image
docker run -p 8080:8080 \
  -e DATABASE_HOST=host.docker.internal \
  -e DATABASE_PORT=3306 \
  -e DATABASE_NAME=ecommerce \
  -e DATABASE_USERNAME=root \
  -e DATABASE_PASSWORD=your_password \
  ecommerce-app:latest
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Coding Standards

- Follow Java naming conventions
- Write meaningful commit messages
- Add comments for complex logic
- Write unit tests for new features
- Update documentation as needed

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](https://opensource.org/licenses/MIT) file for details.

## 📧 Contact

**Omar Hamdi**
- Email: omarellafy1@gmail.com
- GitHub: [@OmarHamdi11](https://github.com/OmarHamdi11)
- Project Link: [https://github.com/OmarHamdi11/ecommerce-rest-api](https://github.com/OmarHamdi11/ecommerce-rest-api)

---

## 🙏 Acknowledgments

- Spring Boot team for the amazing framework
- ImgBB for image hosting services
- All contributors who help improve this project

## 🔮 Future Enhancements

- [ ] Payment gateway integration (Stripe, PayPal)
- [ ] Email notifications for orders and authentication
- [ ] SMS notifications for order updates
- [ ] Advanced analytics dashboard with charts
- [ ] Product recommendations based on user behavior
- [ ] Multi-language support (i18n)
- [ ] Advanced inventory management
- [ ] Coupon/discount system
- [ ] Customer support chat system
- [ ] Product comparison feature
- [ ] Export orders to PDF/Excel
- [ ] Mobile app integration
- [ ] Social media authentication
- [ ] Product import/export functionality
- [ ] Advanced reporting system

---

**Made with ❤️ by Omar Hamdi**
