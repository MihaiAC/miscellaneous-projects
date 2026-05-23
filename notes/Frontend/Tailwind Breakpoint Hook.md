```tsx
import { useSyncExternalStore } from "react";

// Reads Tailwind v4 breakpoint values from --breakpoint-* CSS variables on :root.
// useSyncExternalStore: no setState-in-effect, returns false for SSR.
function getQuery(name: string) {
  const value = getComputedStyle(document.documentElement)
    .getPropertyValue(`--breakpoint-${name}`)
    .trim();
  return `(min-width: ${value})`;
}

export function useBreakpoint(name: string) {
  return useSyncExternalStore(
    (onChange) => {
      const mq = window.matchMedia(getQuery(name));
      mq.addEventListener("change", onChange);
      return () => mq.removeEventListener("change", onChange);
    },
    () => window.matchMedia(getQuery(name)).matches,
    () => false,
  );
}
```

Usage: `useBreakpoint('md')`
Also, need to add this in `:root` (since apparently Tailwind doesn't auto-generate them(?)):
```css
:root {
  /* Generate Tailwind breakpoints explicitly, to make useBreakpoint 
  functional. */
  --breakpoint-sm: 40rem;
  --breakpoint-md: 48rem;
  --breakpoint-lg: 64rem;
  --breakpoint-xl: 80rem;
  --breakpoint-2xl: 96rem;
```