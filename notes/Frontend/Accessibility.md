Source: https://www.freecodecamp.org/news/the-web-accessibility-handbook/

Non-semantic HTML tags (div, span) vs semantic HTML tags (button, a, header, footer, hx, p, ul-li)

Pros of using semantic tags:
- Built-in styles and functionality.
- Code is easier to read and maintain (vs. using divs everywhere)
- Screen readers can easily read and interpret them.

Emphasizing text: strong and em tags.

### Layouts
Use header, main, footer, nav elements.
I do remember the days in which tables were used for layout.

### Keyboard accessibility
Each interactive element should be navigable through the keyboard. 
Testing it: visit a page and press Tab to cycle through elements.
div and span are not "tab-able" by default

Can make them by adding `tabindex`
Example:
`<div id="customElement" role="someRole" tabindex="0"`

Special `tabindex` values:
- 0 -> div will be accessed by tab in the natural order;
- -1 -> element is not reachable via keyboard navigation;

Since we want the div to be interactable, can add an event listener for `keyDown` and check if `Enter` was pressed (to click on it).

Don't omit form labels.

Links -> use `target="_blank"` top open it in a new tab.

Skip links are fine for screen readers as long as they are explicit.

JS: if you add `mouseover` and `mouseout` event listeners, must add equivalent event listeners for `focus`, `blur`.

### WAI-ARIA
**role** attribute = adds semantic info to non-semantic elements

ARIA = extra attributes to give more info to screen readers;
[MDN ARIA states](https://developer.mozilla.org/en-US/docs/Web/Accessibility/ARIA/Reference/Attributes)

Dynamic content updates with `aria-live` attribute:
- `off` -> no content is read out
- `polite` -> updates announced when user is idle
- `assertive` -> content read out as soon as it is updated

`aria-atomic` = read out the entire element

Form errors: `<div class="errors" role="alert" aria-relevant="all">...</div>`
`alert` role = makes element live

Required fields: `aria-required=true`

Custom label: `aria-labelledby="#some-elementid"`

### Multimedia accessibility
Don't forget to set the alt+title attrs on an image.
For audio/video -> provide more formats + download link if none work.
Make sure the controls for A/V are accessible (e.g: by tab or shortcuts like on youtube).
Can add transcripts for A/V underneath if necessary.
Captioning vs subtitles

### Mobile accessibility
`mouseup, mousedown` -> `ontouchstart, ontouchend`
Do not disable zoom.

### Testing accessibility automatically
Lighthouse in Chrome Developer Tools.








