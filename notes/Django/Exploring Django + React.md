Need to create a base template to create small projects fast.

### Modern Django Tooling

Requirements:
- Dependency manager: can still use conda if the project requires ML libraries, otherwise switch to poetry.
- Base template should include a Postgres Docker image. Currently unsure how to approach testing. For the FastAPI/SQLAlchemy/Pytest stack, I had two containers, one for dev/live and one for testing. I think Django normally creates and tears down a DB for every test? But if you use Pytest?
- Testing: pytest + pytest-django.

### Django + React approaches
1. Fully-separate, use DRF or Django-Ninja and Django as an API basically.
2. Compile + Bundle the React files, serve them as static, only one route index.html, no Django pages OR limited templates? 

With approach 1, you don't have access to a lot of Django features, but keeping the frontend and backend separate should be cleaner/less confusing to work with.
With approach 2, you can still make use of things like CSRF tokens, but they are coupled more tightly.

Approach 1 - multiple deployments
Approach 2 - single deployment

Approach 1 - need to handle CORS, doesn't seem to be too difficult?

### Analysing the Suitnumerique project's approach

Fully split backend/frontend. They also use Next.js for the frontend (- why?)

Using "Crowdin" which seems to be a tool which helps translating/localizing digital content. Makes sense, since tool seems to be usable in multiple languages (i18n).

Using "Keycloak" (open-source IAM software) for authentication and authorization. 
Keycloak already has a lot of logins handled by default (with Google, Facebook, etc.).
How does it integrate with Django/React?
Seems to be another service that needs to be deployed and managed + with its own DB.
Can set up roles in Keycloak too + validate them in the backend.
realm.json = config file for Keycloak;

Gitmoji linting - initially thought it's a joke, but it's actually helpful. Signalling with a single icon whether a commit is a new feature, a bug fix, etc. Should try to incorporate it into a new project, seems fun.

Uses NginX + Gunicorn

.po files for i18n

K8 to manage deployments.

MJML to create responsive emails.

#### Backend:
ServerToServerAuthentication: internal service-to-service API calls. Checks for a bearer token + compares it against a list of whitelisted tokens (so, microservices talking to one another).

OIDCAuthenticationBackend: handles user authentication via OpenID Connect (OIDC) - related to Keycloak probably.

Really good usage of docstrings.

2000-line long viewsets.py

Otherwise, just typical DRF stuff (albeit way more complicated)

#### Frontend
Next.js to handle routing
They are generating a CSRFToken in the header when sending requests to the backend API.
Saving the CSRF token inside the cookie with DRF + retrieving it in the frontend.

Other notes:
Should take a look later at how they actually handle the OpenAI connection. Saw some throttling methods there too, which seemed interesting.