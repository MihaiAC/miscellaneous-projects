### Entrypoint
```C#
using System;

class Program
{
	static void Main(string[] args)
	{
		Console.WriteLine("Hello");
	}
}
```

### Var assignments
```C#
int count = 5;
string name = "John";
bool active = true;

// Type inference with 'var'
var age = 30;
```

### Functions
```C#
static int Add(int a, int b)
{
	return a + b;
}

// One-liner
static int Add(int a, int b) => a + b; 
```

### Conditionals
```C#
// Fairly standard stuff...
if (bla > 10) {...}
else if (bla > 5) {...}
else {...}
```

### Switches
Ol' reliable:
```C#
int day = 3;

switch (day)
{
	case 1:
		Console.WriteLine("Monday");
		break;
	...
	default:
		Console...
		break;
};
```

Modern:
```C#
string day = dayOfWeek switch
{
	1 => "Monday",
	2 => "Tuesday",
	_ => "Another day"
};
```

Pattern matching:
```C#
object obj = 123;

string result = obj switch
{
	int i => $"Integer: {i}",
	string s => $"String: {s}",
	_ => "Unknown type"
};
```

`_` replaces `default:`

### Loop flavours
```C#
for (int index = 0; index < 10; index++)
{
	Console.WriteLine(index);
}

string[] names = {"a", "b", "c"};
foreach (string name in names)
{
	Console.WriteLine(name);
}

while (count > 0)
{
	count--;
}
```

### Arrays + useful ops
```C#
int[] numbers = new int[5];
int[] numbers = { 1, 2, 3, 4};

int length = numbers.Length;
Array.Reverse(numbers);
bool exists = Array.Exists(numbers, n => n > 3);
int found = Array.Find(numbers, n => n > 3);
int index = Array.IndexOf(numbers, 3);
Array.Copy(source, target, source.Length);
int[] slice = numbers[1..4];
Array.Fill(numbers, 0);
Array.Clear(numbers, 0, numbers.Length);
string joined = string.Join(",", numbers);
int[] mapped = numbers.Select(n => n * 2).ToArray();
int[] filtered = numbers.Where(n => n > 2).ToArray();

// Resize array
int[] small = new int[10];
Array.Resize(ref small, 20);

// Shallow copy (deep because primitive type)
int[] clone = (int[])numbers.Clone();

// Sort + custom op
Array.sort(numbers);
Array.Sort(words, (left, right) => left.Length.CompareTo(right.Length));
```

### Lists + useful ops
```C#
using System.Collections.Generic;

List<int> numbers = new List<int>();
var numbers = new List<int>();

numbers.Add(10);
numbers.AddRange(new[] {5, 6});
int count = numbers.Count;
numbers.insert(num, idx);
numbers.remove(num);
numbers.RemoveAt(idx);
numbers.RemoveAll(num => num > 5);
bool exists = numbers.Contains(num);
bool any = numbers.Any(x => x > 5);
int found = numbers.Find(x => x > 5);
int index = numbers.IndexOf(num);
numbers.Reverse();
numbers.Clear();
```

### Dictionary + useful ops
```C#
var dict = new Dictionary<string, int>();
dict.add("carol", 30);
dict["dave"] = 200;
bool hasKey = dict.ContainsKey("bob");
bool hasValue = dict.ContainsValue(200);
dict.Clear();
int count = dict.Count;
var keys = dict.Keys;
var values = dict.Values;

// Add only if key doesn't exist
bool added = dict.TryAdd("eve", 44);

// Safely get value out of dict
bool found = dict.TryGetValue("bob", out int value);

// Remove entry by key
bool removed = dict.Remove("carol");

// Iterating
foreach (var pair in dict) { var key = pair.Key; var val = pair.Value; }
foreach (var key in dict.Keys) {...}
foreach (var val in dict.Values) {...}

// Filter
var filtered = dict.Where(pair => pair.Value > 10)
				   .ToDictionary(pair => pair.Key, pair => pair.Value);

// Map
var mapped = dict.ToDictionary(pair => pair.Key, pair => pair.Value * 2);
```

### Example class
```C#
using System;
using System.Collections;
using System.Collections.Generic;

[Serializable]
public class Repository<T>: IEnumerable<T> where T: class, new() 
{
	// Constant
	public const string Version = "1.0";
	
	// Static field
	private static int instanceCount = 0;
	
	// Static property
	public static int InstanceCount => instanceCount;
	
	// Private field
	// readonly = can only be assigned at declaration, and in the constructor
	private readonly List<T> items = new();
	
	// Auto-property
	public string Name { get; set; }
	
	// Property with custom getter/setter
	private int capacity;
	public int Capacity
	{
		get => capacity;
		set
		{
			if (value < 0) throw new ArgumentException("Capacity cannot be negative");
			capacity = value;
		}
	}
	
	// Enable indexing
	public T this[int index]
	{
	    get
	    {
	        if (index < 0 || index >= items.Count)
	            throw new IndexOutOfRangeException($"Index {index} is out of range");
	        return items[index];
	    }
	    set
	    {
	        if (index < 0 || index >= items.Count)
	            throw new IndexOutOfRangeException($"Index {index} is out of range");
	        items[index] = value;
	    }
	}
	
	// Constructor
	public Repository(string name, int capacity = 100)
	{
		Name = name;
		Capacity = capacity;
		instanceCount++;
	}
	
	// Static constructor
	static Repository()
	{
		Console.WriteLine("Repository class initialised");
	}
	
	// Method with generics
	public U Transform<U>(int index, Func<T, U> mapper)
	{
		return mapper(this[index]);
	}
	
	// ... so on and so forth, got a bit bored
}
```

### Enums
```C#
// Init
enum DayOfWeek { Monday, Tuesday, ...}

// Assign
var today = DayOfWeek.today;

// enum -> int
int num = (int)today; // 0

// int -> enum
DayOfWeek day = (DayOfWeek)1;

// int -> enum, proper way
if (Enum.IsDefined(typeof(DayOfWeek), value)) {...}

// string -> enum
DayOfWeek parsed = Enum.Parse<DayOfWeek>("Friday"); //throws if undefined
bool ok = Enum.TryParse<DayOfWeek>("Friday", out DayOfWeek safeParsed); // safe parsing

// Get all values
DayOfWeek[] days = (DayOfWeek[])Enum.GetValues(typeof(DayOfWeek));

// Get all names
string[] names = Enum.GetNames(typeof(DayOfWeek));
```

### Enums + bit operations
```C#
[Flags]
enum FileAccess
{
	None = 0,
	Read = 1,
	Write = 2,
	Execute = 4
}

// Combine flags
FileAccess perms = FileAccess.Read | FileAccess.Write;

// Check if flag is set
bool canWrite = (perms & FileAccess.Write) != 0;

// Remove flag
perms &= ~FileAccess.Read;

// Toggle flag (xor)
perms ^= FileAccess.Execute;

// Check if no flags are set
if (perms == FileAccess.None)

// Iterate over all set flags
foreach (FileAccess flag in Enum.GetValues(typeof(FileAccess)))
{
	if (perms.HasFlag(flag)) Console.WriteLine(flag);
}
```

### Nullable types
Somewhat similar to TS
```C#
// Nullable init
int? maybeInt = null;
// int? = Nullable<int>

// Check for val
if (maybeInt.HasValue) {...}
if (maybeInt is int num) {...}
int valOrDefault = maybeInt ?? 0;

// Null-coalescing assignment
maybeInt ??= 42;

// Same as TS
string? name = person?.Name;
```

### String interpolation
```C#
string name = "Alice";
Console.WriteLine($"Hello {name}");
```

### Out parameters
```C#
if (int.TryParse("42", out int number))
{
	Console.WriteLine(number);
}
```

### Ref
Pass value by reference.
Pretty self-explanatory for primitive types.
For objects:
```C#
void ReplaceList(ref List<int> list)
{
	// This reassigns the original variable
	list = new List<int> { 1, 2, 3 };
}
```

### Main types
**Numeric**:
```
byte: [0, 255]
sbyte: [-128, 127]
short: [-32768, 32767]
ushort: [0, 65535]
int: [-2**31, 2**31-1]
uint: [0, 2**32-1]
long: [-2**63, 2**63-1]
ulong: [0, 2**64-1]
```

**Floating-point / fractional types:**
```
float: +-1.5e-45 -> +-3.4e38 = 7-digit precision;
double: +-5e-324 -> +-1.7e308 = 15-16 digit precision;
decimal: +-1e-28 -> 7.9e28 = 28-29 digits precision
```
**Big integers:** `System.Numerics.BigInteger`
**Big fractional numbers:** `System.Numerics.BigRational`
**Complex:** `System.Numerics.Complex`
**Non-numeric types:** `bool, char, string, object, dynamic, DateTime, TimeSpan, Guid, Nullable<T>, enum, Tuple<T1, T2, ...>, ValueTuple`

**Literal suffixes:**
```
123 -> int
123L -> long
123.0 -> double
123.0f -> float
123.0m -> decimal
'A' -> char
"abc" -> string
```

**Memory-related types:**
(context) stack = very fast, temporary, method-local; heap = garbage-collected, slower, persistent, large or long-lived objects.

`Span<T>` = stack-only, mutable slice, zero (heap) allocation; normally slicing would require making a copy of the thing that's saved on the heap; contains a pointer and a length;

`ReadOnlySpan<T>` = stack-only, immutable slice, zero-allocation;

`Memory<T>` = persistent, heap-safe slice, zero-allocation access to parts of memory;


### Meta
- Who thought it was a good idea to give `{` a whole damn line? Plus, it looks so ugly :( NB: Got used to it after writing all these examples though.
- Should look into parallelism in C# sometime.