### Basics###

.append -> .push
.split, .join, .reverse
new Set(), in -> .has(elem)
let = variable available only in current block
n1, n2 = n2, n1 -> `[n1, n2] = [n2, n1]`
Number.isInteger
arr. filter, map, reduce, splice, slice, concat, forEach, indexOf (uses `===`, doesn't work with NaN), includes (works with NaN), find (returns undefined), findIndex, findLastIndex, sort, reverse, split, join, Array.isArray

### Web usage basics ###

**Selecting an element:**
`const container = document.querySelector("#container")`
`const display = container.firstElementChild;`
Can also select class + based on relationships `.previousElementSibling`
`.querySelectorAll(selectors)` -> returns a "NodeList", not an array. Can convert with `Array.from()`

**Creating an element:**
`document.createElement(tagName, [options])`

**Append an element:**
`parentNode.appendChild(childNode), parentNode.insertBefore(newNode, referenceNode)`

**Remove an element:**
`parentNode.removeChild(child)` -> removes child + returns reference to it.

**Altering style:**
`element.style.backgroundColor` or `element.style["backgroundColor"]` , don't use -

**Editing attributes:**
`setAttribute("attr_name", "attr_value")`, `getAttribute, removeAttribute`

**Working with classes:**
`element.classList.(add|remove|toggle)`

**Adding text:**
`element.textContent=...`

**Adding HTML content:**
`element.innerHTML = "some HTML code"`

### Events ###
A DOM element can have only one onclick.
`addEventListener(action, function)`

Callback = function that is passed as an argument to another function.
Callback parameter e = references the event, that contains some info about the trigger. e.g: `e.target.style.background = "blue"`

### Objects ###
```
function Book(author, title) {
	this.author = author;
	this.title = title;
	this.info = function() {
		console.log(`${this.author} ${this.title}`);
	}
}
book = new Book("Author", "Title");
```
All objects have a prototype object.
`Object.getPrototypeOf(book) === Book.prototype;` = returns true;
By attaching an attribute or function to a prototype, you attach it to all instances of the corresponding object.
Also, every object inherits from Object, since: `Object.getPrototypeOf(Player.prototype) === Object.prototype` is true
Aka, access to general object functions like: `.hasOwnProperty(propertyName) or .valueOf()`
Inheriting: `Object.setPrototypeOf(Player.prototype, Person.prototype)` = should be done before object instantiation.


