# AI_NOTES.md

## AI Usage Summary

AI was used as a development assistant throughout the project rather than as a code generator. It was primarily used to review implementation decisions, discuss architectural trade-offs, identify potential improvements, and improve documentation. Final implementation decisions and all submitted code were manually reviewed and validated before being included in the project.

---

## 1. AI-Assisted Areas

AI was used to assist with:

- Reviewing the overall project architecture and discussing design alternatives.
- Reviewing architectural decisions and discussing implementation trade-offs.
- Reviewing manually written code for correctness, readability, and maintainability.
- Suggesting improvements to unit and integration tests.
- Identifying edge cases and potential bugs during development.
- Improving project documentation, including the README and this document.
  
While AI generated example implementations for some components, they were used as references rather than copied directly into the project.

---

## 2. Validation and Changes

AI suggestions were not accepted without review. Whenever a suggestion was adopted, it was first validated, tested, and often modified to align with the project's architecture and coding style.

Examples include:

- Refining AI-generated code to follow the project's naming conventions and package structure.
- Modifying generated unit and integration tests to match the application's actual API behavior, validation rules, and error responses.
- Reviewing repository, service, and controller implementations to ensure they aligned with the intended architecture and design decisions.
- Manually testing all REST endpoints using Postman before considering the implementation complete.
- Running the complete automated test suite after significant changes to ensure existing functionality remained correct.

---

## 3. AI Suggestions Not Adopted

Some AI suggestions were intentionally not used because they did not align with the project's design goals or assessment requirements.

Examples include:

- Using a basic MVC structure without DTOs. The project instead adopts a layered architecture with DTOs, mappers, and repository abstractions to better separate concerns.
- Combining multiple responsibilities into larger classes. The implementation instead separates controllers, services, repositories, DTOs, mappers, and exception handling into dedicated packages and classes.
- Replacing the in-memory repository with a database implementation. The assessment explicitly required an in-memory solution.
- Introducing additional abstractions or design patterns that increased complexity without providing meaningful value for the scope of the assessment.
  
---

## Final Note

AI was used as a technical reviewer and development assistant throughout the project. It helped evaluate design decisions, review implementation choices, and improve testing and documentation. The final architecture, implementation, debugging, and validation of the submitted solution were completed through manual development and iterative review.
