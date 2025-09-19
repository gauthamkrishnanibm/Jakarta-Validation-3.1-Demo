/*******************************************************************************
 * Copyright (c) 2025 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package io.openliberty.jakarta.validation.v31demo.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Set;

import io.openliberty.jakarta.validation.v31demo.model.Company;
import io.openliberty.jakarta.validation.v31demo.model.EmailAddress;
import io.openliberty.jakarta.validation.v31demo.model.Employee;
import io.openliberty.jakarta.validation.v31demo.model.Person;
import io.openliberty.jakarta.validation.v31demo.model.Registration;
import io.openliberty.jakarta.validation.v31demo.model.RegistrationChecks;
import io.openliberty.jakarta.validation.v31demo.model.SignupForm;
import io.openliberty.jakarta.validation.v31demo.model.ValidationOrder;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

/**
 * Servlet that demonstrates Jakarta Validation 3.1 feature.
 */
@WebServlet("/Validation31TestServlet")
public class Validation31TestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Inject
    private Validator validator;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Jakarta Validation 3.1 Demo Results</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; line-height: 1.6; }");
        out.println(".success { color: green; font-weight: bold; padding: 10px; background-color: #e8f5e9; border-radius: 5px; }");
        out.println(".failure { color: #d32f2f; font-weight: bold; padding: 10px; background-color: #ffebee; border-radius: 5px; }");
        out.println(".result { margin: 20px 0; padding: 20px; border: 1px solid #ddd; border-radius: 5px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
        out.println(".code { background-color: #f5f5f5; padding: 15px; border-left: 4px solid #2196f3; font-family: monospace; overflow-x: auto; }");
        out.println(".demo-description { background-color: #e3f2fd; padding: 15px; border-radius: 5px; margin-bottom: 20px; }");
        out.println(".constraint { font-family: monospace; background-color: #fff3e0; padding: 2px 5px; border-radius: 3px; }");
        out.println("h3 { color: #1976d2; border-bottom: 1px solid #bbdefb; padding-bottom: 5px; }");
        out.println("table { border-collapse: collapse; width: 100%; margin: 15px 0; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f5f5f5; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Jakarta Validation 3.1 Demo Results</h1>");
        
        String testName = request.getParameter("demo");
        if (testName == null || testName.isEmpty()) {
            out.println("<p>Please select a demo to run from the <a href=\"/\">home page</a>.</p>");
        } else {
            try {
                Method testMethod = this.getClass().getMethod(testName, TestResult.class);
                // Check if the method exists
                if (testMethod != null) {
                    out.println("<h2>Running Demo: " + formatTestName(testName) + "</h2>");
                    out.println("<div class=\"result\">");
                    
                    // Add test description based on the test name
                    out.println("<div class=\"demo-description\">");
                    out.println(getTestDescription(testName));
                    out.println("</div>");
                    
                    // Show relevant code snippet
                    out.println("<h3>Validation Constraints Being Demonstrated:</h3>");
                    out.println("<div class=\"code\">");
                    out.println("<pre>" + escapeHtml(getCodeSnippet(testName)) + "</pre>");
                    out.println("</div>");
                    
                    TestResult result = new TestResult();
                    testMethod.invoke(this, result);
                    
                    if (result.isSuccess()) {
                        out.println("<div class=\"success\">&#10004; Demo passed successfully!</div>");
                    } else {
                        out.println("<div class=\"failure\">&#10008; Demo failed: " + result.getMessage() + "</div>");
                    }
                    
                    out.println("<h3>Demo Results:</h3>");
                    
                    // Format the details in a more structured way
                    String details = result.getDetails();
                    if (details != null && !details.isEmpty()) {
                        // Convert the details to a table if it contains violations
                        if (details.contains("Violation:")) {
                            out.println("<table>");
                            out.println("<tr><th>Type</th><th>Path</th><th>Message</th></tr>");
                            
                            String[] lines = details.split("\n");
                            for (String line : lines) {
                                if (line.contains("Violation:")) {
                                    String type = line.substring(0, line.indexOf(":"));
                                    String path = line.substring(line.indexOf(":") + 2, line.indexOf(" - "));
                                    String message = line.substring(line.indexOf(" - ") + 3);
                                    
                                    out.println("<tr>");
                                    out.println("<td>" + type + "</td>");
                                    out.println("<td>" + path + "</td>");
                                    out.println("<td>" + message + "</td>");
                                    out.println("</tr>");
                                } else {
                                    out.println("<tr><td colspan=\"3\">" + line + "</td></tr>");
                                }
                            }
                            
                            out.println("</table>");
                        } else {
                            out.println("<pre>" + details + "</pre>");
                        }
                    }
                    
                    out.println("<h3>Jakarta Validation API Used:</h3>");
                    out.println("<div class=\"code\">");
                    out.println("<pre>" + escapeHtml(getValidationApiUsed(testName)) + "</pre>");
                    out.println("</div>");
                    
                    out.println("</div>");
                } else {
                    out.println("<p class=\"failure\">Invalid demo method: " + testName + "</p>");
                }
            } catch (Exception e) {
                out.println("<p class=\"failure\">Error running demo: " + e.getMessage() + "</p>");
                out.println("<pre>");
                e.printStackTrace(out);
                out.println("</pre>");
            }
        }
        
        out.println("<p><a href=\"/\">Back to Home</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * Helper class to capture demo results
     */
    private static class TestResult {
        private boolean success = true;
        private String message = "";
        private StringBuilder details = new StringBuilder();
        
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public void addDetail(String detail) {
            details.append(detail).append("\n");
        }
        
        public String getDetails() {
            return details.toString();
        }
    }
    
    /**
     * Basic demo for using Jakarta Validation on a Record.
     */
    public void basicRecordTest(TestResult result) {
        try {
            Person p = new Person("SampleName");
            Person np = new Person(null);
            
            Set<ConstraintViolation<Person>> validViolations = validator.validate(p);
            boolean validCheck = validViolations.size() == 0;
            
            Set<ConstraintViolation<Person>> invalidViolations = validator.validate(np);
            boolean invalidCheck = invalidViolations.size() == 1;
            
            result.addDetail("Person(\"SampleName\") violations: " + validViolations.size());
            result.addDetail("Person(null) violations: " + invalidViolations.size());
            
            if (!validCheck || !invalidCheck) {
                result.setSuccess(false);
                result.setMessage("Validation did not produce expected results");
            }
            
            if (invalidViolations.size() > 0) {
                for (ConstraintViolation<Person> violation : invalidViolations) {
                    result.addDetail("Violation: " + violation.getPropertyPath() + " - " + violation.getMessage());
                }
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("Exception occurred: " + e.getMessage());
            result.addDetail(e.toString());
        }
    }
    
    /**
     * Demo that validateProperty and validateValue work on a record
     */
    public void recordValidatePropertyAndValueTest(TestResult result) {
        try {
            Person propertydata = new Person(null);
            Set<ConstraintViolation<Person>> propertyViolations = validator.validateProperty(propertydata, "name");
            Set<ConstraintViolation<Person>> valueViolations = validator.validateValue(Person.class, "name", null);
            
            boolean propertyCheck = propertyViolations.size() == 1;
            boolean valueCheck = valueViolations.size() == 1;
            
            result.addDetail("validateProperty violations: " + propertyViolations.size());
            result.addDetail("validateValue violations: " + valueViolations.size());
            
            if (!propertyCheck || !valueCheck) {
                result.setSuccess(false);
                result.setMessage("Validation did not produce expected results");
            }
            
            if (propertyViolations.size() > 0) {
                for (ConstraintViolation<Person> violation : propertyViolations) {
                    result.addDetail("Property Violation: " + violation.getPropertyPath() + " - " + violation.getMessage());
                }
            }
            
            if (valueViolations.size() > 0) {
                for (ConstraintViolation<Person> violation : valueViolations) {
                    result.addDetail("Value Violation: " + violation.getPropertyPath() + " - " + violation.getMessage());
                }
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("Exception occurred: " + e.getMessage());
            result.addDetail(e.toString());
        }
    }
    
    /**
     * This demonstrates if able to validate parameters and return value with records.
     */
    public void validateRecordParametersTest(TestResult result) {
        try {
            Person object = new Person("x");
            Method method1 = Person.class.getMethod("checkNameSize", String.class);
            Object[] parameterValues = { "Maxallowedvaluesis10" };
            Set<ConstraintViolation<Person>> parameterViolations = validator.forExecutables()
                            .validateParameters(object, method1, parameterValues);
            
            Method method = Person.class.getMethod("getName");
            String returnValue = object.getName();
            Set<ConstraintViolation<Person>> returnValueViolations = validator.forExecutables()
                            .validateReturnValue(object, method, returnValue);
            
            boolean paramCheck = parameterViolations.size() == 1;
            boolean returnCheck = returnValueViolations.size() == 1;
            
            result.addDetail("Parameter violations: " + parameterViolations.size());
            result.addDetail("Return value violations: " + returnValueViolations.size());
            
            if (!paramCheck || !returnCheck) {
                result.setSuccess(false);
                result.setMessage("Validation did not produce expected results");
            }
            
            if (parameterViolations.size() > 0) {
                for (ConstraintViolation<Person> violation : parameterViolations) {
                    result.addDetail("Parameter Violation: " + violation.getPropertyPath() + " - " + violation.getMessage());
                }
            }
            
            if (returnValueViolations.size() > 0) {
                for (ConstraintViolation<Person> violation : returnValueViolations) {
                    result.addDetail("Return Value Violation: " + violation.getPropertyPath() + " - " + violation.getMessage());
                }
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("Exception occurred: " + e.getMessage());
            result.addDetail(e.toString());
        }
    }
    
    /**
     * Demo that cascade validation works with records.
     */
    public void nestedRecordsTest(TestResult result) {
        try {
            Employee emp = new Employee(null, new EmailAddress("emp1@example.com"));
            Set<ConstraintViolation<Employee>> violations = validator.validate(emp);
            
            boolean check = violations.size() == 1;
            
            result.addDetail("Employee(null, EmailAddress) violations: " + violations.size());
            
            if (!check) {
                result.setSuccess(false);
                result.setMessage("Validation did not produce expected results");
            }
            
            if (violations.size() > 0) {
                for (ConstraintViolation<Employee> violation : violations) {
                    result.addDetail("Violation: " + violation.getPropertyPath() + " - " + violation.getMessage());
                }
            }
            
            // Test with invalid email to check cascade validation
            Employee emp2 = new Employee("validId", new EmailAddress("invalid"));
            Set<ConstraintViolation<Employee>> violations2 = validator.validate(emp2);
            
            boolean check2 = violations2.size() == 1;
            
            result.addDetail("Employee(validId, invalid EmailAddress) violations: " + violations2.size());
            
            if (!check2) {
                result.setSuccess(false);
                result.setMessage("Cascade validation did not produce expected results");
            }
            
            if (violations2.size() > 0) {
                for (ConstraintViolation<Employee> violation : violations2) {
                    result.addDetail("Cascade Violation: " + violation.getPropertyPath() + " - " + violation.getMessage());
                }
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("Exception occurred: " + e.getMessage());
            result.addDetail(e.toString());
        }
    }
    
    /**
     * Demo that a group specified on a record can be validated separately, and that group conversion
     * works on a record.
     */
    public void convertGroupsRecordsTest(TestResult result) {
        try {
            Registration reg = new Registration("x1asas", false);
            Set<ConstraintViolation<Registration>> constraintViolations = validator.validate(reg);
            
            boolean check1 = constraintViolations.size() == 0;
            
            result.addDetail("Registration(valid, false) default group violations: " + constraintViolations.size());
            
            Company cmp2 = new Company(" sds", reg);
            Set<ConstraintViolation<Company>> constraintViolations2 = validator.validate(cmp2, RegistrationChecks.class);
            
            boolean check2 = constraintViolations2.size() == 2;
            
            result.addDetail("Company(valid, Registration) with RegistrationChecks group violations: " + constraintViolations2.size());
            
            if (!check1 || !check2) {
                result.setSuccess(false);
                result.setMessage("Group conversion did not produce expected results");
            }
            
            if (constraintViolations2.size() > 0) {
                for (ConstraintViolation<Company> violation : constraintViolations2) {
                    result.addDetail("Group Conversion Violation: " + violation.getPropertyPath() + " - " + violation.getMessage());
                }
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("Exception occurred: " + e.getMessage());
            result.addDetail(e.toString());
        }
    }
    
    /**
     * Demo the GroupSequence on a record.
     */
    public void GroupSequenceRecordsTest(TestResult result) {
        try {
            // Creating a SignupForm with valid data. Both firstName and age is valid here.
            SignupForm validSignupForm = new SignupForm("John Doe", 25);
            
            // Creating a SignupForm with invalid data. Both firstName and age is invalid here.
            SignupForm invalidSignupForm = new SignupForm("", 15);
            
            Set<ConstraintViolation<SignupForm>> constraintViolations = validator.validate(validSignupForm, ValidationOrder.class);
            boolean check1 = constraintViolations.size() == 0;
            
            result.addDetail("Valid SignupForm with ValidationOrder group violations: " + constraintViolations.size());
            
            constraintViolations = validator.validate(invalidSignupForm, ValidationOrder.class);
            boolean check2 = constraintViolations.size() == 1;
            
            result.addDetail("Invalid SignupForm with ValidationOrder group violations: " + constraintViolations.size());
            
            if (!check1 || !check2) {
                result.setSuccess(false);
                result.setMessage("Group sequence did not produce expected results");
            }
            
            if (constraintViolations.size() > 0) {
                for (ConstraintViolation<SignupForm> violation : constraintViolations) {
                    result.addDetail("Group Sequence Violation: " + violation.getPropertyPath() + " - " + violation.getMessage());
                }
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("Exception occurred: " + e.getMessage());
            result.addDetail(e.toString());
        }
    }
    
    /**
     * Format the demo name for display
     */
    private String formatTestName(String testName) {
        StringBuilder result = new StringBuilder();
        
        // Split by camel case
        for (int i = 0; i < testName.length(); i++) {
            char c = testName.charAt(i);
            if (i == 0) {
                result.append(Character.toUpperCase(c));
            } else if (Character.isUpperCase(c)) {
                result.append(' ').append(c);
            } else {
                result.append(c);
            }
        }
        
        return result.toString();
    }
    
    /**
     * Get a description for each demo
     */
    private String getTestDescription(String testName) {
        switch (testName) {
            case "basicRecordTest":
                return "<p>This demo demonstrates basic validation constraints on a Java record. " +
                       "It validates a <code>Person</code> record with a <code>@NotNull</code> constraint on the <code>name</code> field.</p>" +
                       "<p>The demo creates two instances:</p>" +
                       "<ul>" +
                       "<li>A valid instance with a non-null name</li>" +
                       "<li>An invalid instance with a null name</li>" +
                       "</ul>" +
                       "<p>The demo expects the valid instance to have no validation violations and the invalid instance to have exactly one violation.</p>";
                
            case "recordValidatePropertyAndValueTest":
                return "<p>This demo demonstrates how to use <code>validateProperty</code> and <code>validateValue</code> methods on a record.</p>" +
                       "<p><code>validateProperty</code> validates a specific property of an object, while <code>validateValue</code> validates " +
                       "a value against the constraints defined for a specific property of a class.</p>" +
                       "<p>The demo validates the <code>name</code> property of a <code>Person</code> record with a null value using both methods.</p>";
                
            case "validateRecordParametersTest":
                return "<p>This demo demonstrates validation of method parameters and return values on a record using the executable validator.</p>" +
                       "<p>It validates:</p>" +
                       "<ul>" +
                       "<li>A method parameter with a <code>@Size(max = 10)</code> constraint</li>" +
                       "<li>A method return value with a <code>@Size(min = 6)</code> constraint</li>" +
                       "</ul>" +
                       "<p>The demo expects both validations to fail with exactly one violation each.</p>";
                
            case "nestedRecordsTest":
                return "<p>This demo demonstrates cascaded validation with nested records using the <code>@Valid</code> annotation.</p>" +
                       "<p>It validates an <code>Employee</code> record that contains an <code>EmailAddress</code> record. " +
                       "The <code>@Valid</code> annotation on the <code>email</code> field causes validation to cascade to the <code>EmailAddress</code> record.</p>" +
                       "<p>The demo creates two instances:</p>" +
                       "<ul>" +
                       "<li>An instance with a null employee ID (violating <code>@NotNull</code>)</li>" +
                       "<li>An instance with an invalid email (violating <code>@Email</code>)</li>" +
                       "</ul>";
                
            case "convertGroupsRecordsTest":
                return "<p>This demo demonstrates group conversion with records using the <code>@ConvertGroup</code> annotation.</p>" +
                       "<p>It validates a <code>Company</code> record that contains a <code>Registration</code> record. " +
                       "The <code>@ConvertGroup</code> annotation on the <code>registration</code> field converts the default group to the <code>RegistrationChecks</code> group.</p>" +
                       "<p>The demo shows how constraints in different validation groups can be activated based on the context.</p>";
                
            case "GroupSequenceRecordsTest":
                return "<p>This demo demonstrates group sequence validation with records using the <code>@GroupSequence</code> annotation.</p>" +
                       "<p>It validates a <code>SignupForm</code> record using a <code>ValidationOrder</code> that defines a sequence of validation groups: <code>FirstGroup</code> followed by <code>SecondGroup</code>.</p>" +
                       "<p>The demo shows how validation stops at the first failing group in the sequence, preventing validation of constraints in later groups.</p>";
                
            default:
                return "<p>No detailed description available for this demo.</p>";
        }
    }
    
    /**
     * Get a code snippet for each demo
     */
    private String getCodeSnippet(String testName) {
        switch (testName) {
            case "basicRecordTest":
                return "// Person record with @NotNull constraint\n" +
                       "public record Person(@NotNull String name) {\n" +
                       "    // Record implementation\n" +
                       "}\n\n" +
                       "// Demo code\n" +
                       "Person valid = new Person(\"SampleName\");\n" +
                       "Person invalid = new Person(null);";
                
            case "recordValidatePropertyAndValueTest":
                return "// Validate a specific property of an object\n" +
                       "Person person = new Person(null);\n" +
                       "Set<ConstraintViolation<Person>> propertyViolations = \n" +
                       "    validator.validateProperty(person, \"name\");\n\n" +
                       "// Validate a value against constraints of a property\n" +
                       "Set<ConstraintViolation<Person>> valueViolations = \n" +
                       "    validator.validateValue(Person.class, \"name\", null);";
                
            case "validateRecordParametersTest":
                return "// Person record with method constraints\n" +
                       "public record Person(@NotNull String name) {\n" +
                       "    public String checkNameSize(@Size(max = 10) String x) {\n" +
                       "        return x + \"String value\";\n" +
                       "    }\n\n" +
                       "    @Size(min = 6)\n" +
                       "    public String getName() {\n" +
                       "        return this.name;\n" +
                       "    }\n" +
                       "}\n\n" +
                       "// Validate method parameters\n" +
                       "Method method = Person.class.getMethod(\"checkNameSize\", String.class);\n" +
                       "Object[] params = { \"Maxallowedvaluesis10\" };\n" +
                       "validator.forExecutables().validateParameters(person, method, params);\n\n" +
                       "// Validate method return value\n" +
                       "Method getter = Person.class.getMethod(\"getName\");\n" +
                       "String returnValue = person.getName();\n" +
                       "validator.forExecutables().validateReturnValue(person, getter, returnValue);";
                
            case "nestedRecordsTest":
                return "// Email record with validation constraints\n" +
                       "public record EmailAddress(@Email @Size(min = 3, max = 100) String value) {}\n\n" +
                       "// Employee record with cascaded validation\n" +
                       "public record Employee(@NotNull String empid, @Valid EmailAddress email) {}\n\n" +
                       "// Demo code\n" +
                       "Employee emp1 = new Employee(null, new EmailAddress(\"valid@example.com\"));\n" +
                       "Employee emp2 = new Employee(\"validId\", new EmailAddress(\"invalid\"));";
                
            case "convertGroupsRecordsTest":
                return "// Group interface\n" +
                       "public interface RegistrationChecks {}\n\n" +
                       "// Registration record with group-specific constraint\n" +
                       "public record Registration(\n" +
                       "    @NotNull String companyid,\n" +
                       "    @AssertTrue(message = \"Company should be registered\",\n" +
                       "               groups = RegistrationChecks.class) boolean isRegistered) {}\n\n" +
                       "// Company record with group conversion\n" +
                       "public record Company(\n" +
                       "    @NotNull String companyName,\n" +
                       "    @Valid @ConvertGroup(from = Default.class, to = RegistrationChecks.class)\n" +
                       "    Registration registration) {}\n\n" +
                       "// Demo code\n" +
                       "Registration reg = new Registration(\"x1asas\", false);\n" +
                       "Company company = new Company(\"CompanyName\", reg);\n" +
                       "validator.validate(company, RegistrationChecks.class);";
                
            case "GroupSequenceRecordsTest":
                return "// Group interfaces\n" +
                       "public interface FirstGroup {}\n" +
                       "public interface SecondGroup {}\n\n" +
                       "// Group sequence definition\n" +
                       "@GroupSequence({ FirstGroup.class, SecondGroup.class })\n" +
                       "public interface ValidationOrder {}\n\n" +
                       "// SignupForm record with grouped constraints\n" +
                       "public record SignupForm(\n" +
                       "    @NotBlank(message = \"Name cannot be blank\", \n" +
                       "             groups = FirstGroup.class) String firstName,\n" +
                       "    @Min(value = 18, message = \"Age must be at least 18\", \n" +
                       "         groups = SecondGroup.class) int age) {}\n\n" +
                       "// Demo code\n" +
                       "SignupForm valid = new SignupForm(\"John Doe\", 25);\n" +
                       "SignupForm invalid = new SignupForm(\"\", 15);\n" +
                       "validator.validate(invalid, ValidationOrder.class);";
                
            default:
                return "// No code snippet available for this test";
        }
    }
    
    /**
     * Get the validation API used for each demo
     */
    private String getValidationApiUsed(String testName) {
        switch (testName) {
            case "basicRecordTest":
                return "// Basic validation API\n" +
                       "Set<ConstraintViolation<Person>> violations = validator.validate(person);";
                
            case "recordValidatePropertyAndValueTest":
                return "// Property validation API\n" +
                       "Set<ConstraintViolation<Person>> propertyViolations = validator.validateProperty(person, \"name\");\n\n" +
                       "// Value validation API\n" +
                       "Set<ConstraintViolation<Person>> valueViolations = validator.validateValue(Person.class, \"name\", null);";
                
            case "validateRecordParametersTest":
                return "// Method parameter validation API\n" +
                       "Set<ConstraintViolation<Person>> parameterViolations = validator.forExecutables()\n" +
                       "    .validateParameters(object, method, parameterValues);\n\n" +
                       "// Method return value validation API\n" +
                       "Set<ConstraintViolation<Person>> returnValueViolations = validator.forExecutables()\n" +
                       "    .validateReturnValue(object, method, returnValue);";
                
            case "nestedRecordsTest":
                return "// Cascaded validation API (using @Valid annotation)\n" +
                       "Set<ConstraintViolation<Employee>> violations = validator.validate(employee);";
                
            case "convertGroupsRecordsTest":
                return "// Default group validation API\n" +
                       "Set<ConstraintViolation<Registration>> violations1 = validator.validate(registration);\n\n" +
                       "// Group-specific validation API with group conversion\n" +
                       "Set<ConstraintViolation<Company>> violations2 = validator.validate(company, RegistrationChecks.class);";
                
            case "GroupSequenceRecordsTest":
                return "// Group sequence validation API\n" +
                       "Set<ConstraintViolation<SignupForm>> violations = validator.validate(signupForm, ValidationOrder.class);";
                
            default:
                return "// No API usage example available for this test";
        }
    }
    
    /**
     * Escape HTML special characters in code snippets
     */
    private String escapeHtml(String html) {
        if (html == null) {
            return "";
        }
        // Debug the escaping issue
        System.out.println("Escaping HTML: " + html);
        return html.replace("<", "&lt;").replace(">", "&gt;");
    }
}
