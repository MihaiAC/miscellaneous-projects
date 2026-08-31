# Exploring Python's new t-strings.
# Sources:
# https://davepeck.org/2025/04/11/pythons-new-t-strings/
# https://realpython.com/python-t-strings/

from string.templatelib import Template, convert

name = "Alfie"
action = "meows"
sentence = t"{name} is hungry so he {action}."
print(sentence)

# Outputs:
# Template(strings=('$', ' is hungry so he $', '.'),
# interpolations=(
#   Interpolation('Alfie', 'name', None, ''),
#   Interpolation('meows', 'action', None, ''))
# )

# Interpolation(value, expression, conversion, format_spec)
# Example format_spec: ".2f"

# t-strings => intercept + transform input vals before combining into string

print(f"sentence.strings: {sentence.strings}")
print(f"sentence.interpolations: {sentence.interpolations}")
print(f"sentence.values: {sentence.values}")

# t-strings are iterables
for idx, elem in enumerate(sentence):
    print(f"{idx}. {elem}")

# Use isinstance(elem, str) to differentiate.

action_2 = "purrs"
sentence_2 = t"Now he's sated, so he {action_2}."
print(sentence + sentence_2)

# Nested t-strings.
action_3 = (
    "waits until the path is clear, then swiftly jumps out of the window onto the roof."
)
# VSCode's syntax highlighting is not too happy about this one :S
sentence_3 = t"{name} is up for a little mischief, so he {t'{action_3}'}"
print(sentence_3)


# Convert a template to a string, keeping only alphanumerics + extra chars.
ALLOWED_EXTRA = {" ", ".", ","}


def strip_non_alnum(text: str) -> str:
    return "".join(char for char in text if char.isalnum() or char in ALLOWED_EXTRA)


def render_clean(template: Template) -> str:
    parts: list[str] = []
    for item in template:
        if isinstance(item, str):
            parts.append(item)  # trusted template text, untouched
            continue
        # item is an Interpolation.
        value = item.value
        if isinstance(value, Template):  # nested t-string
            rendered = render_clean(value)
        else:
            rendered = format(convert(value, item.conversion), item.format_spec)
        parts.append(strip_non_alnum(rendered))
    return "".join(parts)


combined = " ".join(render_clean(part) for part in (sentence, sentence_2, sentence_3))
print(combined)
