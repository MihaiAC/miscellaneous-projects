https://auth0.com/blog/permissions-privileges-and-scopes/
Permission = attached to the resource-to-be-accessed, details what you can do to/with it;
Privilege = permission that is assigned to a user/app; (e.g: user X has the privilege to read this doc);
Scope = what an application can do on behalf of the user, cannot exceed the user's privileges at evaluation time;

Common gotchas:
- Scopes are not "an application's privileges"; application are merely authorized to exercise those privileges on behalf of the user;
- Not every permission has a scope. e.g: may not want to let an app delete a resource on behalf of a user;
- Can't neatly map scopes to privileges either; in OIDC, the openid scope makes the request into an auth flow + tells the server to give a proof of who made the request(?!).