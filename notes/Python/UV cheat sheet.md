`uv`, `uv help` 
`uv python install`, `uv python list`
`uv python pin` = pin current project to use a specific Python version;

Scripts
`uv run` = run script
`uv add --script` = add a dependency to a script
`uv remove --script` = remove a dependency from a script;
`uv run --with package_name` = run script with dep;
`uv lock --script example.py` = creates lockfile for script;

Projects
`uv init` = create new Python proj
`uv add, uv remove` = add/rem deps
`uv sync` = sync project deps with the environment
`uv lock` = create lockfile for project deps
`uv run` = run command in the project env
`uv tree` = view dependency tree for the project !!!
`uv publish` = publish the project to a package index

Tools
`uv tool run` = run tool in a temp env
`uv tool install` = install a tool user-wide
`uv tool list` = list installed tools
`uv tool update-shell` = update shell to include tool executables

Pip: uv has some sort of compatibility layer with pip. Not interested in that atm.