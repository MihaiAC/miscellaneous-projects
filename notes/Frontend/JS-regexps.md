Some notes from: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Regular_expressions

Two ways to init:
```js
const regex1 = /ab+c/g;
const regex2 = new Regexp("ab+c", "g");
```

Flags:
- `d`: generate indices for substring matches;
Enables `regexp.hasIndices`, also after a match: `regex.exec(string).indices[0]` => e.g: `[0, 3]`

- `g`: global search = regexp should be tested against all possible matches in the string;
- `i`: case insensitive;
- `s`: allows `.` to match newline chars;
- `u`: treat a pattern as a sequence of Unicode code points;
e.g: `const regex = /u{61}/u`
- `v`: `u` + some other stuff;
- `y`: sticky, start match from previous match's `lastIndex`;

`^, $` = start, end of input;

`(?=...), (?!...)` = positive, negative lookaheads
https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Regular_expressions/Lookahead_assertion

`(?<=pattern) (?<!pattern)` = positive, negative lookbehinds;
https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Regular_expressions/Lookbehind_assertion
Pattern must match the input immediately to the left of the current position and current position is not changed before matching subsequent input.


Lookahead => asserts what's on the right
Lookbehind => asserts what's on the left

`.exec` => `[ fullMatch, group1, group2, ... ]`

`\b, \B`  = is / is not a word boundary;
`\1, \2` = backreferences to previous groups;
`(...)` = capturing a group;
`[...], [^...]` = any character in or not in a set of characters;
`\d, \D` - digits
`\w, \W` - word character
`\s, \S` - whitespace, line terminator

Other classic regexp stuff: `.|*+?{n}{n,}{n,m}`
Other non-classic regexp stuff: named backreferences and capturing groups.