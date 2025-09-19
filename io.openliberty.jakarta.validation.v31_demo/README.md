# Jakarta Validation

Jakarta Validation is a specification which allows you to express constraints on object models via annotations and validate those constraints.

# Changes between Jakarta Validation 3.1 and Jakarta Bean Validation 3.0

- Jakarta Validation 3.1 will provide all necessary support for Java records.
- The rename of the specification from Jakarta Bean Validation to Jakarta Validation.
- In Open Liberty new validation feature will be validation-3.1. The symbolic name will be io.openliberty.beanValidation-3.1 to allow ibm.  tolerates. WLP-AlsoKnownAs will be used to notify users who try to use beanValidation-3.1.
- The minimum required Java version is set to 17.


# Jakarta Validation 3.1 Demo

This project demonstrates Jakarta Validation 3.1 new features.

## Prerequisites

- Java 17 or later
- Maven 3.6 or later

## Project Structure

The project is structured as follows:

- `src/main/java/io/openliberty/jakarta/validation/v31demo/model/`: Contains the model classes with validation constraints
- `src/main/java/io/openliberty/jakarta/validation/v31demo/web/`: Contains the servlet that demonstrates Jakarta validation 3.1
- `src/main/webapp/`: Contains the web resources
- `src/main/liberty/config/`: Contains the Liberty server configuration

## Building and Running the Application

### 1. Build the Application

```bash
cd io.openliberty.jakarta.validation.v31_demo
mvn clean package
```

### 2. Run the Application

```bash
mvn liberty:run
```

This will start the Liberty server and deploy the application.

### 3. Access the Application

Open your browser and navigate to:

```
http://localhost:9080/
```

You will see a list of available demos that you can run.

## Available Demos

The following demos are available:

1. **Basic Record Validation**: Demonstrates basic validation constraints on a Java record.
2. **Record ValidateProperty and ValidateValue Demo**: Shows how to validate specific properties or values of a record.
3. **Validate Record Parameters Demo**: Demonstrates validation of method parameters and return values on a record.
4. **Nested Records Demo**: Shows cascaded validation with nested records.
5. **Convert Groups Records Demo**: Demonstrates group conversion with records.
6. **Group Sequence Records Demo**: Shows group sequence validation with records.

## Demo Details

### Basic Record Validation Demo

This demo validates a `Person` record with a `@NotNull` constraint on the `name` field.

### Record ValidateProperty and ValidateValue Demo

This demo demonstrates how to use `validateProperty` and `validateValue` methods on a record.

### Validate Record Parameters Demo

This demo shows how to validate method parameters and return values on a record using the executable validator.

### Nested Records Demo

This demo demonstrates cascaded validation with nested records using the `@Valid` annotation.

### Convert Groups Records Demo

This demo shows how to convert validation groups using the `@ConvertGroup` annotation.

### Group Sequence Records Demo

This demo demonstrates group sequence validation using the `@GroupSequence` annotation.

## Stopping the Application

To stop the Liberty server, press `Ctrl+C` in the terminal where the server is running, or run:

```bash
mvn liberty:stop
```

## Additional Resources

- [Jakarta Validation 3.1 Specification](https://jakarta.ee/specifications/bean-validation/3.1/)
- [Open Liberty Documentation](https://openliberty.io/docs/)