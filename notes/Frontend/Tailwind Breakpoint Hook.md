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