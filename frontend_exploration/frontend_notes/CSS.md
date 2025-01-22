
Preprocessors: SASS, LESS, Stylus

#### Selectors ####
Ancestor combinator vs chaining vs child combinator (>)

`>` = child; `+` = adjacent sibling; `~` = all siblings;
`:focus, :hover, :active, :link, :visited`
`:first-child, :last-child, :only-child, :nth-child, :first-of-type, :only-of-type, :empty, :not(X), :root` 
`::marker, ::first-letter, ::first-line, ::selection, ::before, ::after`

`img[src="puppy.jpg]` = attribute selector
Can use :root to declare global custom properties. (--x -> var(--x))


Cascade = what rules take priority if they all affect the same element.
ID (#) > class (.) > type


----------------------------------------------------

Box model:
- padding = space between content and border
- border = space between padding and margin
- margin = space between border of the box and the border of adjacent boxes.

Margin collapsing - only vertically ++ (largest prevails), -- (smallest), +- (arithmetic)

Setting box-sizing for all elements (see MDN)

top right bottom left; top right (top right)
Center horizontally: `margin: 0 auto` 

Blocks fill the available inline space of the parent element by default.

`height: 100vh` -> element will be as tall as the entire height of the browser viewport = useful for full-screen sections

#### Li ####
`list-style-type: none;` = remove bullet points;

#### a ####
`text-decoration: none;` = remove underline;
```
a,
a:link,
a:hover,
a:visited,
a:active {
text-decoration: none;
color: white;
}
```


### Flexbox ###

Flex container = any element with `display: flex`
Flex item = element inside a flex container
Some properties go on flex container, others on the flex items.

Main axis (row-horizontal): `justify-content:center` equivalent to `margin: 0 auto`
Secondary axis (row-vertical):`align-items: center` vertical alignment
Column flex-direction rotates the axes by 90 deg clockwise.
`flex-wrap: wrap`
`flex-direction: colummn|row`
Flex items + auto-margins.

Flex-grow, flex-shrink and flex-basis
flex: auto = flex 1 1 auto;
flex-shrink: 0 == prevents the element from being squashed == cannot be shrinked at all (IIUC)

flex:direction column with flex:1 => boxes collapse onto themselves as a div has default height 0; fix: flex: 1 1 auto;

Align: each item can move along the secondary axis without interfering with other items. Justify cannot do that. => different values available for the two properties. Shish kebab vs cocktail wieners analogy.

Elements have minimum width. For text, it's the length of the longest word.

min-width: 0px can be useful sometimes.

Gap

Space-between: elem1 - space - elem2 - space - elem3
Space-around: space/2 - elem1 - space - elem2 - space - elem3 - space/2
Space-evenly: space - elem1 - space - elem2 - space - elem3 - space

`ul {margin: 0; padding: 0;}` (why justify-content was not working)

flex: 1 take space

### Text###
`system font stack`
Have fallback if font is not available. Consider all major systems: macOS, Windows, Ubuntu, + phone OS's.
Hosting own font may be slower than importing it from Google. Importing it comes with privacy issues (potential GDPR violation?).

```
width: ___px;
white-space: nowrap;
overflow: hidden; 
text-overflow: ellipsis;
```

`overflow:scroll|auto` 