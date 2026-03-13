### Terms
- Solution = `.sln`, references to projects, build order, configs;
- Project = `.csproj` = buildable unit inside a solution; code, resources, dependencies, build instructions;
- Controller = class handling HTTP requests;
- Action = method inside a controller, handling a route (e.g: `[HttpGet]`)
- Domain = pure business logic + Entities, Services, Repositories, etc.
- DbContext = part of Entity Framework Core (ORM); CRUD ops on entities;

### Creating a project from cmd
`dotnet new list` = all available things we can create;
`dotnet new sln` = create Solution;
`dotnet new webapi -n API -controllers` = create a new api named "API" using the classic MVC;
`dotnet new classlib -n Domain` = creates a new class library named "Domain"; 
`dotnet sln add Domain` = adds the classlib to the Solution;
++ adding references to the classlibs from the API and other classlibs (manual);

`Microsoft.EntityFrameworkCore.DbContext` = session with the DB, used to query + save instances of entities; Is a combination of `Unit of Work` and `Repository` patterns; 

Installing Entity Framework Core: https://www.nuget.org/packages/dotnet-ef
Initial migration: `dotnet ef migrations add InitialCreate -p .\Persistence\ -s API`
Creating the DB:  `dotnet ef database update -p Persistence -s API`

### Packages used so far
- MediatR for mediators between API Controller and persistence layer;
- AutoMapper;

