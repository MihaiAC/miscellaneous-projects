CQRS = Command Query Responsibility Segregation.

In a traditional CRUD architecture, you have the same data model doing both read and write operations. CQRS separates the two.

Why?
- Data mismatch: read, write representations can differ;
- Lock contention(?): read and write ops can compete for the same locks;
- Enables horizontal scaling for reads;
- Security implications: more fine-grained auth rules (old: you need permission to update a record, new: you need permission to call e.g: "ApproveInvoice"  aka align with business use cases? in an easier way?). I don't see how you couldn't just do this in the old model though. CQRS seems to imply that mutating operations are way more specific.


Request -> API Controller (query | command) -> Mediator (.send() -> handler -> return object) -> API Controller

**Commands:** do something, modify state, do not return values.
**Query:** answers a question, does not modify state, returns values.

CQRS seems to be useful only when you have multiple interconnected DBs + some scale.
Or different DBs that you can optimise for write / read.

In C#, you have the `MediatR` package which does this for you.
(NB: This already seems like a bit of a red flag. Like ok, let's introduce a mediator in the name of clean architecture for our small shitty app and also a dependency on an external package right away!)


