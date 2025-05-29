Crate = library | binary (executable) = basic unit of compilation
Every target defined for a Cargo package is a crate. Why do we have two names for them?
Types of targets:
1. Cargo target. Cargo packages consist of targets which correspond to artifacts that will be produced: library, binary, example, test, benchmark.
   List of targets in Cargo.toml manifest.
2. Target directory. Where all artifacts + interm files are put.
3. Target architecture (OS + machine)
4. Target triple = way to specify target architecture, `<arch><sub>-<vendor>-<sys>-<abi>` e.g: armv7-pc-linux-gnu
A crate can be subdivided into modules.

Cargo = Rust package manager, ensures repeatable build.

Creating a binary program:
`cargo new hello_world --bin --vcs none`
`--bin` can be replaced by `--lib` (but it also initialises a new git repo by default)
`cargo build --release` = compile files with optimizations turned on.

Add packages by first updating the .toml deps:
```toml
[dependencies]
time="0.1.12"
regex="0.1.41"
```

#### Rust project structure
`src/lib.rs` = default library file
`src/main.rs` = default exe file
`benches` = benchmarks
`tests` = integration tests

#### Cargo.toml vs Cargo.lock
.toml describes dependencies, you write it, .lock generated from .toml by Cargo, don't edit it

Revisions in deps: 
```toml
regex = { git = "https://github.com/rust-lang/regex.git", rev = "9f9f693" }
```
Can update packages to latest version with `cargo update`, `cargo update specific_package`.

#### Ownership

Safety = absence of undefined behaviour

**Variables live in the stack.**
Variables live in frames; frame = mapping from variable to value within a single scope, like a function.
Frames are organized into a stack of currently-called functions. 

**Boxes live in the heap**
Heap = separate region of memory where data can live indefinitely, not tied to a specific stack frame.
Box = construct that allows putting data on the heap.

**Rust does not permit manual memory management**
A box's owner manages deallocation.
```rust
let a = Box::new([0; 1_000_000]);
let b = a;
```
First line: allocates an array of 1M zeros and binds it to a.
Second line: ownership is transferred from a to b.

Can **clone** elements.

### References and Borrowing
```rust
fn main() {
    let m1 = String::from("Hello");
    let m2 = String::from("world");
    greet(m1, m2);
    let s = format!("{} {}", m1, m2); // Error: m1 and m2 are moved
}

fn greet(g1: String, g2: String) {
    println!("{} {}!", g1, g2);
}
```
This is so weird, so the greet function moves m1 and m2??
As an inconvenient workaround, you could return g1, g2 in the greet function.
Convenient solution: references
```rust
fn main() {
    let m1 = String::from("Hello");
    let m2 = String::from("world");
    greet(&m1, &m2); // note the ampersands
    let s = format!("{} {}", m1, m2);
}

fn greet(g1: &String, g2: &String) { // note the ampersands
    println!("{} {}!", g1, g2);
}
```
g1 does not own m1 and neither the heap string "Hello".

**References** = non-owning pointers.

```rust
// Puts the number 1 on the heap.
// x is a pointer on the stack, pointing to the 1 on the heap.
// x OWNS the 1 on the heap.
let mut x: Box<i32> = Box::new(1);

// Since a is an i32, the following operation makes a copy of the value referenced by x.
// The copied value is on the stack, with a pointing to it.
let a: i32 = *x;

// This modifies the heap value x is pointing to.
*x += 1;

// If we were to print x and a in this order, we would get 2 and 1.

// r1 is a pointer on the stack, that points to x.
let r1: &Box<i32> = &x; 

// *r1 = x (the box itself)
// **r1 = 1 (the integer inside the box)
let b: i32 = **r1;

// *x dereferences the Box<i32>, so is basically the integer on the heap
// &*x = direct reference to the i32 on the heap.
let r2: &i32 = &*x;

// c is the value on the heap.
let c: i32 = *r2; 
```

Implicit de-referencing with the dot operator:
```rust
// x is on the stack, pointing to an i32 on the heap (-1)
let x: Box<i32> = Box::new(-1);

// Two ways to dereference x and call abs on it.
let x_abs1 = i32::abs(*x);
let x_abs2 = x.abs();
assert_eq!(x_abs1, x_abs2);
```

```rust
// r is on the stack, pointing to x, which is also on the stack.
let r: &Box<i32> = &x;

// De-referencing the "old way", *r = x, **r = -1
let r_abs1 = i32::abs(**r);

// De-referencing with the dot.
let r_abs2 = r.abs();
```

I don't understand this one yet.
```rust
let s = String::from("Hello");
let s_len1 = str::len(&s);
let s_len2 = s.len();   
assert_eq!(s_len1, s_len2);
```
So s is a string.
The len function requires a string address (why?)

```rust
fn main() {
	let x = Box::new(0);
	let y = Box::new(&x);
}
```
How many dereferences on y to copy 0?

So. x is on the stack, pointing to a 0 on the heap. y is on the stack, pointing to the heap to a reference to x. 
`*y = &x`
`**y = x`
`***y = 0`