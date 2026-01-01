# 🛒 E-commerce REST API

A comprehensive, production-ready REST API for managing an e-commerce platform built with Spring Boot 3.5.7. This API provides complete functionality for product management, user authentication, shopping cart, wishlist, orders, reviews, and more.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-Database-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 📋 Table of Contents

- [Features](#-features)
- [Technologies Used](#️-technologies-used)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Project Structure](#-project-structure)
- [Authentication](#-authentication)
- [Database Schema](#️-database-schema)
- [Usage Examples](#-usage-examples)
- [Contributing](#-contributing)
- [License](#-license)
- [Contact](#-contact)

## ✨ Features

### 🔐 Authentication & Authorization
- JWT-based authentication system
- Role-based access control (ADMIN, USER, EMPLOYEE)
- Secure password encryption using BCrypt
- Token-based session management

### 📦 Product Management
- Complete CRUD operations for products
- Product variants with SKU system
- Multiple product images with primary image selection
- Product attributes (Color, Size, Material, Style)
- Product categorization and sub-categorization
- Featured products functionality
- Product search and filtering
- Stock management and low stock alerts
- Brand management

### 👤 User Management
- User profile management
- Profile image upload via ImgBB integration
- Multiple address management
- User authentication history

### 🛍️ Shopping Cart
- Add/remove items from cart
- Update item quantities
- Real-time stock validation
- Cart persistence across sessions
- Price calculations

### ❤️ Wishlist
- Add/remove products to wishlist
- Check product availability
- Wishlist persistence
- Product tracking

### 📦 Order Management
- Create orders from cart
- Multiple order statuses (Pending, Processing, Shipped, Delivered, Cancelled)
- Payment method selection
- Order tracking
- Admin order management
- User order history

### ⭐ Review System
- Product reviews and ratings
- Admin approval workflow for reviews
- Verified purchase indicators
- Average rating calculation

### 🖼️ Image Management
- Integration with ImgBB for image hosting
- Support for multiple product images
- Automatic image optimization
- Cover image functionality

### 📊 Admin Dashboard
- Dashboard statistics
- Sales reports with date ranges
- Recent activities tracking
- Low stock products monitoring
- Top selling products

### 🔍 Additional Features
- Pagination support for all list endpoints
- Advanced search and filtering
- Soft delete functionality
- Comprehensive error handling
- API documentation with Swagger/OpenAPI
- View count tracking for products

## 🛠️ Technologies Used

### Core Framework
- **Spring Boot 3.5.7** - Main application framework
- **Spring Data JPA** - Database abstraction layer
- **Spring Security** - Authentication and authorization
- **Spring Web** - REST API development

### Database
- **MySQL** - Primary database
- **Hibernate** - ORM implementation

### Security
- **JWT (JSON Web Tokens)** - Authentication tokens
- **BCrypt** - Password encryption
- **JJWT 0.13.0** - JWT implementation

### Documentation
- **SpringDoc OpenAPI 2.8.13** - API documentation (Swagger UI)

### Utilities
- **Lombok** - Reduce boilerplate code
- **ModelMapper 3.2.5** - Object mapping
- **ImgBB API** - Image hosting service

### Development Tools
- **Spring Boot DevTools** - Hot reload during development
- **Maven** - Dependency management

## 🚀 Getting Started

### Prerequisites

Before running this application, ensure you have:

- **Java 21** or higher installed
- **Maven 3.6+** installed
- **MySQL 8.0+** installed and running
- **ImgBB API Key** (free from [ImgBB](https://api.imgbb.com/))

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

4. **Build the Project**
```bash
mvn clean install
```

5. **Run the Application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Configuration

#### Generate JWT Secret Key

You can generate a secure JWT secret key using:

```bash
openssl rand -hex 32
```

#### ImgBB API Key

1. Visit [ImgBB API](https://api.imgbb.com/)
2. Sign up for a free account
3. Get your API key from the dashboard
4. Add it to `application.properties`

## 📚 API Documentation

Once the application is running, access the interactive API documentation at:

**Swagger UI:** `http://localhost:8080/swagger-ui.html`

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
- `DELETE /{id}` - Delete product (Admin)
- `POST /search` - Search products
- `GET /featured` - Get featured products
- `GET /brands` - Get all brands

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
│   │   │       └── utils/              # Utility classes
│   │   └── resources/
│   │       └── application.properties  # Configuration
│   └── test/                           # Test files
├── pom.xml                             # Maven dependencies
└── README.md                           # This file
```

## 🔒 Authentication

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

## 🗄️ Database Schema

### Main Entities

- **users** - User accounts and authentication
- **addresses** - User delivery addresses
- **products** - Product information
- **product_skus** - Product variants
- **product_images** - Product images
- **product_attributes** - Product attributes (color, size, etc.)
- **categories** - Product categories
- **sub_categories** - Product subcategories
- **reviews** - Product reviews and ratings
- **carts** - Shopping carts
- **cart_items** - Cart items
- **wishlists** - User wishlists
- **wishlist_items** - Wishlist items
- **orders** - Customer orders
- **order_items** - Order items

## 💡 Usage Examples

### Creating a Product (Admin)

```bash
POST /api/v1/products
Authorization: Bearer <admin-token>
Content-Type: multipart/form-data

data: {
  "name": "Premium T-Shirt",
  "description": "High-quality cotton t-shirt",
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
  "comment": "Very satisfied with the quality and delivery time."
}
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

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
- [ ] Advanced analytics dashboard with charts
- [ ] Product recommendations based on user behavior
- [ ] Multi-language support (i18n)
- [ ] Advanced inventory management
- [ ] Coupon/discount system
- [ ] Customer support chat system
- [ ] Product comparison feature
- [ ] Export orders to PDF/Excel

---

**Made with ❤️ by Omar Hamdi**