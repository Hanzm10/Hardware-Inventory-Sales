# CODING STANDARDS

## Introduction

This document describes the coding standards for the project.
The purpose of this document is to ensure that the code is written in a
consistent manner
and is easy to read and maintain.

## Rules

1. **Rule**: Restrict all code to very simple control flow constructs -- do not
   use direct or indirect recursion, circular references.
   <br>
   <br>
    - **Rationale**: Simpler control flow translates into stronger capabilities
      for stronger capabilities for verification
      and often results in improved code quality. Without recursion, it is
      guaranteed
      to have an acyclic function call graph, which, if used, can be exploited
      by code analyzers, and can directly
      help to prove that all executions that should be bounded are in fact
      bounded. The same can be said for
      circular references, which are difficult to manage and can lead to bugs
      and other issues when complexity scales.
      <br>
      <br>
    - **Benefits**:
        - Easier to understand
        - Easier to test
        - Predictability of code execution
        - Easier to maintain
          <br>
          <br>
          <br>
2. **Rule**: All loops must have a fixed upper-bound. It must be trivially
   possible for a checking tool to *prove* statically
   that a preset upper-bound on the number of iterations of a loop cannot be
   exceeded. If the loop-bound cannot be
   proven statically, the rule is violated.
    - **Rationale**: The absence of recursions and the presence of loop bounds
      prevents runaway code.
      This rule does not, of course, apply to iterations that are *meant* to be
      non-terminating (e.g., process scheduler).
      In those cases, the reverse rule is applied: it should be statically
      provable that the iteration cannot terminate.
      One way to support the rule is to add an explicit upper-bound to all loops
      that have a variable number of iterations.
      When the upper bound is exceeded, an assertion failure is triggers, and
      the function containing the failing
      iteration returns an error.
      <br>
      <br>
      <br>
3. **Rules**: All functions and loops must have a single entry and a single exit
   point, unless a multiple exit points
   or early return statements will make the code simpler and easier to
   understand.
   <br>
   <br>
    - **Rationale**: Single entry and exit points make the code easier to
      understand and maintain.
      <br>
      <br>
    - **Benefits**:
        - Easier to understand
        - Easier to test
        - Easier to maintain
          <br>
          <br>
          <br>
4. **Rules**: No function should be longer than what can be printed on a single
   sheet of paper in
   a standard reference format with one line per statement and one line per
   declaration. Typically, this means
   no more than about **60 lines of code per function**.
   <br>
   <br>
    - **Rationale**: Each function should be a logical unit in the code that is
      understandable and verifiable as a unit.
      It is much harder to understand a logical unit that spans multiple screens
      on a computer display or multiple pages when printed.
      Excessively long functions are often a sign of poorly structured code.
      <br>
      <br>
      <br>
5. **Rules**: The assertion density of the code should average to a minimum of
   two assertions per function.
   <br>
   <br>
   <br>
6. **Rules**: Data objects must be declared at the smallest possible level of
   scope.
   <br>
   <br>
    - **Rationale**: This rule supports a basic principle of data-hiding.
      Clearly if an object is not in scope, its value
      cannot be referenced or corrupted. Similarly; if an erroneous value of an
      object has to be diagnosed, the fewer the number of statements
      where the value could have been assigned; the easier it is to diagnose the
      problem. The rule discourages the re-use of
      variables for multiple, incompatible purposes, which can complicate fault
      diagnosis.
7. **Rules**: The return value of non-void functions must be checked by each
   calling function, and the validity of parameters must be checked inside each
   function.
   <br>
   <br>
    - **Rationale**: This rule is a simple defensive programming measure. It is
      much easier to diagnose a defect if it is detected close to its origin.