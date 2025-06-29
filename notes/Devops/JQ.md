JQ = command line JSON processor
Manual: https://jqlang.org/manual/
Following this guide: https://navendu.me/posts/jq-interactive-guide/

Identity filter: `.` (dot) = returns JSON unchanged
Accessing fields: `.name`
Nested fields: `.address.zipcode`, `.address.geo.lat`
Array index filter: `.[0]`
Array slice: `.[3:6]`
Iterating through all elements: `.[]` 
Getting the names of all the users: `.[].name`
The names of all users without the quotes: `-r .[].name`
Can create new objects: `'{"name": .name, "email": .email, "company": .company.name}'`
Can create a new array of objects: `'[.[] | {"name": .name, "emailAddress": .email, "company": .company.name}]'`
JQ does have an internal pipe operator, that works similarly to the normal Linux pipe.
Displaying the length of a value:`| length`
Displaying the keys of an object, in an array: `. | keys`
Applying a function to an input array of objects: `'map({name: .name, city: .address.city})'`
Chaining functions:
```
'.[:3] |
map({name: .name, city: .address.city, slug: ((.name + "-" + .address.city |
gsub(" "; "-") |
ascii_downcase))})'
```
https://jqlang.org/manual/#builtin-operators-and-functions
Select function: `'.[] | select(.address.city == "South Christy") | {name, username, email}'`