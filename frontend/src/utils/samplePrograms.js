export const SAMPLE_PROGRAMS = [
  {
    id: 'arithmetic-expressions',
    name: 'Arithmetic & TAC Generation',
    description: 'Demonstrates operator precedence, temporary variable generation, and assignment statements.',
    code: `// Arithmetic Expressions & Precedence
int main() {
    int a = 10;
    int b = 20;
    int c = 30;
    int d = 5;
    
    // Complex expression: demonstrates Pratt parsing & TAC
    int result = a + b * c - (d * 2) / (a + 5);
    
    print(result);
    return result;
}
`
  },
  {
    id: 'control-flow-loops',
    name: 'While & For Loops with Branching',
    description: 'Demonstrates condition evaluation, loop labels, conditional jumps, and block scopes.',
    code: `// Loops & Conditional Execution
int factorial(int n) {
    int result = 1;
    int i = 1;
    
    while (i <= n) {
        result = result * i;
        i = i + 1;
    }
    
    return result;
}

int main() {
    int num = 5;
    int fact = factorial(num);
    
    if (fact > 100) {
        print(fact);
    } else {
        print(0);
    }
    
    return 0;
}
`
  },
  {
    id: 'nested-scopes',
    name: 'Hierarchical Scopes & Symbol Table',
    description: 'Demonstrates global vs local variables, shadowing, and scope level tracking in the Symbol Table.',
    code: `// Scoping & Symbol Table Demonstration
int globalCounter = 100;

int processValue(int x) {
    int localVal = x * 2;
    
    // Nested block scope
    {
        int inner = localVal + 5;
        globalCounter = globalCounter + inner;
    }
    
    return globalCounter;
}

int main() {
    int input = 25;
    int finalResult = processValue(input);
    print(finalResult);
    return 0;
}
`
  },
  {
    id: 'semantic-error-demo',
    name: 'Semantic Error Detection Demo',
    description: 'Contains type mismatch, undeclared variable usage, and duplicate declarations to test compiler diagnostics.',
    code: `// Semantic Error Test Suite
int main() {
    int validVar = 10;
    
    // Error 1: Undeclared variable
    undeclaredVar = 50;
    
    // Error 2: Duplicate declaration
    int validVar = 20;
    
    // Error 3: Type mismatch (string assigned to int)
    int count = "hello world";
    
    return 0;
}
`
  },
  {
    id: 'relational-logic',
    name: 'Boolean Logic & Relational Ops',
    description: 'Tests comparison operators, logical AND/OR, and truth values.',
    code: `// Boolean Logic & Short-Circuit
int main() {
    int score = 85;
    int attendance = 90;
    bool isEligible = false;
    
    if (score >= 80 && attendance >= 75) {
        isEligible = true;
        print(1);
    } else {
        isEligible = false;
        print(0);
    }
    
    return 0;
}
`
  }
];
