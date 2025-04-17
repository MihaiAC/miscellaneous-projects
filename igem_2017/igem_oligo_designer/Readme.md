# Oligo Designer

A Java-based tool to split DNA sequences into parts, under the given constraints.

## Constraints

### Fusion Site Rules

- Each fusion site must:
  - Be exactly **4 base pairs** long
  - Contain **at least one G or C**
  - Contain **at least one A or T**
  - **Not** be palindromic with its reverse complement
    (e.g., AGCT is palindromic: its complement is TCGA → reverse = AGCT)
  - Be **sufficiently different** from other fusion sites
    - A maximum of **2 positions** can have the same base
    - Examples of invalid pairs:
      - `AGTT` vs `AGAT` → 3 same positions → invalid
      - `ATCT` vs `AGAT` → reverse complement of `ATCT` is `TAGA` → `AGAT` → not different enough

### Repeat Placement

- Repeats longer than a given threshold (e.g. 15bp) must be placed in separate parts
- If a repeat appears in both forward and reverse directions, both instances must be in different parts
- One repeat may appear twice in a single part only if its length ≤ repeat_len_tolerance

### Part Length

- Each part must be between 45 and 200 base pairs

### Output Preferences

- Prefer solutions with the minimum number of parts
- Sequence split positions must be annotated

## How It Works

The program breaks the input DNA sequence into valid parts by detecting repeats, generating possible fusion sites, and solving a constraint satisfaction problem.

### 1. Repeat Detection

Repeats are detected using an efficient method based on suffix arrays.

Implements the approaches from:

- [Efficient repeat finding via suffix arrays](https://arxiv.org/pdf/1304.0528.pdf)
- [Linear Work Suffix Array Construction](https://www.cs.helsinki.fi/u/tpkarkka/publications/jacm05-revised.pdf)

### 2. Fusion Site Analysis

Identifies all possible fusion sites in the sequence that meet the required constraints.

Builds a constraint graph where:

- Nodes = valid fusion sites
- Edges = conflicts - two fusion sites are connected if they cannot be selected together

### 3. Constraint Satisfaction Problem (CSP)

- The problem is modeled as a CSP
- Naively attempts every valid combination of fusion sites
- Iterates over increasing numbers of splits:
  - From 1 up to `floor(sequence length / min part length)`
- Stops early when a valid configuration is found that:
  - Satisfies all fusion site constraints
  - Places repeats in compliant positions
  - Keeps part lengths within bounds
- Once found, the program outputs the final part split and exits

## How to Run

### 1. Open the Project in Eclipse

- Open Eclipse IDE.
- Import the project as an existing Java project.

### 2. Configure Run Settings

- Open `Main.java` in Eclipse.
- Go to **Run Configurations**
- Under **Arguments**, provide the input as:
  `"<SEQUENCE>" <min_repeat_len> <min_part_len> <max_part_len> <repeat_len_tolerance>`

### 3. Run the Program

Click **Run** in Eclipse.

### 4. Output

Results are saved in `output.txt` in the project folder, containing detected repeats and part splits (if found).
