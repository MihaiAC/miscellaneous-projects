Source: https://www.freecodecamp.org/news/complete-micro-frontends-guide/
From the get-go I am pretty skeptical, but it may be relevant to e.g: switching from one framework to another, or modernising an old codebase.

Monolithic apps -> good for small to medium projects.

Micro-frontends = equivalent of micro-services for frontend
Aka: multiple frontend modules, each owned by a different team, deployed separately, integrated at runtime.

Use cases presented by the tutorial:
- Independent deployments
- Team autonomy
- Incremental upgrades - migrate legacy components to a modern stack
- Better scalability (?)

No drawbacks presented - fishy.

Some drawbacks:
- Advanced CI/CD needed, coordination to avoid breaking things. 
- Shared contracts (design systems, APIs).
- Team autonomy -> every team will use own tooling, harder onboarding, shared UI will be harder to keep consistent.
- Can use multiple frameworks -> user needs to download more things.
- Runtime conflicts + CSS bleed.
- Testing becomes harder.
- Slower local dev.
- Handling routing - this one particularly doesn't sound fun.

Teams that have successfully used this pattern:
- Spotify -> iframes + custom infra
- Zalando -> "Project Mosaic"  = in-house microfrontend framework, heavy investment
- BMW 
- American Express -> Built "One-App" = React-based framework to handle microfrontends.
- IKEA

It makes sense - it looks like an architecture that is needed for large apps and it seems that it requires a big team and large investment in tooling to make work.

Proceeding with the tutorial though, mostly out of morbid curiosity now.

### iFrames
Aka embedding an HTML page inside another HTML page.
So I guess that the main app acts like a message broker for the iframes?

### Web components
https://developer.mozilla.org/en-US/docs/Web/API/Web_components
Pages are contained with a "shadow DOM" (aka no need to worry about scripts + styling collisions).

Pros:
- Framework-agnostic
- Natively supported by browsers
- Easy communication
Cons:
- Integration difficulties -> need to properly manage communication between apps
- Limited support for older browsers
- Global state isolation -> no built-in way to share state across components -> need to implement your own solution for this


### single-spa
https://single-spa.js.org/ = way to build+run multiple independent SPAs on the same webpage;

Each SPA is responsible for one part of the UI + is loaded dynamically based on routes.

NEVER use this approach again if you want to keep your sanity.

You need a PhD in WebPack to make this work:
- Webpack needs to bundle everything in a way in which SystemJS can run it.
- Need to configure SystemJS to load the different SPAs (how much fun that was oh boy)
- Need to configure webpack to not bundle the `@acme/shared-state` import (SystemJS provides it at runtime).
- Need to setup shared state that the different SPAs can read and modify (easy for a simple counter, but how would this look with more complex state or when both need to RW?)

Now, the tutorial we're following has some pros and cons to give.
Pros:
- Built-in routing & lifecycles -> may be, but the setup is a nightmare
- Cross-framework support -> maybe, but you'll lose your sanity
- Fine-grained loading -> only load the active app -> woooo more config!
Cons:
- Complex learning curve - agreed
- Configurations can get verbose - understatement of the century; congrats you don't need to config one app, you need to config 2 or 3 and you have to config the shared state and then you have to config systemJS, and it's all in WebPack!!! this is amazing :)))
- More boilerplate - again, understatement

Oh and don't get me started on trying to make Angular work with this. That framework + single-spa is absolutely cursed. 

Couldn't make it work and replaced it with Vue. Again, this is for a simple app in which React has a button that when pressed increments a counter displayed by Vue (Angular previously). I shudder to think what would happen for more complex use cases. 

Again, WebPack PhD and SystemJS knowledge required.

### Module federation
https://module-federation.io/
Webpack feature to enable MFE

Main host app coordinates remote apps.
It was surprisingly less painful to set up than single-spa.
Still had to write 4 (5?) webpack configs, but they worked with minimal tweaks needed.

### Conclusions
MFEs seem to trade application complexity for configuration and tooling complexity. 
And I mean I don't buy this either. Applications will still be complex too. So what exactly are we doing here? No clue.

Issues:
- Dependency hell
- Loader hell
- Framework interop hell (the React wrapper component needs to know how to mount the Vue app)
- Module system hell

Single-spa is cursed (or maybe just SystemJS). I am not touching that ever again.
Others were actually not so bad - I don't think I'll ever use MFE but it was a fun little project.

