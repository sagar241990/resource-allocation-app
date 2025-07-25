📘 Project Name: ProjectAllocationApp
📝 Description:
ProjectAllocationApp is a backend service designed to manage and allocate resources across various projects. The application will handle the ingestion and persistence of project allocation data through file uploads. In the future, a frontend application built with Angular will integrate with this Spring Boot backend to offer a full-featured user interface.

🧰 Tech Stack:
Backend Framework: Spring Boot

Web Layer: Spring Web

Persistence Layer: Spring Data JPA

Database: H2 (in-memory, for development and testing)

🚀 Key Feature (Phase 1):
📂 Upload Project Allocation Details
Supports CSV or Excel file formats

Expected Columns:

workRequestNumber

wrName

soeId

projectName

projectType

lob

Uploaded data will be parsed and stored in the H2 database

✅ Task Breakdown
🏗️ 1. Project Setup
 check Spring Boot project for dependencies: Web, JPA, H2

 Set up base package structure (controller, service, repository, model, dto, util)

🧱 2. Model and Database Layer
 Create ProjectAllocation JPA Entity

 Create ProjectAllocationRepository interface

 Configure H2 database properties

📥 3. CSV/Excel File Upload Feature
 Design upload endpoint: /api/upload

 Implement CSV parser (e.g., using BufferedReader)

 Integrate Apache POI for Excel (.xlsx) parsing

 Create a generic FileUploadService

 Validate uploaded data (e.g., null checks, formats)
