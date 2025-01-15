Ancestor combinator vs chaining vs child combinator (>)

Cascade = what rules take priority if they all affect the same element.
ID (#) > class (.) > type

Box model:
- padding = space between content and border
- border = space between padding and margin
- margin = space between border of the box and the border of adjacent boxes.

Margin collapsing - only vertically ++ (largest prevails), -- (smallest), +- (arithmetic)

Setting box-sizing for all elements (see MDN)

top right bottom left; top right (top right)
Center horizontally: `margin: 0 auto` 

Blocks fill the available inline space of the parent element by default.

### Flexbox ###

Flex container = any element with `display: flex`
Flex item = element inside a flex container
Some properties go on flex container, others on the flex items.

For row direction:
`justify-content:center` equivalent to `margin: 0 auto`
`align-items: center` vertical alignment
Column direction is the reverse of this.

`flex-wrap: wrap`
`flex-direction: colummn|row`
Flex items + auto-margins.
