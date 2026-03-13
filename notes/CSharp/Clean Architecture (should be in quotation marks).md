Dependencies point inwards:
Inner first:
- Entities: enterprise business rules (aka: stuff that doesn't depend on me, but on other people, the ones that give me the reqs?);
- Use cases: application business rules (how I choose to model interactions with the entities);
- Controllers | Presenters | Gateways: (glue code between outer layer and inner layer);
- Web | UI | External interfaces | DB | Devices;

Some general recommendations (Architecture...):
- ...should be independent from frameworks (in practice, it seems to be dependent on the framework, the skill of the devs and of the people giving out the requirements);
- Testable (good luck when speed's all that matters);
- Business logic should be independent from the (external?) interface?;
- Independent from the DB.
