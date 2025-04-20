https://overreacted.io/jsx-over-the-wire/ -> feeding JSON into Components
- Instead of typical REST resources, implement an endpoint for every (major?) screen - interesting idea. (e.g: get all details for a single post) => screens do not compete with each other for endpoint design.
- BFF (Backend for Frontend) - ~~frontend(??) layer that gathers data and puts it in the format it needs to be in for a certain screen - which would run on the server (??)~~. - RSC basically Note: JS backend is the implicit assumption here.
- SDUI = server-driven UI = JSON endpoints that return UI trees
- Reminds of GraphQL DataLoaders, but for components?