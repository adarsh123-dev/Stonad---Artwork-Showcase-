# STONAD frontend (integrated with your Spring Boot backend)

## Install
1. Extract the supplied `Stonad_backend_with_frontend.zip`.
2. Open the `Stonad_backend` folder in Spring Tool Suite as an existing Maven project.
3. The frontend files are already placed in `src/main/resources/static/`.
4. Run `StonadBackendApplication`.
5. Open http://localhost:2005/ in your browser.

## API routes used
- GET /api/artworks
- GET /api/artworks/{id}
- GET /api/artworks/search?title=
- GET /api/artworks/category/{categoryId}
- POST /api/artworks/upload (multipart fields: title, description, artistId, categoryId, image)
- GET /api/artists
- GET /api/categories
- POST /api/visitors/register
- POST /api/visitors/login
- GET/PUT/DELETE /api/visitors/{id}

## Important notes about the current backend
- The Visitor entity marks `name` as @NotBlank. The login controller also uses @Valid on Visitor, so the frontend sends the placeholder name "Visitor" during login to satisfy current validation. A future improvement is a dedicated LoginRequest DTO.
- The update endpoint validates the full Visitor entity, including its required password, although VisitorService updates only name and email. The frontend currently sends a placeholder password to pass that validation. A future improvement is a dedicated UpdateVisitorRequest DTO or validation groups. Do not use this placeholder approach for a production system.
- Visitor passwords are currently stored/compared as plain text in the supplied backend. This is unsafe for real deployment; use secure password hashing before production.
- The current backend does not implement authorization. Restrict visitor profile operations and artwork uploads before public deployment.
- Uploaded images are served from /uploads/** using the existing WebConfig. The uploads directory must remain available to the running backend.
- Frontend and backend share the same origin when served from Spring Boot, so no CORS configuration is required for this setup.

## This is a vanilla HTML/CSS/JavaScript frontend. No Node.js installation is required.
