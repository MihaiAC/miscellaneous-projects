Linter + formatter (with a proposal to unify them in 2023, exciting times!)
`uv tool install ruff@latest`

`uv add --dev ruff` -> add ruff as a dep for the current project
then, e.g: `uv run ruff check .`
### Linter

`ruff check`
`ruff check --fix`
`ruff check --unsafe-fixes`
`ruff check --fix --unsafe-fixes`

Control which rules are enabled with `pyproject.toml`
e.g:
```toml
[tool.ruff.lint]
select = ["E", "F"]
ignore = ["F401"]
extend-safe-fixes = ["F601"]
extend-unsafe-fixes = ["UP034"]
```

Can include comments in the code which disable rule(s).
At the end of a line: `# noqa: F841`
Block: 
```python
# ruff: disable[E501, F841]
# some code here
# ruff: enable[E501, F841]
```
Whole file (at the top): `# noqa: F841`

### Formatter
`ruff format` -> formats all the files in the current dir
`ruff format --check` -> checks format + exits with nonzero status if unformatted file is found;
