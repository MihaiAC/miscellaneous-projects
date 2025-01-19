### Basics###

.append -> .push
.split, .join, .reverse
new Set(), in -> .has(elem)
let = variable available only in current block
n1, n2 = n2, n1 -> `[n1, n2] = [n2, n1]`
Number.isInteger

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

