#### Ch.1 ####
Black to auto-format code
Flake8 to enforce Pep8 - can install VSCode extension
Can create pre-commit hooks to deny a commit if the code style is violated.
Switch from conda to virtual envs and pyproject.toml for standalone python project repos.
See .vscode file in the first Django project for configuring flake8 and Black.

Relative imports
No import *

ESLint for Javascript

#### Ch.2 ####
Use PostgreSQL when developing.
conda vs pip + virtualenv.

#### Ch.3 ####
Cookiecutter for advanced project templating.
Re-arranging project structure to `configuration_root` and `django_project_root`, but that seems to be a thing for more complex projects.

#### Ch.4 ####
App names = single word
Apps = singular focus, small
Common + uncommon app modules.

#### Ch.5 ####
Changing settings in prod => server restart.
Keep secret keys out of version control (automatic tools can be incorporated in GitHub actions, e.g.: Trufflehog)
No `local_settings.py` 
Multiple settings files: `base, local, staging, test, production` + select it at server runtime OR set `DJANGO_SETTINGS_MODULE` 
If multiple settings files => maybe multiple requirements files.
Unset env vars after you no longer need them.

!! Custom environment variable loading function -> throw custom error.
Packages for this: `django-environ`, `django-configurations`

`django-admin` for multiple setting files?

For services in which environment variables do not work, is it possible to encrypt/decrypt the config files with an environment variable?

### Ch.6 - Models###
Django abstract base class for common fields?
Custom save and delete methods -> won't be called by RunPython.
Always back up data before a migration.
Can take a while if you have a lot of data.

Start with models representing a normalised database (google 1NF -> 4NF ).
If denormalisation is necessary, try caching first.

When to use `null vs blank`:
- text fields: blank = True => stored as empty strings unless null=True and unique=True, then stored as NULL
- images/files: no null, blank ok;
- boolean, numbers, dates, keys, IP, many to many: both ok; if blank=True => also set null=true;

BinaryField - if too large => save to file + FileField

When saving numeric data from Python to Postgres, check that it fits.

Model  `_meta` API.

Model Manager = interface used when interacting with the database (through the ORM) - stored in `objects`?

Rather than having big models, you can do:
- Abstract classes representing behaviors that you can import into your models (e.g: Publishable, Timestampable, etc.)
OR
- Stateless helper functions.




