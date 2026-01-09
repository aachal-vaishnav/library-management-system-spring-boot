# 1️⃣ PROJECT OVERVIEW 
This project is a **Spring Boot REST API** with:

* **Library features**

    * Books CRUD
    * Issue book
    * Return book
* **Authentication & Authorization**

    * Login / Register
    * JWT based security
    * Role-based access (`ADMIN`, `USER`)
* **Database**

    * MySQL
    * JPA / Hibernate
* **Security**

    * Spring Security
    * Stateless authentication using JWT

---

# 2️⃣ SPRING BOOT MAIN CLASS

```java
@SpringBootApplication
public class LibraryManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementApplication.class, args);
    }
}
```

### `@SpringBootApplication`

This is **3 annotations combined**:

1. `@Configuration` → marks class as configuration
2. `@EnableAutoConfiguration` → auto configures beans
3. `@ComponentScan` → scans packages for `@Component`, `@Service`, etc.

📌 **This is the entry point of your application**

---

# 3️⃣ ENTITY LAYER (DATABASE MODEL)

---

## USER ENTITY

```java
@Entity
@Table(name = "users")
@Data
public class User implements UserDetails
```

### `@Entity`

* Tells Hibernate → **this class = database table**

### `@Table(name = "users")`

* Table name explicitly set to `users`

### `@Data` (Lombok)

Auto-generates:

* getters
* setters
* toString
* equals & hashCode

---

## PRIMARY KEY

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

* `@Id` → primary key
* `IDENTITY` → auto-increment in DB

---

## USERDETAILS INTERFACE (VERY IMPORTANT)

```java
public class User implements UserDetails
```

### WHY `UserDetails`?

Spring Security **does NOT understand your User class**.
It only understands `UserDetails`.

By implementing `UserDetails`, you are saying:

> “Spring Security, you can use my User class for authentication.”

---

## ROLES

```java
@ElementCollection(fetch = FetchType.EAGER)
private Set<String> roles;
```

### 🔹 `@ElementCollection`

* Used for **collection of basic types**
* No separate entity
* Stored in **separate table**

### DB STRUCTURE CREATED

| user_id | roles      |
| ------- | ---------- |
| 1       | ROLE_USER  |
| 1       | ROLE_ADMIN |

📌 **You were correct**:

* This table is **fully owned** by User
* If User is deleted → roles are deleted automatically

---

### 🔹 Why `FetchType.EAGER`?

* When a User loads → **roles load immediately**
* Spring Security **needs roles at login time**

If `LAZY`:

* roles would load later
* causes `LazyInitializationException`
* authentication may fail

---

### 🔹 FetchType.LAZY (comparison)

| EAGER                 | LAZY                             |
| --------------------- | -------------------------------- |
| Loads immediately     | Loads only when accessed         |
| More DB calls upfront | Efficient but risky for security |
| Needed for roles      | Used in large collections        |

---

## AUTHORITIES MAPPING

```java
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
}
```

### Why this?

Spring Security **does not understand String roles**.

It understands:

```java
GrantedAuthority
```

So:

* `"ROLE_ADMIN"` → wrapped into `SimpleGrantedAuthority`
* `stream()` converts collection
* `map()` transforms
* `collect()` returns List

---

# 4️⃣ BOOK ENTITY

```java
@Entity
@Data
public class Book
```

Fields are self-explanatory.

---

# 5️⃣ RECORD ISSUED ENTITY

```java
@ManyToOne
@JoinColumn(name = "user_id")
private User user;
```

### `@ManyToOne`

* Many issue records → one user

### `@JoinColumn(name="user_id")`

* Foreign key column in DB

DB:

```sql
record_issued.user_id → users.id
```

Same for Book:

```java
@ManyToOne
@JoinColumn(name = "book_id")
private Book book;
```

---

# 6️⃣ DTOs (WHY THEY EXIST)

DTO = **Data Transfer Object**

Purpose:

* Don’t expose entity directly
* Control request & response

---

## `@Builder` (IMPORTANT)

```java
@Builder
```

### Why use `@Builder`?

Instead of:

```java
LoginResponseDTO dto = new LoginResponseDTO(a,b,c);
```

You can do:

```java
LoginResponseDTO.builder()
    .token(token)
    .username(username)
    .roles(roles)
    .build();
```

✔ Cleaner
✔ Avoids constructor confusion
✔ Readable

---

# 7️⃣ REPOSITORY LAYER

```java
public interface UserRepository extends JpaRepository<User,Long>
```

### JpaRepository gives you:

* `save()`
* `findById()`
* `findAll()`
* `deleteById()`
* `existsById()`

### Custom method

```java
Optional<User> findByUsername(String userName);
```

Spring automatically generates SQL.

---

# 8️⃣ SECURITY CONFIG (MOST IMPORTANT)

---

## @EnableWebSecurity

* Enables Spring Security

## @EnableMethodSecurity

* Enables `@PreAuthorize`

---

## SecurityFilterChain

```java
http
    .csrf(csrf -> csrf.disable())
```

### Why disable CSRF?

* JWT is stateless
* No cookies
* CSRF not needed

---

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/auth/**").permitAll()
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .anyRequest().authenticated()
)
```

Meaning:

* `/auth/**` → public
* `/admin/**` → ADMIN only
* everything else → logged in

---

## Stateless Session

```java
.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
```

Means:

* No HTTP session
* JWT controls authentication

---

## addFilterBefore

```java
.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
```

JWT filter runs **before login filter**

---

# 9️⃣ @PreAuthorize

```java
@PreAuthorize("hasRole('ADMIN')")
```

Checks:

* User must have `ROLE_ADMIN`
* Method not executed if false

---

# 🔟 CUSTOM USER DETAILS SERVICE

```java
implements UserDetailsService
```

### Why?

Spring Security **calls this automatically** during authentication.

Method:

```java
loadUserByUsername()
```

You return:

* User
* Password
* Roles

---

# 1️⃣1️⃣ AUTHENTICATION SERVICE

---

## Register

```java
roles.add("ROLE_USER");
```

📌 Spring Security **requires ROLE_ prefix**

---

## Login Flow

```java
authenticationManager.authenticate(...)
```

* Validates password
* Calls `CustomUserDetailsService`
* Uses `PasswordEncoder`

---

## JWT Creation

```java
String token = jwtService.generateToken(user);
```

---

# 1️⃣2️⃣ JWT CONCEPT (VERY IMPORTANT)

JWT = **JSON Web Token**

### Format:

```
header.payload.signature
```

---

## Bearer

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### What is Bearer?

* Authentication scheme
* Means: “Whoever bears this token is authenticated”

---

## Header

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

---

## Payload (Claims)

```json
{
  "sub": "username",
  "iat": 123456,
  "exp": 123999
}
```

* `sub` → subject (username)
* `iat` → issued at
* `exp` → expiration

---

## Signature

* Created using **secret key**
* Verifies token integrity

---

## Secret Key Rules

✔ At least **32 characters (256 bits)**
✔ Cryptographically random
✔ Uppercase + lowercase + number + special

Reason:

* HS256 requires 256-bit key
* Otherwise → runtime exception

---

# 1️⃣3️⃣ JWT SERVICE (LINE BY LINE)

---

## extractUsername

```java
return extractClaim(jwtToken, Claims::getSubject);
```

### `Claims::getSubject`

* Method reference
* Same as: `claims -> claims.getSubject()`

---

## Generic Method

```java
private <T> T extractClaim(String jwtToken, Function<Claims, T> fn)
```

Why generic?

* Reuse for expiration, subject, etc.

---

## extractAllClaims

```java
Jwts.parser()
    .verifyWith(getSigninKey())
    .build()
    .parseSignedClaims(jwtToken)
    .getPayload();
```

Steps:

1. Parse token
2. Verify signature
3. Return claims

---

## generateToken OVERLOADING

```java
generateToken(UserDetails userDetails)
```

calls:

```java
generateToken(Map, UserDetails)
```

This is **method overloading**.

---

# 1️⃣4️⃣ JWT FILTER (CORE LOGIC)

---

## OncePerRequestFilter

### Why?

* Executes once per request
* Avoids multiple authentication
* Important for async requests

---

## Filter Steps (YOUR EXACT STEPS)

### 1️⃣ Get Header

```java
request.getHeader("Authorization");
```

---

### 2️⃣ substring(7)

```java
Bearer<space> → 7 characters
```

So:

```
"Bearer abc.def.ghi"
       ↑ index 7
```

---

### 3️⃣ Extract Username

```java
jwtService.extractUsername(jwt);
```

---

### 4️⃣ Load User

```java
userDetailsService.loadUserByUsername()
```

---

### 5️⃣ Validate Token

```java
jwtService.isTokenValid()
```

---

### 6️⃣ Create Authentication

```java
UsernamePasswordAuthenticationToken
```

Contains:

* principal (user)
* credentials (null)
* authorities

---

### 7️⃣ Set Security Context

```java
SecurityContextHolder.getContext().setAuthentication(authToken);
```

Now Spring knows:
✔ User is authenticated
✔ Roles are available

---

# 1️⃣5️⃣ STREAM()

```java
roles.stream().map().collect()
```

### Why stream?

* Functional style
* Clean transformation
* Less boilerplate

---

# 1️⃣6️⃣ SecurityContextHolder

```java
SecurityContextHolder.getContext().getAuthentication().getName();
```

Meaning:

* Get current logged-in user
* `.getName()` → username

---

# 1️⃣7️⃣ ISSUE & RETURN BOOK LOGIC

✔ Book quantity reduced
✔ Availability updated
✔ User identified from JWT

---

# 1️⃣8️⃣ APPLICATION.YML

```yaml
jwt:
  secretkey:
  expiration:
```

Injected using:

```java
@Value("${jwt.secretkey}")
```
---