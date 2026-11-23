package com.ai.pluginAi.AiTools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class CalculatorTool {

    @Tool(description = """
            Perform arithmetic, algebraic, trigonometric, and mathematical calculations 
            based on the provided operation and input values. This tool acts as a robust 
            computational assistant for evaluating complex mathematical expressions.
            """)
    public double calculate(
            @ToolParam(description = """
                    The specific mathematical operation to execute. 
                    Supported operations include basic arithmetic ('+', '-', '*', '/'), 
                    exponentiation ('^'), roots ('sqrt', 'cbrt'), absolute values ('abs'), 
                    rounding functions ('ceil', 'floor'), trigonometric functions ('sin', 'cos', 'tan'), 
                    and logarithmic functions ('log', 'log10').
                    """)
            String operation,
            @ToolParam(description = """
                    The primary numeric operand (a) required for the calculation. 
                    This serves as the main input value for both unary and binary mathematical operations.
                    """)
            double a,
            @ToolParam(description = """
                    The secondary numeric operand (b) required for binary operations 
                    such as addition, subtraction, multiplication, division, and exponentiation. 
                    This parameter is ignored for unary operations like square root or sine.
                    """)
            double b) {

        switch (operation.toLowerCase()) {
            case "+":
            case "addition":
                return a + b;
            case "-":
            case "subtraction":
                return a - b;
            case "*":
            case "multiply":
                return a * b;
            case "/":
            case "division":
                if (b == 0) {
                    throw new IllegalArgumentException("Division by zero is not allowed.");
                }
                return a / b;
            case "^":
            case "power":
                return Math.pow(a, b);
            case "sin":
                return Math.sin(a);
            case "cos":
                return Math.cos(a);
            case "tan":
                return Math.tan(a);
            case "sqrt":
                return Math.sqrt(a);
            case "cbrt":
                return Math.cbrt(a);
            case "abs":
                return Math.abs(a);
            case "ceil":
            case "ceiling":
                return Math.ceil(a);
            case "floor":
                return Math.floor(a);
            case "log":
                return Math.log(a);
            case "log10":
                return Math.log10(a);
            default:
                throw new IllegalArgumentException("Unsupported mathematical operation: " + operation);
        }
    }
}