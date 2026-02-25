# Data Design Decisions - KWIC System

## 1. Storage of Input Lines
**Data Structure:** `ArrayList<Sentence>`

The `Sentence` class bundles a list of words with their original line number metadata. Using an `ArrayList` ensures sentences are stored in the exact order they appear in the file for accurate tracking.



---

## 2. Storage of Circularly Shifted Lines
**Data Structure:** `ArrayList<Sentence>`

Shifted lines are stored as new `Sentence` objects that carry the original line number. Using the same data structure for shifts as the input allows sorting and formatting modules to process the data without extra conversion.



---

## 3. Storage of Sorted Lines
**Data Structure:** `ArrayList<Sentence>` (Sorted in-place)

The list is sorted alphabetically using Java's built-in sorting utilities. Since the line number is stored inside each `Sentence` object, the relationship between the text and its source is preserved during the shuffle.

---

## 4. Key-Word Indexing 
**Data Structure:** `HashMap<String, Set<Integer>>`

A `HashMap` provides instant word lookup for the search and index commands. Using a `Set` for the values ensures each line number is only recorded once per word, even if that word appears multiple times in a single sentence.