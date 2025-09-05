# Past Commit Messages

Some of my earlier commits have inconsistent messages, so I'm using this section to explain in a more conventional manner. 

Starting from July 4th, 2025, I will be using the more conventional format as follows.

## Commit 2867a97 (May 1st, 2025)  
init: Initial Commit with basic MVC structure (Controller, Service, Entity, Repository)

## Commit dc79934 (May 2nd, 2025)
feat: Added EnvLoader to enable using .env files

## Commit 07cede1 (May 7th, 2025)
feat: Added User Entity (including Controller, Service, Entity, Repository)

feat: Added the User Login system. 

## Commit 2aff910 (May 7th, 2025)

fix: Implementing function fixes within services (eg: changing isPresent to isEmpty)

## Commit 269ae15 (May 7th, 2025)

chore: Removing unnecessary imports

## Commit 983b196 (May 7th, 2025)

feat: Added the missing LoginController

## Commit 9d9a992 (May 8th, 2025)

feat: Added Spring-boot-actuator for testing with VS Code

## Commit ce90223 (May 13th, 2025)

feat: Added CORS to integrate with the (WIP) Front End

feat: Added counting functions (To count how many entities have been stored in the database)

## Commit bea5990 (June 4th, 2025)

feat: Added Gender enum for Hero entity.

feat: Implements the ImgIcon and how to display it via addResourceHandlers. WebMvcConfig is added.

## Commit 930aaec (June 4th, 2025)

chore: Combines both CORS and Resource Handlers into one WebMvcConfig file. 

## Commit 9cf5568 (June 5th, 2025)

chore: Uses SonarQube to clean up programming structure.

## Commit 4368703 (June 21st, 2025)

fix: Fixing the destination for Resource Handler

fix: Fixing the definition of Hero Description and Hero Lore (no longer uses Lob)

fix: Updates the Hero Service to ensure being able to proceed when certain parameters are not filled.

## Commit e11843d (June 24th, 2025)

feat: Added DTOs 

feat: Added Admin & Batch Initializer features (Run the services when starting with an empty DB)

fix: Added ImgIcon for Company

fix: Make image files not to be required when adding/updating Hero/Game/Company

fix: Added additional allowedMethods for CORS

## Commit 684acb1 (June 26th, 2025)

feat: Added User Search service

feat: Added Readme.MD --> Project goes Public.

## Commit 6df5311 (July 2nd, 2025)

test: Unit Testing implemented - Added Hero Service Test

## Commit 0b13851 (July 2nd, 2025)

test: Added Game Service Test

## Commit 6134370 (July 2nd, 2025)

test: Added Company Service Test